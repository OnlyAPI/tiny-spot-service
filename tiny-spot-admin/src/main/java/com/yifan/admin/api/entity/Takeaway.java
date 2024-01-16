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
 * @date: 2023/5/6
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_takeaway")
@ApiModel(value="Takeaway对象", description="好评表")
public class Takeaway implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("内容")
    private String contentText;

    @ApiModelProperty("点赞数")
    private int niceNum;

    @ApiModelProperty("字数")
    private int keyworkNum;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
