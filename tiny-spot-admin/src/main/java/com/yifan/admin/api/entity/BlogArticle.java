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
 * @date 2023/5/19 16:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_blog_article")
@ApiModel(value="BlogArticle对象", description="博客-文章表")
public class BlogArticle {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

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

}

