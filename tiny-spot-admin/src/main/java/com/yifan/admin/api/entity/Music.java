package com.yifan.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_music")
@ApiModel(value = "Music对象", description = "音乐表")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("歌曲名")
    private String title;

    @ApiModelProperty("作者")
    private String artist;

    @ApiModelProperty("专辑")
    private String album;

    @ApiModelProperty("音乐url")
    private String src;

    @ApiModelProperty("图片url")
    private String pic;

    @ApiModelProperty(value = "排序")
    private int sort;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
