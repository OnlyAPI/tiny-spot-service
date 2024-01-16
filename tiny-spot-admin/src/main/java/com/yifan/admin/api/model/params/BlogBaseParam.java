package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/6/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlogBaseParam extends BasePageParam{
    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "升序或降序")
    private String sortOrder = "descending";

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "分类ID")
    private Long classifyId;
}
