package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.model.params.RoleAddOrUpdateParam;
import com.yifan.admin.api.entity.SysRole;

import java.util.List;

/**
 * 后台角色管理Service
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 添加角色
     */
    boolean create(RoleAddOrUpdateParam role);

    /**
     * 批量删除角色
     */
    boolean delete(List<Long> ids);

    /**
     * 分页获取角色列表
     */
    Page<SysRole> list(String keyword, Integer pageSize, Integer pageNum);


    /**
     * 获取用户对于角色
     */
    List<SysRole> getRoleList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<Long> roleMenuIdList(Long roleId);

    /**
     * 给角色分配菜单
     */

    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 修改角色及角色菜单信息
     * @param role
     * @return
     */
    boolean updateRoleAndMenu(Long roleId, RoleAddOrUpdateParam role);
}
