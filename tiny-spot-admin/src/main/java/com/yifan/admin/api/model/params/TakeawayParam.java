package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TakeawayParam extends BasePageParam{

    @ApiModelProperty(value = "字数")
    private int keyworkNum = 0;

    @ApiModelProperty(value = "创建时间降序/升序")
    private String sortOrder = "descending";

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
