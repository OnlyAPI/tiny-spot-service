package com.yifan.admin.api.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.model.node.RoleMenuTreeNode;
import com.yifan.admin.api.model.node.SysMenuNode;
import com.yifan.admin.api.model.params.MenuAddOrUpdateParam;
import com.yifan.admin.api.model.params.MenuTreeListParams;
import com.yifan.admin.api.entity.SysMenu;

import java.util.List;

/**
 * 后台菜单管理Service
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 创建后台菜单
     */
    boolean create(MenuAddOrUpdateParam param);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, MenuAddOrUpdateParam sysMenu);

    /**
     * 分页查询后台菜单
     */
    Page<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<SysMenuNode> treeList();

    List<SysMenuNode> treeList(Long adminId);

    List<SysMenuNode> treeList(List<SysMenu> menuList);

    List<RoleMenuTreeNode> roleMenuTreeList(List<SysMenu> menuList);


    /**
     * 修改菜单状态 0：禁用 1：启用
     * @param menuId
     * @param status
     * @return
     */
    boolean updateStatus(Long menuId, Integer status);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<SysMenu> getMenuNodeTree(Long adminId);

    List<SysMenu> getMenuNodeTree(Long userId, MenuTreeListParams param);

    boolean removeByMenuId(Long menuId);
}
