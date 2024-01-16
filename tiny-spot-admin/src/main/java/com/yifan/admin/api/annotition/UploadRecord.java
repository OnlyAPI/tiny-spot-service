package com.yifan.admin.api.annotition;

import java.lang.annotation.*;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/14 10:24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadRecord {

}
