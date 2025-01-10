package com.yifan.admin.api.strategy.oAuth2.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yifan.admin.api.constant.SysConstant;
import com.yifan.admin.api.entity.SysRole;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.entity.SysUserRole;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.mapper.SysUserMapper;
import com.yifan.admin.api.mapper.SysUserRoleMapper;
import com.yifan.admin.api.model.dto.CodeDTO;
import com.yifan.admin.api.model.dto.SocialTokenDTO;
import com.yifan.admin.api.model.dto.SocialUserInfoDTO;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.service.SysRoleService;
import com.yifan.admin.api.service.UserLoginLogService;
import com.yifan.admin.api.service.pwdEncoder.PasswordEncoder;
import com.yifan.admin.api.strategy.oAuth2.SocialLoginStrategy;
import com.yifan.admin.api.utils.DateTimeUtil;
import com.yifan.admin.api.utils.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * 抽象登录模板
 */
@Service
@Slf4j
public abstract class AbstractLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private UserLoginLogService loginLogService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private SysRoleService roleService;

    @Override
    public String authUri() {
        return getSocialAuthUri(UuidUtils.randomUUID());
    }

    @Override
    public String login(CodeDTO data) {
        SysUser user;
        // 获取token
        SocialTokenDTO socialToken;
        try {
            socialToken = getSocialToken(data);
        }catch (Exception e){
            log.error("error", e);
            throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), "授权已过期，请重新授权");
        }
        // 获取用户信息
        SocialUserInfoDTO socialUserInfoVO = getSocialUserInfo(socialToken);
        // 判断是否已注册
        SysUser existUser = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, socialUserInfoVO.getUsername())
                .eq(SysUser::getUserResource, socialToken.getUserResource()));
        // 用户未登录过
        if (Objects.isNull(existUser)) {
            user = saveLoginUser(socialToken, socialUserInfoVO);
        } else {
            if (existUser.getStatus().equals(StatusEnum.DISABLE.getStatus())) {
                throw new ApiException(ResultCode.OAUTH2_LOGIN_ERROR.getCode(), ResultCode.USER_DISABLE.getMsg());
            }
            user = existUser;
        }
        String token = cacheService.generateToken(user);
        loginLogService.insertAdminLoginLog(user.getId());
        return token;
    }

    /**
     * 获取第三方Token
     *
     * @param data data
     * @return {@link SocialTokenDTO} 第三方token
     */
    public abstract SocialTokenDTO getSocialToken(CodeDTO data);

    /**
     * 获取第三方用户信息
     *
     * @param socialToken 第三方token
     * @return {@link SocialUserInfoDTO} 第三方用户信息
     */
    public abstract SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialToken);

    /**
     * 获取重定向地址
     *
     * @return
     */
    public abstract String getSocialAuthUri(String state);

    /**
     * 新增用户账号
     *
     * @param socialToken 第三方Token
     * @return {@link SysUser} 登录用户身份权限
     */
    @Transactional
    public SysUser saveLoginUser(SocialTokenDTO socialToken, SocialUserInfoDTO socialUserInfoVO) {
        // 新增用户信息
        SysUser newUser = new SysUser();
        newUser.setIcon(socialUserInfoVO.getAvatar());
        newUser.setNickName(socialUserInfoVO.getNickname());
        newUser.setUsername(socialUserInfoVO.getUsername());
        final String encodePassword = passwordEncoder.encode(SysConstant.INIT_PASSWORD, newUser.getUsername());
        newUser.setPassword(encodePassword);
        newUser.setUserType(RoleEnum.USER.toString());
        newUser.setUserResource(socialToken.getUserResource());
        newUser.setOpenId(socialToken.getOpenId());
        newUser.setCreateTime(DateTimeUtil.currentDate());

        userMapper.insert(newUser);

        // 新增用户角色
        LambdaQueryWrapper<SysRole> lambda = new QueryWrapper<SysRole>()
                .lambda()
                .eq(SysRole::getName, RoleEnum.USER.toString())
                .eq(SysRole::getStatus, StatusEnum.USABLE.getStatus());
        SysRole sysRole = roleService.getOne(lambda, false);
        if (sysRole == null) {
            throw new ApiException("service error");
        }

        SysUserRole role = new SysUserRole();
        role.setAdminId(newUser.getId());
        role.setRoleId(sysRole.getId());

        userRoleMapper.insert(role);
        return newUser;
    }

}
