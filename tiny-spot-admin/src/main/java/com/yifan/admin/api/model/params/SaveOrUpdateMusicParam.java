package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/12
 */
@Data
@ApiModel(value = "新增或修改请求参数")
public class SaveOrUpdateMusicParam {

    @ApiModelProperty("歌曲名")
    @NotBlank(message = "歌曲名不能为空")
    private String title;

    @ApiModelProperty("作者")
    @NotBlank(message = "作者不能为空")
    private String artist;

    @ApiModelProperty("专辑")
    private String album;

    @ApiModelProperty("音乐url")
    @NotBlank(message = "音乐url不能为空")
    private String src;

    @ApiModelProperty("图片url")
    private String pic;

    @ApiModelProperty(value = "排序")
    private int sort;
}
