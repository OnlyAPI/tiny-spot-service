package com.yifan.admin.api.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2024/1/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionOutput {
    private Boolean futureInvoke;
    private String result;

}
