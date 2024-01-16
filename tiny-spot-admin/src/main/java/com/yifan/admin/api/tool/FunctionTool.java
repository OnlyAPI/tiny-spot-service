package com.yifan.admin.api.tool;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2024/1/3
 */
public interface FunctionTool {
    String getName();

    FunctionDefinition getFunctionDefinition();

    FunctionOutput apply(String param);

}
