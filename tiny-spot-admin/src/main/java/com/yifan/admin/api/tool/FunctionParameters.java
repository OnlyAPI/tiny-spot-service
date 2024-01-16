package com.yifan.admin.api.tool;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author TaiYi
 * @ClassName
 * @date 2024/1/3 18:16
 */
@Data
public class FunctionParameters {

    private String type;

    private Map<String, FunctionProperties> properties;

    private List<String> required;

}
