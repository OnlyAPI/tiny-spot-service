package com.yifan.admin.api.tool;

import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/3 18:14
 */
@Data
public class FunctionDefinition {

    private String name;

    private String description;

    private FunctionParameters parameters;

}
