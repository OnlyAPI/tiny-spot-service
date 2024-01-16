package com.yifan.admin.api.annotition;


import java.lang.annotation.*;

/**
 * 权限注解
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Right {

    //多条件同时成立
    String [] rightsAnd() default {};

    //多条件任一成立
    String [] rightsOr() default {};

}
