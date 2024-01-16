package com.yifan.admin.api.model.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/14 12:24
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("文件上传记录-请求参数")
@Data
public class FileUploadRecordsParam extends BasePageParam{

    @ApiModelProperty(value = "文件名/文件URL/文件Hash值")
    private String keyword;

    @ApiModelProperty(value = "升序或降序")
    private String sortOrder = "descending";

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
