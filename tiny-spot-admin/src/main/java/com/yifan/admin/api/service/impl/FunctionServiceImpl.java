package com.yifan.admin.api.service.impl;

import com.yifan.admin.api.service.FunctionService;
import com.yifan.admin.api.tool.FunctionDefinition;
import com.yifan.admin.api.tool.FunctionOutput;
import com.yifan.admin.api.tool.FunctionTool;
import com.yifan.admin.api.tool.FunctionToolFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/4 16:03
 */
@Slf4j
@Service
public class FunctionServiceImpl implements FunctionService {

    @Resource
    private FunctionToolFactory functionToolFactory;

    @Override
    public List<FunctionDefinition> getFunctionDefinitions() {
        return functionToolFactory.getFunctionDefinitions();
    }

    @Override
    public boolean isSupport(String name) {
        return functionToolFactory.getFunctionTool(name) != null;
    }

    @Override
    public FunctionOutput call(String name, String params) {
        FunctionTool functionTool = functionToolFactory.getFunctionTool(name);
        if (functionTool != null) {
            try {
                return functionTool.apply(params);
            } catch (Exception e) {
                log.error("function call error,  name: {}, params: {}", name, params, e);
                return null;
            }
        }
        return null;
    }
}
