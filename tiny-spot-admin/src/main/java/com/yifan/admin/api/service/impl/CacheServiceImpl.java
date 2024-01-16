package com.yifan.admin.api.service.impl;

import com.yifan.admin.api.constant.RedisKeys;
import com.yifan.admin.api.constant.SysConstant;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.model.data.StoreWsTokenData;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void setMenusList(Long adminId, List<SysMenu> menuList) {
        if (menuList.isEmpty()) {
            log.info("[SysCacheService - setMenusList] menuList is empty. adminId: {}.", adminId);
            return;
        }
        RList<SysMenu> rList = redissonClient.getList(RedisKeys.menusKey(adminId));
        boolean addAll = rList.addAll(menuList);
        rList.expire(Duration.ofSeconds(SysConstant.CACHE_TIME_OUT));

        log.info("[setMenusList] menuList add cache result: {}, adminId: {}.", addAll, adminId);
    }

    @Override
    public List<SysMenu> getMenusList(Long adminId) {
        RList<SysMenu> menusRList = redissonClient.getList(RedisKeys.menusKey(adminId));
        return Optional.ofNullable(menusRList.readAll()).orElse(Collections.emptyList());
    }

    @Override
    public void delMenusList(Long adminId) {
        RList<SysMenu> menusRList = redissonClient.getList(RedisKeys.menusKey(adminId));
        boolean delResult = menusRList.delete();
        log.info("[delMenusList] delete menuList in cache result: {}.", delResult);
    }


    @Override
    public Set<String> getPermissionSet(Long adminId) {
        RSet<String> permissionSetBucket = redissonClient.getSet(RedisKeys.permissionKey(adminId));
        return Optional.ofNullable(permissionSetBucket.readAll()).orElse(Collections.emptySet());
    }

    @Override
    public void setPermissionSet(Long adminId, Set<String> permissionSet) {
        RSet<String> rSet = redissonClient.getSet(RedisKeys.permissionKey(adminId));
        boolean addAllResult = rSet.addAll(permissionSet);
        rSet.expire(Duration.ofSeconds(SysConstant.CACHE_TIME_OUT));
        log.info("[setPermissionSet] permissionSet add cache result: {}, adminId: {}.", addAllResult, adminId);
    }

    @Override
    public void delPermissionSet(Long adminId) {
        RSet<String> redissonClientSet = redissonClient.getSet(RedisKeys.permissionKey(adminId));
        Boolean aBoolean = redissonClientSet.delete();
        log.info("[delPermissionSet] result: {}, adminId: {}.", aBoolean, adminId);
    }

    @Override
    public String generateToken(SysUser user) {
        boolean tokenUsed;
        String token;
        do {
            token = TokenUtil.token(user.getId(), user.getUsername(), RandomStringUtils.random(32, true, true));
            tokenUsed = (getUser(token) != null);
        } while (tokenUsed);
        cacheToken(token, user);
        return token;
    }


    @Override
    public void cacheToken(String token, SysUser user) {
        log.info("[cacheToken] token: {}, userId: {}.", token, user.getId());
        RBucket<SysUser> bucket = redissonClient.getBucket(RedisKeys.tokenKey(token));
        bucket.set(user, SysConstant.CACHE_TOKEN_TIME_OUT, TimeUnit.SECONDS);
    }


    @Override
    public boolean checkToken(String token) {
        log.info("[checkToken] token: {}.", token);
        RBucket<SysUser> bucket = redissonClient.getBucket(RedisKeys.tokenKey(token));
        SysUser user = bucket.get();
        if (user != null) {
            log.info("[checkToken] valid token: {}.", token);
            bucket.expire(Duration.ofSeconds(SysConstant.CACHE_TOKEN_TIME_OUT));
            return true;
        }
        log.info("[checkToken] invalid token: {}.", token);
        return false;
    }

    @Override
    public boolean deleteToken(String token) {
        RBucket<SysUser> bucket = redissonClient.getBucket(RedisKeys.tokenKey(token));
        boolean del = bucket.delete();
        log.info("[deleteToken] delete token: {}, result: {}.", token, del);
        return del;
    }


    @Override
    public SysUser getUser(String token) {
        RBucket<SysUser> bucket = redissonClient.getBucket(RedisKeys.tokenKey(token));
        return bucket.get();
    }

    @Override
    public void cacheCaptcha(String randomStr, String text) {
        log.info("[cacheCaptcha] randomStr: {}, text: {}.", randomStr, text);
        RBucket<Object> redissonClientBucket = redissonClient.getBucket(RedisKeys.captchaKey(randomStr));
        redissonClientBucket.set(text, SysConstant.CACHE_CAPTCHA_TIME_OUT, TimeUnit.SECONDS);
    }

    @Override
    public String getCapacha(String randomStr) {
        log.info("[getCapacha] randomStr: {}.", randomStr);
        RBucket<String> bucket = redissonClient.getBucket(RedisKeys.captchaKey(randomStr));
        log.info("[getCapacha] test: {}.", bucket.get());
        return bucket.get();
    }

    @Override
    public boolean deleteCapacha(String randomStr) {
        log.info("[deleteCapacha] randomStr: {}.", randomStr);
        RBucket<String> bucket = redissonClient.getBucket(RedisKeys.captchaKey(randomStr));
        boolean delete = bucket.delete();
        log.info("[deleteCapacha] delete cache captcha result: {}.", delete);
        return delete;
    }

    @Override
    public StoreWsTokenData getWsTokenInfo(String wsToken) {
        RBucket<StoreWsTokenData> rBucket = redissonClient.getBucket(RedisKeys.wsTokenKey(wsToken));
        return rBucket.get();
    }

    @Override
    public void cacheWsTokenInfo(String wsToken, StoreWsTokenData data) {
        RBucket<StoreWsTokenData> bucket = redissonClient.getBucket(RedisKeys.wsTokenKey(wsToken));
        bucket.set(data, 300, TimeUnit.SECONDS);
    }

    @Override
    public Boolean existWsToken(String wsToken) {
        RBucket<StoreWsTokenData> bucket = redissonClient.getBucket(RedisKeys.wsTokenKey(RedisKeys.wsTokenKey(wsToken)));
        return bucket.isExists();
    }

    @Override
    public void removeWsToken(String wsToken) {
        RBucket<StoreWsTokenData> bucket = redissonClient.getBucket(RedisKeys.wsTokenKey(wsToken));
        bucket.delete();
    }
}
