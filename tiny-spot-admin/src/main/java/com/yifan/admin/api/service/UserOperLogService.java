package com.yifan.admin.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yifan.admin.api.model.params.MonitorOperLogParam;
import com.yifan.admin.api.entity.UserOperLog;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 17:28
 */
public interface UserOperLogService extends IService<UserOperLog> {

    Page<UserOperLog> list(MonitorOperLogParam param);

}
