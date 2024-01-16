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
 * file_upload_record
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_file_upload_record")
@ApiModel(value="FileUploadRecord对象", description="文件上传记录表")
public class FileUploadRecord implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "原文件名")
    private String fileName;

    @ApiModelProperty(value = "文件类型：扩展名")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "文件url")
    private String fileUrl;

    @ApiModelProperty(value = "文件摘要hash")
    private String excerptHash;

    @ApiModelProperty(value = "上传人ID")
    private Long uploadUserId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}