package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.model.node.RoleMenuTreeNode;
import com.yifan.admin.api.model.node.SysMenuNode;
import com.yifan.admin.api.model.params.MenuAddOrUpdateParam;
import com.yifan.admin.api.model.params.MenuTreeListParams;
import com.yifan.admin.api.model.vo.SysMenuVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 后台菜单管理Controller
 */
@RestController
@Api(tags = "后台菜单管理")
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService menuService;

    @ApiOperation("添加后台菜单")
    @Right(rightsOr = "sys:menu:add")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.INSERT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<Void> create(@RequestBody @Valid MenuAddOrUpdateParam param) {
        boolean success = menuService.create(param);
        if (success) {
            return BaseResult.ok();
        } else {
            return BaseResult.failed();
        }
    }

    @ApiOperation("修改后台菜单")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.UPDATE)
    @Right(rightsOr = "sys:menu:update")
    @RequestMapping(value = "/update/{menuId}", method = RequestMethod.POST)
    public BaseResult<Void> update(@PathVariable Long menuId,
                                   @RequestBody MenuAddOrUpdateParam param) {
        boolean success = menuService.update(menuId, param);
        if (success) {
            return BaseResult.ok();
        } else {
            return BaseResult.failed();
        }
    }

    @ApiOperation("删除后台菜单")
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.DELETE)
    @Right(rightsOr = "sys:menu:delete")
    @RequestMapping(value = "/remove/{menuId}", method = RequestMethod.DELETE)
    public BaseResult<Void> delete(@PathVariable Long menuId) {
        boolean success = menuService.removeByMenuId(menuId);
        return BaseResult.ok();
    }

    @ApiOperation("根据ID获取菜单详情")
    @Right(rightsOr = "sys:menu:list")
    @RequestMapping(value = "info/{menuId}", method = RequestMethod.GET)
    public BaseResult<SysMenuVO> getItem(@PathVariable Long menuId) {
        SysMenu umsMenu = menuService.getById(menuId);
        SysMenuVO menuVO = new SysMenuVO(umsMenu);
        return BaseResult.ok(menuVO);
    }

    @ApiOperation("分页查询后台菜单")
    @Right(rightsOr = "sys:menu:list")
    @RequestMapping(value = "/list/page", method = RequestMethod.GET)
    public BaseResult<CommonPage<SysMenu>> list(@RequestParam(value = "parentId", defaultValue = "0") Long parentId,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<SysMenu> menuList = menuService.list(parentId, pageSize, pageNum);
        return BaseResult.ok(CommonPage.restPage(menuList));
    }

    @ApiOperation("角色菜单树")
    @Right(rightsOr = "sys:menu:list")
    @RequestMapping(value = "/roleMenuTree", method = RequestMethod.GET)
    public BaseResult<List<RoleMenuTreeNode>> getRoleMenuTreeNode() {
        List<SysMenu> list = menuService.getMenuNodeTree(RequestContext.getUser().getId());
        List<RoleMenuTreeNode> sysMenuNodes = menuService.roleMenuTreeList(list);
        return BaseResult.ok(sysMenuNodes);
    }

    @ApiOperation("菜单树")
    @Right(rightsOr = "sys:menu:list")
    @RequestMapping(value = "/menuNodeTree", method = RequestMethod.GET)
    public BaseResult<List<SysMenuNode>> getMenuNodeTree(MenuTreeListParams param) {
        List<SysMenu> list = menuService.getMenuNodeTree(RequestContext.getUser().getId(), param);
        List<SysMenuNode> treeNodes = menuService.treeList(list);
        return BaseResult.ok(treeNodes);
    }

    @ApiOperation("修改菜单状态-1:启用 0：禁用")
    @Right(rightsAnd = {"sys:menu:enable", "sys:menu:disable"})
    @Log(title = "菜单管理", businessType = BusinessTypeEnum.UPDATE)
    @RequestMapping(value = "/updateStatus/{menuId}", method = RequestMethod.GET)
    public BaseResult<Void> updateStatus(@PathVariable(value = "menuId") Long menuId, @RequestParam("status") Integer status) {
        boolean success = menuService.updateStatus(menuId, status);
        return success ? BaseResult.ok() : BaseResult.failed();
    }
}
