package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 后台用户角色表 Mapper 接口
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     *
     * 获取用户所有角色
     */
    @Select("select r.* from ts_sys_user_role ar left join ts_sys_role r on ar.role_id = r.id where ar.admin_id = #{adminId} and r.status = 1 ")
    List<SysRole> getRoleList(@Param("adminId") Long adminId);

}
