package com.yifan.admin.api.model.vo.open;

import com.yifan.admin.api.entity.BlogTag;
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

    public OpenBlogTagVO() {

    }

    public OpenBlogTagVO(BlogTag blogTag) {
        if (blogTag != null) {
            this.tagId = blogTag.getId();
            this.title = blogTag.getTitle();
        }
    }
}
