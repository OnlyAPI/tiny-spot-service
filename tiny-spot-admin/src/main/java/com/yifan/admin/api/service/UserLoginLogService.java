package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.model.params.MonitorLoginLogParam;
import com.yifan.admin.api.entity.UserLoginLog;

/**
 * 用户登录日志
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 14:15
 */
public interface UserLoginLogService extends IService<UserLoginLog> {

    void insertAdminLoginLog(Long adminId);

    Page<UserLoginLog> list(MonitorLoginLogParam param);

}
