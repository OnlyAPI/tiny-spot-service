package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.model.params.UpdateSysUserInfoParam;
import com.yifan.admin.api.model.params.UserAddParam;
import com.yifan.admin.api.model.params.UserPwdResetParam;
import com.yifan.admin.api.model.params.UpdateUserPwdParam;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.result.BaseResult;

import java.util.List;

/**
 * 后台管理员管理Service
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    SysUser login(String username, String password);

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    BaseResult register(String username, String password);

    /**
     * 根据用户名获取后台管理员
     */
    //SysAdmin getAdminByUsername(String username);

    /**
     * 用户添加功能
     */
    boolean add(UserAddParam umsAdminParam);


    /**
     * 根据用户名或昵称分页查询用户
     */
    Page<SysUser> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    boolean update(Long id, SysUser sysAdmin);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色关系
     */

    int updateAdminRole(Long adminId, List<Long> roleIds);


    /**
     * 修改密码
     */
    boolean updatePassword(UpdateUserPwdParam updatePasswordParam);


    boolean adminPwdReset(Long adminId, UserPwdResetParam pwdResetParam);


    boolean updateByParam(Long userId, UpdateSysUserInfoParam param);
}
