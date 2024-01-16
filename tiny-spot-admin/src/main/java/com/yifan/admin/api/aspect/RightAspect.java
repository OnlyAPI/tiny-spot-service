package com.yifan.admin.api.aspect;

import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.context.RequestContext;
import com.yifan.admin.api.entity.SysMenu;
import com.yifan.admin.api.enums.RoleEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.service.CacheService;
import com.yifan.admin.api.service.SysMenuService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/10 15:59
 */
@Aspect
@Component
public class RightAspect {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private CacheService cacheService;

    private static final Logger log = LoggerFactory.getLogger(RightAspect.class);


    @Pointcut(value = "@annotation(com.yifan.admin.api.annotition.Right)")
    public void pointCut() {

    }

    @Before("pointCut() && @annotation(right)")
    public void doBefore(JoinPoint joinPoint, Right right) {

        //系统超级管理员直接拥有全部权限，不需分配
        if (RequestContext.getUser().getUserType().equalsIgnoreCase(RoleEnum.ADMIN.toString())) {
            log.info("super user");
            return;
        }

        if (right.rightsAnd() != null && right.rightsAnd().length > 0) {
            checkPermission(true, right.rightsAnd());
        } else if (right.rightsOr() != null && right.rightsOr().length > 0) {
            checkPermission(false, right.rightsOr());
        }
    }

    private void checkPermission(boolean isAnd, String[] rights) {
        Set<String> permissionList = cacheService.getPermissionSet(RequestContext.getUser().getId());
        if (permissionList.isEmpty()) {
            List<SysMenu> menuList = sysMenuService.getMenuNodeTree(RequestContext.getUser().getId());
            if (menuList.isEmpty()) {
                throw new ApiException(ResultCode.FORBIDDEN);
            }
            permissionList = menuList.stream().map(SysMenu::getPermissionCode).collect(Collectors.toSet());
        }
        if (permissionList == null || permissionList.size() == 0) {
            throw new ApiException(ResultCode.FORBIDDEN);
        } else {
            if (isAnd) {
                //多个权限
                for (String right : rights) {
                    if (!permissionList.contains(right)) {
                        throw new ApiException(ResultCode.FORBIDDEN);
                    }
                }
                return;
            } else {
                for (String right : rights) {
                    if (permissionList.contains(right)) {
                        return;
                    }
                }
                throw new ApiException(ResultCode.FORBIDDEN);
            }
        }
    }
}
