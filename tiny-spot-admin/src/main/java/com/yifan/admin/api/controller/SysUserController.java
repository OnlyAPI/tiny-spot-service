package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.SysRoleService;
import com.yifan.admin.api.service.SysUserService;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.entity.SysRole;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.model.params.AdminAllocRoleParam;
import com.yifan.admin.api.model.params.UpdateSysUserInfoParam;
import com.yifan.admin.api.model.params.UserAddParam;
import com.yifan.admin.api.model.params.UserPwdResetParam;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.service.CacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 后台用户管理
 */
@RestController
@Api(tags = "后台用户管理")
@RequestMapping("/system/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private CacheService cacheService;


    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @Right(rightsOr = "sys:user:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<SysUser>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysUser> adminList = userService.list(keyword, pageSize, pageNum);
        adminList.getRecords().forEach(e -> e.setPassword("******"));
        return BaseResult.ok(CommonPage.restPage(adminList));
    }

    @ApiOperation(value = "用户-添加")
    @Right(rightsOr = "sys:user:add")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<SysUser> add(@Validated @RequestBody UserAddParam umsAdminParam) {
        boolean result = userService.add(umsAdminParam);
        return BaseResult.ok();
    }

    @ApiOperation("获取指定用户信息")
    @Right(rightsOr = "sys:user:list")
    @RequestMapping(value = "info/{adminId}", method = RequestMethod.GET)
    public BaseResult<SysUser> getItem(@PathVariable Long adminId) {
        SysUser admin = userService.getById(adminId);
        return BaseResult.ok(admin);
    }

    @ApiOperation("修改指定用户信息")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @Right(rightsOr = "sys:user:update")
    @RequestMapping(value = "/update/{adminId}", method = RequestMethod.POST)
    public BaseResult<Void> update(@PathVariable Long adminId, @RequestBody UpdateSysUserInfoParam param) {
        boolean success = userService.updateByParam(adminId, param);
        return BaseResult.ok();
    }


    @ApiOperation("删除指定用户信息")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.DELETE)
    @Right(rightsOr = "sys:user:delete")
    @RequestMapping(value = "/remove/{adminId}", method = RequestMethod.DELETE)
    public BaseResult<Void> delete(@PathVariable Long adminId) {
        boolean removeUserResult = userService.delete(adminId);
        return removeUserResult ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("帐号禁用 0:禁用")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @Right(rightsOr = "sys:user:disable")
    @RequestMapping(value = "/status/disable/{adminId}", method = RequestMethod.PUT)
    public BaseResult<Void> adminStatusDisable(@PathVariable Long adminId) {
        SysUser umsAdmin = new SysUser();
        umsAdmin.setStatus(StatusEnum.DISABLE.getStatus());
        boolean success = userService.update(adminId, umsAdmin);
        return success ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("帐号启用 1：启用")
    @Right(rightsOr = "sys:user:enable")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/status/enable/{adminId}", method = RequestMethod.PUT)
    public BaseResult<Void> adminStatusable(@PathVariable Long adminId) {
        SysUser umsAdmin = new SysUser();
        umsAdmin.setStatus(StatusEnum.USABLE.getStatus());
        boolean success = userService.update(adminId, umsAdmin);
        return success ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("重置用户密码")
    @Right(rightsOr = "sys:user:pwdreset")
    @Log(title = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/pwd/reset/{adminId}", method = RequestMethod.POST)
    public BaseResult<Void> adminPwdReset(@PathVariable Long adminId, @RequestBody UserPwdResetParam pwdResetParam) {
        boolean resetResult = userService.adminPwdReset(adminId, pwdResetParam);
        return resetResult ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("给用户分配角色")
    @Right(rightsAnd = {"sys:user:update", "sys:role:list"})
    @Log(title = "用户管理", businessType = BusinessTypeEnum.GRANT)
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public BaseResult<Integer> updateAdminRole(@RequestBody @Validated AdminAllocRoleParam param) {
        List<Long> roleList = Collections.emptyList();
        if (StringUtils.isNotEmpty(param.getRoleIds())){
            roleList = Stream.of(param.getRoleIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        int count = userService.updateAdminRole(param.getUserId(), roleList);
        return BaseResult.ok(count);
    }

    @ApiOperation("获取指定用户的角色")
    @Right(rightsOr = {"sys:user:list", "sys:role:list"})
    @RequestMapping(value = "/role/{adminId}", method = RequestMethod.GET)
    public BaseResult<List<Long>> getRoleList(@PathVariable Long adminId) {
        List<Long> roleList = roleService.getRoleList(adminId).stream().map(SysRole::getId).collect(Collectors.toList());
        return BaseResult.ok(roleList);
    }
}
