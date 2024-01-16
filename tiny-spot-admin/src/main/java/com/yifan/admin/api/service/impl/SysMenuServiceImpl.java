package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.SysMenuMapper;
import com.yifan.admin.api.service.SysMenuService;
import com.yifan.admin.api.service.SysRoleMenuService;
import com.yifan.admin.api.service.SysUserService;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.model.node.RoleMenuTreeNode;
import com.yifan.admin.api.model.node.SysMenuNode;
import com.yifan.admin.api.model.params.MenuAddOrUpdateParam;
import com.yifan.admin.api.model.params.MenuTreeListParams;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.entity.SysRoleMenu;
import com.yifan.admin.api.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台菜单管理Service实现类
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

//    @Autowired
//    private SysMenuMapper sysMenuMapper;
    @Autowired
    private SysUserService sysAdminService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @Override
    public boolean create(MenuAddOrUpdateParam param) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(param, sysMenu);
        sysMenu.setCreateTime(new Date());
        return save(sysMenu);
    }

    @Override
    public boolean update(Long id, MenuAddOrUpdateParam sysMenu) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(sysMenu, menu);
        menu.setId(id);
        return updateById(menu);
    }

    @Override
    public Page<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<SysMenu> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysMenu::getParentId, parentId)
                .orderByDesc(SysMenu::getSort);
        return page(page, wrapper);
    }

    @Override
    public List<SysMenuNode> treeList() {
        List<SysMenu> menuList = list();
        return treeList(menuList);
    }

    @Override
    public List<SysMenuNode> treeList(Long adminId) {
        List<SysMenu> menuList = getMenuNodeTree(adminId);
        if (menuList.isEmpty()) {
            log.info("[treeList] menuList is empty. adminId: {}.", adminId);
            return Collections.emptyList();
        }
        return treeList(menuList);
    }

    /**
     * 转树形结构
     *
     * @param menuList
     * @return
     */
    @Override
    public List<SysMenuNode> treeList(List<SysMenu> menuList) {
        if (menuList.isEmpty()) {
            return Collections.emptyList();
        }
        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
    }

    @Override
    public List<RoleMenuTreeNode> roleMenuTreeList(List<SysMenu> menuList) {
        if (menuList.isEmpty()) {
            return Collections.emptyList();
        }
        return menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertRoleMenuNode(menu, menuList)).collect(Collectors.toList());
    }

    @Override
    public boolean updateStatus(Long menuId, Integer status) {
        SysMenu umsMenu = new SysMenu();
        umsMenu.setId(menuId);
        umsMenu.setStatus(status);
        return updateById(umsMenu);
    }


    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private SysMenuNode covertMenuNode(SysMenu menu, List<SysMenu> menuList) {
        SysMenuNode node = new SysMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<SysMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

    private RoleMenuTreeNode covertRoleMenuNode(SysMenu menu, List<SysMenu> menuList){
        RoleMenuTreeNode node = new RoleMenuTreeNode();
        node.setId(menu.getId());
        node.setLabel(menu.getTitle());
        List<RoleMenuTreeNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertRoleMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }


    @Override
    public List<SysMenu> getMenuNodeTree(Long adminId) {
        SysUser admin = sysAdminService.getById(adminId);
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())){
            LambdaQueryWrapper<SysMenu> queryWrapper = new QueryWrapper<SysMenu>().lambda().orderByAsc(SysMenu::getSort);
            return baseMapper.selectList(queryWrapper);
        }else {
           return baseMapper.getMenuList(adminId);
        }
    }

    @Override
    public List<SysMenu> getMenuNodeTree(Long adminId, MenuTreeListParams param) {
        SysUser admin = sysAdminService.getById(adminId);
        LambdaQueryWrapper<SysMenu> lambda = new QueryWrapper<SysMenu>().lambda();
        if (StringUtils.isNotBlank(param.getTitle())){
            lambda.like(SysMenu::getTitle, param.getTitle());
        }
        lambda.orderByAsc(SysMenu::getSort);
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())){
            return baseMapper.selectList(lambda);
        }else {
           return baseMapper.getMenuListByUserIdAndTitle(adminId, param.getTitle());
        }
    }

    @Override
    @Transactional
    public boolean removeByMenuId(Long menuId){
        int deleteById = baseMapper.deleteById(menuId);

        LambdaQueryWrapper<SysRoleMenu> wrapper = new QueryWrapper<SysRoleMenu>().lambda();
        wrapper.eq(SysRoleMenu::getMenuId, menuId);
        boolean remove = roleMenuService.remove(wrapper);

        return (deleteById > 0 && remove);
    }
}
