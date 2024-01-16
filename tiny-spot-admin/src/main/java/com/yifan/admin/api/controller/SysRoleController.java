package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysRole;
import com.yifan.admin.api.entity.SysRoleMenu;
import com.yifan.admin.api.entity.SysUserRole;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.model.node.RoleMenuTreeNode;
import com.yifan.admin.api.model.params.RoleAddOrUpdateParam;
import com.yifan.admin.api.model.params.RoleAllocMenuParam;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.service.SysMenuService;
import com.yifan.admin.api.service.SysRoleMenuService;
import com.yifan.admin.api.service.SysRoleService;
import com.yifan.admin.api.service.SysUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 后台用户角色管理
 */
@RestController
@Api(tags = "后台用户角色管理")
@RequestMapping("/system/role")
public class SysRoleController {
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysMenuService menuService;
    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysUserRoleService userRoleService;

    @ApiOperation("添加角色")
    @Right(rightsOr = "sys:role:add")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<Void> create(@RequestBody @Valid RoleAddOrUpdateParam role) {
        boolean success = roleService.create(role);
        return success ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("修改角色")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @Right(rightsOr = "sys:role:update")
    @RequestMapping(value = "/update/{roleId}", method = RequestMethod.POST)
    public BaseResult<Void> update(@PathVariable Long roleId, @RequestBody @Valid RoleAddOrUpdateParam role) {
        boolean result = roleService.updateRoleAndMenu(roleId, role);
        return result ? BaseResult.ok() : BaseResult.failed();
    }

    @ApiOperation("删除角色")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.DELETE)
    @Right(rightsOr = "sys:role:delete")
    @RequestMapping(value = "/remove/{roleId}", method = RequestMethod.DELETE)
    @Transactional
    public BaseResult<Void> delete(@PathVariable Long roleId) {
        SysRole sysRole = roleService.getById(roleId);
        if (sysRole == null) {
            return BaseResult.failed("service error");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(sysRole.getName())) {
            return BaseResult.failed("不能删除超级管理员角色");
        }
        LambdaQueryWrapper<SysUserRole> wrapper = new QueryWrapper<SysUserRole>().lambda()
                .eq(SysUserRole::getRoleId, roleId);
        List<SysUserRole> list = userRoleService.list(wrapper);
        if (list.size() > 0) {
            return BaseResult.failed("当前角色下有挂载用户，不可删除");
        }
        roleService.removeById(roleId);

        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<SysRoleMenu>().lambda()
                .eq(SysRoleMenu::getRoleId, roleId);
        roleMenuService.remove(queryWrapper);

        return BaseResult.ok();
    }

    @ApiOperation("获取指定角色信息")
    @Right(rightsOr = "sys:role:list")
    @GetMapping(value = "/info/{roleId}")
    public BaseResult<SysRole> getById(@PathVariable(value = "roleId") Long roleId) {
        SysRole sysRole = roleService.getById(roleId);
        if (sysRole == null) {
            return BaseResult.failed("角色信息不存在");
        }
        return BaseResult.ok(sysRole);
    }

//    @ApiOperation("批量删除角色")
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    public BaseResult delete(@RequestParam("ids") List<Long> ids) {
//        boolean success = roleService.delete(ids);
//        if (success) {
//            return BaseResult.success();
//        }
//        return BaseResult.failed();
//    }


    @ApiOperation("获取所有角色")
    @Right(rightsOr = "sys:role:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<SysRole>> listAll() {
        List<SysRole> roleList = roleService.list()
                .stream()
                .filter(r -> !RoleEnum.ADMIN.toString().equalsIgnoreCase(r.getName())).collect(Collectors.toList());
        return BaseResult.ok(roleList);
    }

    @ApiOperation("根据角色名称分页获取角色列表")
    @Right(rightsOr = "sys:role:list")
    @RequestMapping(value = "/list/page", method = RequestMethod.GET)
    public BaseResult<CommonPage<SysRole>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysRole> roleList = roleService.list(keyword, pageSize, pageNum);
        return BaseResult.ok(CommonPage.restPage(roleList));
    }

    @ApiOperation("修改角色状态 0：禁用 1：启用")
    @Right(rightsOr = "sys:role:update")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/updateStatus/{roleId}", method = RequestMethod.GET)
    public BaseResult<Void> updateStatus(@PathVariable Long roleId, @RequestParam(value = "status") Integer status) {
        SysRole sysRole = roleService.getById(roleId);
        if (sysRole == null) {
            return BaseResult.failed("service error");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(sysRole.getName())) {
            return BaseResult.failed("不能修改超级管理员角色状态");
        }

        SysRole umsRole = new SysRole();
        umsRole.setId(roleId);
        umsRole.setStatus(status);
        return roleService.updateById(umsRole) ? BaseResult.ok() : BaseResult.failed("update fail");
    }

    @ApiOperation("获取角色相关树菜单")
    @Right(rightsAnd = {"sys:role:list", "sys:menu:list"})
    @RequestMapping(value = "/roleMenuTreeSelect/{roleId}", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> listMenu(@PathVariable Long roleId) {
        List<Long> roleList = roleService.roleMenuIdList(roleId);
        List<SysMenu> menuByUserIdList = menuService.getMenuNodeTree(RequestContext.getUser().getId());
        List<RoleMenuTreeNode> roleMenuTreeNodes = menuService.roleMenuTreeList(menuByUserIdList);
        HashMap<String, Object> map = new HashMap<>();
        map.put("checkedKeys", roleList);
        map.put("menus", roleMenuTreeNodes);
        return BaseResult.ok(map);
    }

    @ApiOperation("给角色分配菜单")
    @Log(title = "角色管理", businessType = BusinessTypeEnum.GRANT)
    @Right(rightsAnd = {"sys:role:update", "sys:menu:list"})
    @RequestMapping(value = "/allocMenu", method = RequestMethod.POST)
    public BaseResult<Integer> allocMenu(@RequestBody @Validated RoleAllocMenuParam param) {
        int count = roleService.allocMenu(param.getRoleId(), param.getMenuList());
        return BaseResult.ok(count);
    }

}
