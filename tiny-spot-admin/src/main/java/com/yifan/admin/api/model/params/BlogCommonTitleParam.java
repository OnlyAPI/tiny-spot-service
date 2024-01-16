package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/30 16:12
 */
@ApiModel(value = "添加或修改标签/分类请求参数")
@Data
public class BlogCommonTitleParam {

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "内容不能为空")
    private String title;

    @ApiModelProperty(value = "状态 0：禁用 1：可用")
    @NotNull(message = "状态不能为空")
    private Integer status;

}
