package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/6 18:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "监控-操作日志-请求参数")
public class MonitorOperLogParam extends BasePageParam {

    @ApiModelProperty(value = "操作人员id")
    private Long operAdminId;

    @ApiModelProperty(value = "请求地址")
    private String requestUri;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "升序、降序")
    private String sortOrder = "descending";

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
