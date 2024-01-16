package com.yifan.admin.api.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/4 16:06
 */
@Component
@Slf4j
public class DefaultFunctionToolFactory implements FunctionToolFactory {

    @Resource
    private List<FunctionTool> functionTools;


    @Override
    public List<FunctionDefinition> getFunctionDefinitions() {
        return functionDefinitions;
    }

    @Override
    public FunctionTool getFunctionTool(String name) {
        return functionToolMap.get(name);
    }

    @PostConstruct
    public void init() {
        if (functionTools != null) {
            functionDefinitions = functionTools.stream().map(FunctionTool::getFunctionDefinition).collect(Collectors.toList());
            functionToolMap = functionTools.stream().collect(Collectors.toMap(FunctionTool::getName, t -> t));
        } else {
            functionDefinitions = new ArrayList<>();
            functionToolMap = new HashMap<>();
        }
    }


    private static List<FunctionDefinition> functionDefinitions;

    private static Map<String, FunctionTool> functionToolMap;
}
