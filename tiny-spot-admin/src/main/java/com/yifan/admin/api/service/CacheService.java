package com.yifan.admin.api.service;

import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.model.data.StoreWsTokenData;

import java.util.List;
import java.util.Set;

/**
 * 后台用户缓存管理Service
 */
public interface CacheService {

    /**
     * 缓存用户菜单
     * @param adminId
     * @param menuList
     */
    void setMenusList(Long adminId, List<SysMenu> menuList);

    /**
     * 获取缓存菜单
     * @param adminId
     * @return
     */
    List<SysMenu> getMenusList(Long adminId);

    /**
     * 删除缓存菜单
     * @param adminId
     */
    void delMenusList(Long adminId);

    /**
     * 获取缓存权限码
     * @return
     */
    Set<String> getPermissionSet(Long adminId);

    /**
     * 设置后台用户权限码列表
     */
    void setPermissionSet(Long adminId, Set<String> permissionSet);

    /**
     * 删除用户缓存权限码
     * @param adminId
     */
    void delPermissionSet(Long adminId);


    String generateToken(SysUser user);

    /**
     * 缓存token-用户 对应关系
     * @param token
     * @param umsAdmin
     */
    void cacheToken(String token, SysUser umsAdmin);

    /**
     * 检查用户token
     * @param token
     * @return
     */
    boolean checkToken(String token);

    /**
     * token删除缓存用户
     * @param token
     */
    boolean deleteToken(String token);

    /**
     * token获取缓存用户
     * @param token
     * @return
     */
    SysUser getUser(String token);

    void cacheCaptcha(String randomStr, String text);

    String getCapacha(String randomStr);

    boolean deleteCapacha(String randomStr);

    StoreWsTokenData getWsTokenInfo(String wsToken);

    void cacheWsTokenInfo(String wsToken, StoreWsTokenData data);

    Boolean existWsToken(String wsToken);

    void removeWsToken(String wsToken);
}
