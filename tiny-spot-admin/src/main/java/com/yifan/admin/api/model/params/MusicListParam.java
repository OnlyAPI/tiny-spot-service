package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "音乐列表-请求参数")
public class MusicListParam extends BasePageParam{

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "排序")
    private String sortOrder = "descending";

}
