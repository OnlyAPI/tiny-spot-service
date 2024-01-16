package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/19 17:08
 */
@ApiModel(value = "添加或修改文章-请求参数")
@Data
public class SaveOrUpdateArticleParam {

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "title can not be null.")
    private String title;

    @ApiModelProperty(value = "副标题")
    @NotBlank(message = "title can not be null.")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "标签IDs")
    private List<Object> tagIds;

    @ApiModelProperty(value = "分类ID")
    private Object classifyId;

    @ApiModelProperty(value = "允许评论 0：不允许 1：允许")
    private int allowComment;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty(value = "状态 0:草稿 1：发布")
    private int status;

    @ApiModelProperty(value = "是否置顶 0：不置顶 1：置顶")
    private int isTop;

}
