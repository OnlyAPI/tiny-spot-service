package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.SysUserRole;
import org.springframework.stereotype.Repository;

/**
 * 后台用户和角色关系表 Mapper 接口
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
