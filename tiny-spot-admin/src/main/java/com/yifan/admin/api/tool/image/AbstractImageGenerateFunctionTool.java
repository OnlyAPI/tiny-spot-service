package com.yifan.admin.api.tool.image;

import com.yifan.admin.api.tool.FunctionDefinition;
import com.yifan.admin.api.tool.FunctionParameters;
import com.yifan.admin.api.tool.FunctionProperties;
import com.yifan.admin.api.tool.FunctionTool;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/4 16:15
 */
public abstract class AbstractImageGenerateFunctionTool implements FunctionTool {

    public FunctionDefinition getFunctionDefinition() {
        FunctionDefinition definition = new FunctionDefinition();
        definition.setName(getName());
        definition.setDescription("提供文本生成图像功能，你可以提供提示内容和要生成的图像数量，来生成图像");

        HashMap<String, FunctionProperties> map = new HashMap<>();

        FunctionProperties functionProperties = new FunctionProperties();
        functionProperties.setType("string");
        functionProperties.setDescription("提示词");
        map.put("prompt", functionProperties);

        FunctionProperties functionProperties2 = new FunctionProperties();
        functionProperties2.setType("number");
        functionProperties2.setDescription("生成图片的数量");
        map.put("n", functionProperties2);

        FunctionParameters functionParameters = new FunctionParameters();
        functionParameters.setType("object");
        functionParameters.setRequired(Arrays.asList("prompt", "n"));
        functionParameters.setProperties(map);

        definition.setParameters(functionParameters);

        return definition;
    }

}
