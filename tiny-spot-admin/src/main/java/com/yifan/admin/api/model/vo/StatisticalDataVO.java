package com.yifan.admin.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/11 15:30
 */
@ApiModel("首页统计数据VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalDataVO {

    @ApiModelProperty("用户总计")
    private long userNum;

    @ApiModelProperty("文章总计")
    private long articleNum;

    @ApiModelProperty("登录总计")
    private long loginNum;

    @ApiModelProperty("歌曲总计")
    private long musicNum;

}

