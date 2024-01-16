package com.yifan.admin.api.annotition;

import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.enums.OperatorTypeEnum;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * 
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 模块 
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 操作人类别
     */
    public OperatorTypeEnum operatorType() default OperatorTypeEnum.MANAGE;

}
