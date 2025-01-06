package com.yifan.admin.api.model.vo.open;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("open博客标签-VO")
public class OpenBlogTagVO {

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "标签标题")
    private String title;

}
