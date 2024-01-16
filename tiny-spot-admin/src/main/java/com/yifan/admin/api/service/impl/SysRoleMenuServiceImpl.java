package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.SysRoleMenuMapper;
import com.yifan.admin.api.service.SysRoleMenuService;
import com.yifan.admin.api.entity.SysRoleMenu;
import org.springframework.stereotype.Service;

/**
 * 角色菜单关系管理Service实现类
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
}
