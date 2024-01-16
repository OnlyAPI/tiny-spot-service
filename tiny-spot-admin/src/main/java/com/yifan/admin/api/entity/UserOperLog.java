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
 * 操作日志记录表 oper_log
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ts_user_oper_log")
@ApiModel(value = "UserOperLog对象", description = "用户操作日志表")
public class UserOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("操作模块")
    private String title;

    @ApiModelProperty("业务类型（0其它 1新增 2修改 3删除 4授权 5:导出 6:导入）")
    private Integer businessType;

    @ApiModelProperty("操作类别（0 后台用户）")
    private Integer operatorType;

    @ApiModelProperty("操作人员")
    private Long operAdminId;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("请求uri")
    private String requestUri;

    @ApiModelProperty("响应码")
    private Long respCode;

    @ApiModelProperty("响应消息")
    private String respMsg;

    @ApiModelProperty("操作状态")
    private String status;

    @ApiModelProperty("错误消息")
    private String errorMsg;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
