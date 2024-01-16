package com.yifan.admin.api.tool;

import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2024/1/3
 */
public interface FunctionToolFactory {

    List<FunctionDefinition> getFunctionDefinitions();

    FunctionTool getFunctionTool(String name);

}
