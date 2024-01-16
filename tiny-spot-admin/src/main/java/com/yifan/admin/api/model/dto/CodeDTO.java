package com.yifan.admin.api.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/6/16 14:13
 */
@Data
@ApiModel(description = "Code信息")
public class CodeDTO {

    @NotBlank(message = "code不能为空")
    @ApiModelProperty(value = "code")
    private String code;

    @NotBlank(message = "authType不能为空")
    @ApiModelProperty(value = "authType")
    private String authType;

    @NotBlank(message = "state不能为空")
    @ApiModelProperty(value = "state")
    private String state;
}
