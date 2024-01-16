package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.UserOperLogMapper;
import com.yifan.admin.api.service.UserOperLogService;
import com.yifan.admin.api.model.params.MonitorOperLogParam;
import com.yifan.admin.api.entity.UserOperLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 17:28
 */
@Slf4j
@Service
public class UserOperLogServiceImpl extends ServiceImpl<UserOperLogMapper, UserOperLog> implements UserOperLogService {

    @Override
    public Page<UserOperLog> list(MonitorOperLogParam param) {
        Page<UserOperLog> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<UserOperLog> lambda = new QueryWrapper<UserOperLog>().lambda();
        if (param.getOperAdminId() != null) {
            lambda.eq(UserOperLog::getOperAdminId, param.getOperAdminId());
        }
        if (StringUtils.isNotBlank(param.getRequestUri())) {
            lambda.like(UserOperLog::getRequestUri, param.getRequestUri());
        }
        if (StringUtils.isNotBlank(param.getStatus())) {
            lambda.eq(UserOperLog::getStatus, param.getStatus());
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())) {
            lambda.between(UserOperLog::getCreateTime, param.getBeginTime(), param.getEndTime());
        }
        if (param.getSortOrder().contains("desc")) {
            lambda.orderByDesc(UserOperLog::getCreateTime);
        } else {
            lambda.orderByAsc(UserOperLog::getCreateTime);
        }
        return baseMapper.selectPage(page, lambda);
    }
}
