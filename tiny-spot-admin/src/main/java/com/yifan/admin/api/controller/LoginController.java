package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yifan.admin.api.annotition.CaptchaCheck;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysRole;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.model.params.CommonLoginParam;
import com.yifan.admin.api.model.params.UpdateUserInfoParam;
import com.yifan.admin.api.model.params.UpdateUserPwdParam;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.*;
import com.yifan.admin.api.service.storage.FileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/4/11 11:18
 */
@RestController
@Api(tags = "登录控制器")
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserLoginLogService loginLogService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    SysRoleService roleService;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @CaptchaCheck(value = "login")
    public BaseResult<Map<String, Object>> login(@Validated @RequestBody CommonLoginParam umsAdminLoginParam) {
        SysUser sysAdmin = userService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (sysAdmin == null) {
            return BaseResult.validateFailed("用户名或密码错误");
        }
        String token = cacheService.generateToken(sysAdmin);
        loginLogService.insertAdminLoginLog(sysAdmin.getId());

        sysAdmin.setPassword("******");

        Map<String, Object> data = new HashMap<>();
        data.put("userInfo", sysAdmin);
        data.put("token", token);

        return BaseResult.ok(data);
    }

//    @ApiOperation(value = "注册")
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    @CaptchaCheck(value = "register")
//    public BaseResult register(@Validated @RequestBody CommonRegisterParam registerParam) {
//        return userService.register(registerParam.getUsername(), registerParam.getPassword());
//    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getAdminInfo() {
        Long userId = RequestContext.getUser().getId();
        SysUser user = userService.getById(userId);
        user.setPassword("******");

        Map<String, Object> data = new HashMap<>();
        data.put("userInfo", user);

        Set<String> permissionsSet = new HashSet<>();
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(user.getUserType())) {
            permissionsSet.add("*:*:*");
        } else {
            List<SysMenu> menusList = menuService.getMenuNodeTree(userId);
            permissionsSet.addAll(menusList.stream().map(SysMenu::getPermissionCode).collect(Collectors.toSet()));
        }

        data.put("permissions", permissionsSet);

        List<SysRole> roleList = roleService.getRoleList(userId);
        Set<String> roles = new HashSet<>();
        if (CollectionUtils.isNotEmpty(roleList)) {
            roles = roleList.stream().map(SysRole::getName).collect(Collectors.toSet());
        }
        data.put("roles", roles);
        return BaseResult.ok(data);
    }

    @ApiOperation("修改自己账号密码")
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public BaseResult<Void> updatePassword(@Validated @RequestBody UpdateUserPwdParam updatePasswordParam) {
        userService.updatePassword(updatePasswordParam);
        return BaseResult.ok();
    }

    @ApiOperation("修改自己账号头像")
    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST)
    public BaseResult<String> updateAvatar(@RequestPart(value = "file") MultipartFile file) {
        final String avatarUrl = fileStorageService.fileUpload(file);
        Long userId = RequestContext.getUser().getId();
        LambdaUpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getId, userId)
                .set(SysUser::getIcon, avatarUrl);
        userService.update(updateWrapper);
        return BaseResult.ok(avatarUrl);
    }

    @ApiOperation("修改自己账号信息")
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public BaseResult<Boolean> updateInfo(@RequestBody @Valid UpdateUserInfoParam param) {
        SysUser umsAdmin = RequestContext.getUser();
        LambdaUpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<SysUser>().lambda()
                .eq(SysUser::getId, umsAdmin.getId())
                .set(SysUser::getNickName, param.getNickName())
                .set(SysUser::getEmail, param.getEmail())
                .set(SysUser::getNote, param.getNote());

        boolean update = userService.update(updateWrapper);
        return BaseResult.ok(update);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public BaseResult<Void> logout() {
        cacheService.deleteToken(RequestContext.getToken());
        cacheService.delPermissionSet(RequestContext.getUser().getId());
        cacheService.delMenusList(RequestContext.getUser().getId());
        return BaseResult.ok();
    }

}
