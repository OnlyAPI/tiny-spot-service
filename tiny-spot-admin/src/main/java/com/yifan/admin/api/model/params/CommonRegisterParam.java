package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/4/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "系统用户注册请求参数")
public class CommonRegisterParam extends CommonLoginParam {
}
