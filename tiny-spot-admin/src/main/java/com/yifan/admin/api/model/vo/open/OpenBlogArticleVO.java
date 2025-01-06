package com.yifan.admin.api.model.vo.open;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yifan.admin.api.entity.BlogArticle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("open博客文章-VO")
public class OpenBlogArticleVO {

    @ApiModelProperty(value = "ID")
    private Long articleId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "点击量")
    private int hits;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "允许评论 0：不允许 1：允许")
    private int allowComment;

    @ApiModelProperty(value = "排序")
    private int sort;

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

    @ApiModelProperty(value = "标签信息")
    private List<OpenBlogTagVO> tagVOList = Collections.emptyList();

    @ApiModelProperty(value = "创建人昵称")
    private String createNickName;


    public OpenBlogArticleVO(BlogArticle blogArticle, List<OpenBlogTagVO> openBlogTagVOS, String createNickName) {
        if (blogArticle != null) {
            BeanUtils.copyProperties(blogArticle, this);
            this.articleId = blogArticle.getId();
        }
        if (!openBlogTagVOS.isEmpty()) {
            this.tagVOList = openBlogTagVOS;
        }
        if (StringUtils.isNoneBlank(createNickName)) {
            this.createNickName = createNickName;
        }
    }
}
