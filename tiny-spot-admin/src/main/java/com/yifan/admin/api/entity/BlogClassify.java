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
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_blog_classify")
@ApiModel(value="BlogClassify对象", description="博客-分类表")
public class BlogClassify {

    @TableId(value = "classify_id", type = IdType.AUTO)
    private Long classifyId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "状态 0：禁用 1：可用")
    private int status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
