package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/6 10:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "监控-登录日志-请求参数")
public class MonitorLoginLogParam extends BasePageParam{

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "升序或降序")
    private String sortOrder = "descending";

    @ApiModelProperty(value = "用户id")
    private Long adminId;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结合时间")
    private String endTime;

}
