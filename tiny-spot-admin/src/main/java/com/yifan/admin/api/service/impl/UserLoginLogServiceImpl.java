package com.yifan.admin.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yifan.admin.api.mapper.UserLoginLogMapper;
import com.yifan.admin.api.service.UserLoginLogService;
import com.yifan.admin.api.utils.IpUtil;
import com.yifan.admin.api.utils.ServletUtil;
import com.yifan.admin.api.entity.UserLoginLog;
import com.yifan.admin.api.model.params.MonitorLoginLogParam;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/2/16 14:15
 */
@Slf4j
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {


    @Override
    public void insertAdminLoginLog(Long adminId) {
        try {
            UserLoginLog loginLog = new UserLoginLog();
            loginLog.setAdminId(adminId);
            loginLog.setCreateTime(new Date());

            HttpServletRequest request = ServletUtil.getRequest();
            final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

            loginLog.setIp(IpUtil.getIpAddr(request));
            loginLog.setBrowser(userAgent.getBrowser().getName());
            loginLog.setOs(userAgent.getOperatingSystem().getName());
            save(loginLog);
        } catch (Exception e) {
            log.error("[insertAdminLoginLog] error.", e);
        }
    }

    @Override
    public Page<UserLoginLog> list(MonitorLoginLogParam param) {
        Page<UserLoginLog> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<UserLoginLog> queryWrapper = new QueryWrapper<UserLoginLog>().lambda();
        if (StringUtils.isNotBlank(param.getIp())) {
            queryWrapper.like(UserLoginLog::getIp, param.getIp());
        }
        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())) {
            queryWrapper.between(UserLoginLog::getCreateTime, param.getBeginTime(), param.getEndTime());
        }
        if(param.getAdminId() != null){
            queryWrapper.eq(UserLoginLog::getAdminId, param.getAdminId());
        }
        if (param.getSortOrder().contains("desc")) {
            queryWrapper.orderByDesc(UserLoginLog::getCreateTime);
        } else {
            queryWrapper.orderByAsc(UserLoginLog::getCreateTime);
        }

        return baseMapper.selectPage(page, queryWrapper);
        //return page(page);
    }
}
