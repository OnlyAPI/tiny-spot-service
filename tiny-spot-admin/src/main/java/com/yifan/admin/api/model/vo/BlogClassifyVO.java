package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.entity.BlogClassify;
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
@ApiModel(value = "博客分类-VO")
public class BlogClassifyVO extends BlogClassify {

    @ApiModelProperty(value = "分类下文章数量")
    private long articleNum = 0L;

    public BlogClassifyVO() {
    }

    public BlogClassifyVO(BlogClassify classify) {
        if (classify != null){
            BeanUtils.copyProperties(classify, this);
        }
    }
}
