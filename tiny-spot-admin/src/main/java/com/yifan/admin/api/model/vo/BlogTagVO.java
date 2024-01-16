package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.entity.BlogTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/30 15:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "博客标签-VO")
public class BlogTagVO extends BlogTag {

    @ApiModelProperty(value = "标签下文章数量")
    private long articleNum = 0L;

    public BlogTagVO() {
    }

    public BlogTagVO(BlogTag tag) {
        if (tag != null){
            BeanUtils.copyProperties(tag, this);
        }
    }
}
