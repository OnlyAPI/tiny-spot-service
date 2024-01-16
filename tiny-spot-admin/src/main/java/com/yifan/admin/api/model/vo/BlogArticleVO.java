package com.yifan.admin.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yifan.admin.api.entity.BlogArticle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/23
 */
@Data
@ApiModel("博客文章-VO")
public class BlogArticleVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签IDs")
    private List<Long> tagIds = Collections.emptyList();

    @ApiModelProperty(value = "分类ID")
    private Long classifyId;

    @ApiModelProperty(value = "点击量")
    private int hits;

    @ApiModelProperty(value = "允许评论 0：不允许 1：允许")
    private int allowComment;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "状态 0:草稿 1：发布")
    private int status;

    @ApiModelProperty(value = "是否置顶 0：不置顶 1：置顶")
    private int isTop;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createBy;


    public BlogArticleVO(){}

    public BlogArticleVO(BlogArticle param){
        if (param != null){
            BeanUtils.copyProperties(param, this);
        }
    }
}
