package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysRole;
import com.yifan.admin.api.entity.SysRoleMenu;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.mapper.SysMenuMapper;
import com.yifan.admin.api.mapper.SysRoleMapper;
import com.yifan.admin.api.model.params.RoleAddOrUpdateParam;
import com.yifan.admin.api.service.SysRoleMenuService;
import com.yifan.admin.api.service.SysRoleService;
import com.yifan.admin.api.utils.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台角色管理Service实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMenuService roleMenuService;
    @Autowired
    private SysMenuMapper menuMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    @Transactional
    public boolean create(RoleAddOrUpdateParam role) {
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(role.getName())) {
            throw new ApiException("权限字符不正确");
        }
        LambdaQueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>()
                .lambda()
                .eq(SysRole::getName, role.getName());
        Long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new ApiException("权限字符已存在");
        }
        SysRole sysRole = new SysRole();
        sysRole.setCreateTime(DateTimeUtil.currentDate());
        sysRole.setUserCount(0);
        BeanUtils.copyProperties(role, sysRole);
        boolean save = save(sysRole);

        //todo 插入菜单权限
        if (!role.getMenuIds().isEmpty()) {
            ArrayList<SysRoleMenu> list = new ArrayList<>();
            role.getMenuIds().forEach(r -> {
                list.add(new SysRoleMenu(sysRole.getId(), r));
            });
            roleMenuService.saveBatch(list);
        }
        return save;
    }

    @Override
    public boolean delete(List<Long> ids) {
        LambdaQueryWrapper<SysRole> lambda = new QueryWrapper<SysRole>()
                .lambda()
                .eq(SysRole::getName, RoleEnum.ADMIN.toString())
                .eq(SysRole::getStatus, StatusEnum.USABLE.getStatus());
        SysRole adminRole = sysRoleMapper.selectOne(lambda);

        List<Long> roles = ids.stream().filter(r -> !r.equals(adminRole.getId())).collect(Collectors.toList());
        return removeByIds(roles);
    }

    @Override
    public Page<SysRole> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysRole> lambda = wrapper.lambda().orderByAsc(SysRole::getSort);
        if (StringUtils.isNotEmpty(keyword)) {
            lambda.like(SysRole::getDescription, keyword)
                    .or()
                    .like(SysRole::getName, keyword);
        }
        return page(page, wrapper);
    }


    @Override
    public List<Long> roleMenuIdList(Long roleId) {
        List<SysMenu> menuListByRoleId = menuMapper.getMenuListByRoleId(roleId);
        if (menuListByRoleId.isEmpty()) {
            return Collections.emptyList();
        }
        return menuListByRoleId.stream().map(SysMenu::getId).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysRoleMenu::getRoleId, roleId);
        roleMenuService.remove(wrapper);
        //批量插入新关系
        List<SysRoleMenu> relationList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu relation = new SysRoleMenu();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            relationList.add(relation);
        }
        roleMenuService.saveBatch(relationList);
        return menuIds.size();
    }

    @Override
    @Transactional
    public boolean updateRoleAndMenu(Long roleId, RoleAddOrUpdateParam role) {
        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        BeanUtils.copyProperties(role, sysRole);
        int updateRoleInfo = baseMapper.updateById(sysRole);

        //todo 修改角色-菜单关联表
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<SysRoleMenu>().lambda()
                .eq(SysRoleMenu::getRoleId, roleId);
        boolean remove = roleMenuService.remove(queryWrapper);

        ArrayList<SysRoleMenu> sysRoleMenuArrayList = new ArrayList<>();
        role.getMenuIds().forEach(m -> {
            sysRoleMenuArrayList.add(new SysRoleMenu(roleId, m));
        });
        boolean saveBatch = roleMenuService.saveBatch(sysRoleMenuArrayList);
        return true;
    }

    @Override
    public List<SysRole> getRoleList(Long adminId) {
        return sysRoleMapper.getRoleList(adminId);
    }

}
