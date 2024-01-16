package com.yifan.admin.api.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/11 15:32
 */
@ApiModel("折线图数据VO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineChartDataVO {

    @ApiModelProperty("统计名称")
    private String name;

    @ApiModelProperty("日期数据")
    private List<String> dateData;

    @ApiModelProperty("统计数据")
    private List<Integer> statisticsData;
}
