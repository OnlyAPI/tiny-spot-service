package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/29 17:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_blog_article_tag")
@ApiModel(value="BlogArticleTag对象", description="博客：文章-标签关联表")
public class BlogArticleTag {

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "标签ID")
    private Long tagId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    public BlogArticleTag(){}

    public BlogArticleTag(Long articleId) {
        this.articleId = articleId;
        this.createTime = new Date();
    }

    public BlogArticleTag(Long articleId, Long tagId){
        this.articleId = articleId;
        this.tagId = tagId;
        this.createTime = new Date();
    }
}
