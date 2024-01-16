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
 * @date 2023/11/21 16:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_ai_image_task")
@ApiModel(value = "AiTask对象", description = "AI应用任务表")
public class AiImageTask {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "配置类型：txt2img-baidu")
    private String configType;

    @ApiModelProperty(value = "提示词")
    private String prompt;

    @ApiModelProperty(value = "生成的数量")
    private Integer generateNum;

    @ApiModelProperty(value = "生成的尺寸")
    private String imageSize;

    @ApiModelProperty(value = "生成结果-url")
    private String generateDataUrl;

    @ApiModelProperty(value = "生成结果-base64")
    private String generateDataBase64;

    @ApiModelProperty(value = "状态 0：待完成 1：已完成 2：错误")
    private Integer status;

    @ApiModelProperty(value = "错误提示")
    private String errorMsg;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
