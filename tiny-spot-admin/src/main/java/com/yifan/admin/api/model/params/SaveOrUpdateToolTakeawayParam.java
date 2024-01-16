package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/6
 */
@Data
@ApiModel("添加或修改好评请求参数")
public class SaveOrUpdateToolTakeawayParam {

    @ApiModelProperty("内容")
    @NotBlank(message = "请输入内容")
    private String contentText;

}
