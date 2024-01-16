package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.SysUserRoleMapper;
import com.yifan.admin.api.service.SysUserRoleService;
import com.yifan.admin.api.entity.SysUserRole;
import org.springframework.stereotype.Service;

/**
 * 管理员角色关系管理Service实现类
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
}
