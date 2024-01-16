package com.yifan.admin.api.aspect;

import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.service.UserOperLogService;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.enums.BusinessStatusEnum;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.utils.JacksonJsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserOperLogService operLogService;

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            SysUser loginUser = RequestContext.getUser();

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();

            com.yifan.admin.api.entity.UserOperLog operLog = new com.yifan.admin.api.entity.UserOperLog();

            operLog.setCreateTime(new Date());
            //operLog.setStatus(BusinessStatusEnum.SUCCESS.name());

            // 请求的地址
            operLog.setRequestUri(request.getRequestURI());
            if (loginUser != null) {
                operLog.setOperAdminId(loginUser.getId());
            }

            if (e != null) {
                operLog.setStatus(BusinessStatusEnum.FAIL.name());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 200));
            }else {
                operLog.setStatus(BusinessStatusEnum.SUCCESS.name());
            }

            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");

            // 设置请求方式
            operLog.setRequestMethod(request.getMethod());

            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog, operLog, jsonResult);

            // 保存数据库
            operLogService.save(operLog);
        } catch (Exception exp) {
            log.error("[logAspect - handleLog] error msg: {}.", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(Log log, com.yifan.admin.api.entity.UserOperLog operLog, Object jsonResult) throws Exception {

        // 设置action动作
        operLog.setBusinessType(log.businessType().ordinal());

        // 设置标题
        operLog.setTitle(log.title());

        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());

        if (jsonResult != null) {
            BaseResult baseResult = JacksonJsonUtil.fromJson(JacksonJsonUtil.toJson(jsonResult), BaseResult.class);
            operLog.setRespCode(baseResult.getCode());
            operLog.setRespMsg(baseResult.getMsg());
            if (baseResult.getCode() != 2000){
                operLog.setStatus(BusinessStatusEnum.FAIL.name());
            }
        }

    }


}
