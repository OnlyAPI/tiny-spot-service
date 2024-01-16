package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.mapper.SysUserMapper;
import com.yifan.admin.api.mapper.SysRoleMapper;
import com.yifan.admin.api.service.SysUserRoleService;
import com.yifan.admin.api.service.SysUserService;
import com.yifan.admin.api.enums.UserResourceStrategyEnum;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.model.params.UpdateSysUserInfoParam;
import com.yifan.admin.api.enums.StatusEnum;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.model.params.UserAddParam;
import com.yifan.admin.api.model.params.UserPwdResetParam;
import com.yifan.admin.api.model.params.UpdateUserPwdParam;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.entity.SysUserRole;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.service.pwdEncoder.PasswordEncoder;
import com.yifan.admin.api.result.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 后台管理员管理Service实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserRoleService adminRoleRelationService;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CacheService cacheService;


    @Override
    public SysUser login(String username, String password) {
        final String encodedPwd = passwordEncoder.encode(password, username);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .eq(SysUser::getPassword, encodedPwd);
        List<SysUser> admins = sysUserMapper.selectList(queryWrapper);
        if (admins.isEmpty()) {
            logger.info("[login] query user is empty. username: {}, password: {}.", username, encodedPwd);
            return null;
        }
        SysUser sysAdmin = admins.get(0);
        if (sysAdmin.getStatus().equals(StatusEnum.DISABLE.getStatus())) {
            logger.info("[login] user disable, username: {}.", username);
            throw new ApiException(ResultCode.USER_DISABLE);
        }
        return sysAdmin;
    }

    @Override
    public BaseResult register(String username, String password) {
        add(new UserAddParam(username, password));
        return BaseResult.ok();
    }


//    @Override
//    public SysAdmin getAdminByUsername(String username) {
//        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(SysAdmin::getUsername, username);
//        List<SysAdmin> adminList = list(wrapper);
//        if (adminList != null && adminList.size() > 0) {
//            return adminList.get(0);
//        }
//        return null;
//    }

    @Override
    @Transactional
    public boolean add(UserAddParam umsAdminParam) {

        //查询是否有相同用户名的用户
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUsername, umsAdminParam.getUsername());
        List<SysUser> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            logger.error("[addUser] username exist. username: {}.", umsAdminParam.getUsername());
            throw new ApiException("用户已存在");
        }

        SysUser umsAdmin = new SysUser();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setUserType(RoleEnum.USER.toString());
        umsAdmin.setUserResource(UserResourceStrategyEnum.SYSTEM.getResourceType());
        umsAdmin.setCreateTime(new Date());
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword(), umsAdmin.getUsername());
        umsAdmin.setPassword(encodePassword);
        int addUser = baseMapper.insert(umsAdmin);
        int addRole = updateAdminRole(umsAdmin.getId(), umsAdminParam.getRoleIds());
        return addUser > 0 && addRole > 0;
    }


    /**
     * 根据用户名修改登录时间
     */
//    private void updateLoginTimeByUsername(String username) {
//        SysAdmin record = new SysAdmin();
//        record.setLoginTime(new Date());
//        QueryWrapper<SysAdmin> wrapper = new QueryWrapper<>();
//        wrapper.lambda().eq(SysAdmin::getUsername, username);
//        update(record, wrapper);
//    }
    @Override
    public Page<SysUser> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysUser> lambda = wrapper.lambda();
        if (StringUtils.isNotEmpty(keyword)) {
            lambda.like(SysUser::getUsername, keyword);
            lambda.or().like(SysUser::getNickName, keyword);
        }
        return page(page, wrapper);
    }

    @Override
    public boolean update(Long id, SysUser admin) {
        admin.setId(id);
        SysUser rawAdmin = getById(id);
        if (admin == null) {
            logger.error("[update] query SysAdmin is null by adminId. adminId: {}.", id);
            throw new ApiException("未找到对应用户");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(rawAdmin.getUserType())) {
            logger.error("[SysAdminService - update] not update super admin info.");
            throw new ApiException("不能修改超级管理员信息");
        }
        if (rawAdmin.getPassword().equals(admin.getPassword())) {
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        } else {
            //与原加密密码不同的需要加密修改
            if (StringUtils.isEmpty(admin.getPassword())) {
                admin.setPassword(null);
            } else {
                admin.setPassword(passwordEncoder.encode(admin.getPassword(), admin.getUsername()));
            }
        }
        boolean success = updateById(admin);
        cacheService.delMenusList(id);
        cacheService.delPermissionSet(id);
        return success;
    }

    @Override
    @Transactional
    public boolean delete(Long userId) {
        SysUser admin = getById(userId);
        if (admin == null) {
            logger.error("[delete] query SysAdmin is null by adminId. adminId: {}.", userId);
            throw new ApiException("未找到对应用户");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())) {
            logger.error("[SysAdminService - delete] not del super admin.");
            throw new ApiException("不能删除超级管理员");
        }

        boolean removeUserResult = removeById(userId);

        //todo 删除角色对应关系
        LambdaQueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<SysUserRole>()
                .lambda()
                .eq(SysUserRole::getAdminId, userId);
        boolean removeUserRoleResult = adminRoleRelationService.remove(queryWrapper);
        logger.info("[delete] remove user-role, adminId: {}, result: {}.", userId, removeUserRoleResult);

        logger.info("[delete] remove SysAdmin result: {}, adminId: {}.", removeUserResult, admin);
//        cacheService.delMenusList(userId);
//        cacheService.delPermissionSet(userId);
        return removeUserResult && removeUserRoleResult;
    }

    @Override
    @Transactional
    public int updateAdminRole(Long adminId, List<Long> roleIds) {
        SysUser admin = getById(adminId);
        if (admin == null) {
            logger.error("[updateRole] query SysAdmin is null by adminId. adminId: {}.", adminId);
            throw new ApiException("未找到对应用户");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())) {
            logger.error("[SysAdminService - updateRole] not update super admin role.");
            throw new ApiException("不能修改超级管理员角色");
        }
        //List<Long> collect = roleIds.stream().filter(r -> !r.equals(SysConstant.USER_SUPER_ADMIN)).collect(Collectors.toList());

        //先删除原来的关系
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUserRole::getAdminId, adminId);
        adminRoleRelationService.remove(wrapper);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysUserRole> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole roleRelation = new SysUserRole();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
        //cacheService.delMenusList(adminId);
        //cacheService.delPermissionSet(adminId);
        return roleIds.size();
    }


    @Override
    public boolean updatePassword(UpdateUserPwdParam param) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getId, RequestContext.getUser().getId());
        List<SysUser> adminList = list(wrapper);
        if (CollectionUtils.isEmpty(adminList)) {
            throw new ApiException("未找到用户");
        }
        SysUser umsAdmin = adminList.get(0);
        if (!passwordEncoder.matches(param.getOldPassword(), umsAdmin.getPassword(), umsAdmin.getUsername())) {
            throw new ApiException("旧密码错误");
        }
        if (param.getOldPassword().equals(param.getNewPassword())) {
            throw new ApiException("新密码和旧密码不能相同");
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword(), umsAdmin.getUsername()));
        return updateById(umsAdmin);
    }

    @Override
    public boolean adminPwdReset(Long adminId, UserPwdResetParam pwdResetParam) {
        SysUser admin = getById(adminId);
        if (admin == null) {
            logger.error("[adminPwdReset] query SysAdmin is null by adminId. adminId: {}.", adminId);
            throw new ApiException("未找到对应用户");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())) {
            logger.error("[adminPwdReset] not update super admin role.");
            throw new ApiException("不能重置超级管理员密码");
        }
        final String newEncodePwd = passwordEncoder.encode(pwdResetParam.getNewPwd(), admin.getUsername());
        if (Objects.equals(newEncodePwd, admin.getPassword())) {
            logger.error("[adminPwdReset] newPassword and oldPassword not different. adminId: {}.", adminId);
            throw new ApiException("不能与原密码相同");
        }
        admin.setPassword(newEncodePwd);
        boolean updateById = updateById(admin);
        logger.info("[adminPwdReset] reset password result: {}, adminId: {}, operator-adminId: {}.", updateById, admin, RequestContext.getUser().getId());
        return updateById;
    }

    @Override
    public boolean updateByParam(Long userId, UpdateSysUserInfoParam param) {
        if (!param.getRoleIds().isEmpty()) {
            updateAdminRole(userId, param.getRoleIds());
        }
        SysUser admin = getById(userId);
        if (admin == null) {
            logger.error("[updateByParam] query SysAdmin is null by userId. userId: {}.", userId);
            throw new ApiException("未找到对应用户");
        }
        if (RoleEnum.ADMIN.toString().equalsIgnoreCase(admin.getUserType())) {
            logger.error("[updateByParam] can not update super admin role.");
            throw new ApiException("不能重置超级管理员信息");
        }
        BeanUtils.copyProperties(param, admin);
        int update = baseMapper.updateById(admin);
        return update > 0;
    }

}
