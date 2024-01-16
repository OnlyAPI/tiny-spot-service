package com.yifan.admin.api.entity;

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
@TableName("ts_blog_article_classify")
@ApiModel(value="BlogArticleClassify对象", description="博客：文章-分类关联表")
public class BlogArticleClassify {

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "分类ID")
    private Long classifyId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    public BlogArticleClassify(){}

    public BlogArticleClassify(Long articleId) {
        this.articleId = articleId;
        this.createTime = new Date();
    }

    public BlogArticleClassify(Long articleId, Long tagId){
        this.articleId = articleId;
        this.classifyId = tagId;
        this.createTime = new Date();
    }
}
