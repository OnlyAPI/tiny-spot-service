package com.yifan.admin.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yifan.admin.api.entity.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {


    @Select("SELECT distinct m.* FROM ts_sys_menu m " +
            "join ts_sys_role_menu rm on m.id = rm.menu_id " +
            "join ts_sys_role r on r.id = rm.role_id " +
            "join ts_sys_user_role ar on r.id = ar.role_id " +
            "where ar.admin_id = #{adminId} and r.status = 1 and m.status = 1 " +
            "order by m.sort asc ")
    List<SysMenu> getMenuList(@Param("adminId") Long adminId);


    @Select("<script>" +
            "SELECT distinct m.* " +
            "FROM ts_sys_menu m " +
            "join ts_sys_role_menu rm on m.id = rm.menu_id " +
            "join ts_sys_role r on r.id = rm.role_id " +
            "join ts_sys_user_role ar on r.id = ar.role_id " +
            "where ar.admin_id = #{adminId} and r.status = 1 " +
            "<if test=\"title != '' and title != null\"> " +
            "   and m.title like concat('%',#{title,jdbcType=VARCHAR},'%') " +
            "</if>" +
            "order by m.sort asc" +
            "</script>")
    List<SysMenu> getMenuListByUserIdAndTitle(@Param("adminId") Long adminId, @Param("title") String title);

    /**
     * 根据角色ID获取菜单
     */
    @Select("SELECT m.* from ts_sys_role r " +
            "join ts_sys_role_menu rm on r.id = rm.role_id " +
            "join ts_sys_menu m on rm.menu_id = m.id " +
            "where r.status = 1 and r.id = #{roleId}")
    List<SysMenu> getMenuListByRoleId(@Param("roleId") Long roleId);



    /**
     * 根据用户ID获取权限码列表
     * @param adminId
     * @return
     */
    @Select("SELECT distinct m.permission_code FROM ts_sys_user_role arr " +
            "LEFT JOIN ts_sys_role r ON arr.role_id = r.id " +
            "LEFT JOIN ts_sys_role_menu rmr ON r.id = rmr.role_id " +
            "LEFT JOIN ts_sys_menu m ON rmr.menu_id = m.id " +
            "WHERE " +
            "r.status = 1 and m.status = 1 " +
            "and " +
            "arr.admin_id = #{adminId}")
    List<String> getPermissionList(@Param("adminId") Long adminId);
}
