package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/5/11 17:24
 */
@ApiModel(value = "菜单树请求参数")
@Data
public class MenuTreeListParams {

    @ApiModelProperty(value = "标题")
    private String title;
}
