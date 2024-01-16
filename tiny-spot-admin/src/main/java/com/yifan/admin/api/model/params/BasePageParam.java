package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/6 10:50
 */
@Data
@ApiModel(value = "基础分页参数")
public class BasePageParam {

    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "页数")
    private Integer pageNum = 1;

}
