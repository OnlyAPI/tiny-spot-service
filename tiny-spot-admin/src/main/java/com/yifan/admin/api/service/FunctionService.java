package com.yifan.admin.api.service;

import com.yifan.admin.api.tool.FunctionDefinition;
import com.yifan.admin.api.tool.FunctionOutput;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/3 18:30
 */
public interface FunctionService {

    List<FunctionDefinition> getFunctionDefinitions();

    boolean isSupport(String name);

    FunctionOutput call(String name, String params);
}
