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
 * @date 2023/11/13 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_ai_config")
@ApiModel(value="AiConfig对象", description="AI配置")
public class AiConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "配置类型")
    private String configType;

    @ApiModelProperty(value = "服务器URL")
    private String serverUrl;

    @ApiModelProperty(value = "授权配置")
    private String authConfig;

    @ApiModelProperty(value = "代理类型")
    private String proxyType;

    @ApiModelProperty(value = "代理主机")
    private String proxyHost;

    @ApiModelProperty(value = "代理端口")
    private Integer proxyPort;

    @ApiModelProperty(value = "密钥名称")
    private String keyName;

    @ApiModelProperty(value = "状态 0：禁用 1：可用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
