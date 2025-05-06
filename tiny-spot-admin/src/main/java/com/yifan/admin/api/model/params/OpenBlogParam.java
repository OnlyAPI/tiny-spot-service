package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenBlogParam extends BasePageParam{

    @ApiModelProperty(value = "关键字")
    private String keyword;


    @ApiModelProperty(value = "标签列表字符串")
    private String tagIdStr;
}
