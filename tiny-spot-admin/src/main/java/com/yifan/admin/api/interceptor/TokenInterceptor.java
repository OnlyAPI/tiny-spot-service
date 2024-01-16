package com.yifan.admin.api.interceptor;

import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.constant.SysConstant;
import com.yifan.admin.api.entity.SysUser;
import com.yifan.admin.api.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private CacheService sysCacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(SysConstant.HEAD_TOKEN_KEY);
        log.info("[TokenInterceptor] URL :{}; HTTP_METHOD :{}; IP :{}; TOKEN :{}", request.getRequestURL().toString(),
                request.getMethod(), request.getRemoteAddr(), token);

        if (StringUtils.isBlank(token)) {
            throw ApiException.newCheckFaidException("token不能为空");
        }
        if (!sysCacheService.checkToken(token)) {
            throw ApiException.newCheckFaidException();
        }

        SysUser sysAdmin = sysCacheService.getUser(token);

        RequestContext.set(token, sysAdmin);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        RequestContext.clear();
    }
}
