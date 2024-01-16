package com.yifan.admin.api.strategy.lineChart;

import com.yifan.admin.api.model.vo.LineChartDataVO;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/11
 */
public interface LineChartStrategy {

    LineChartDataVO getLineChartData(String type, int days);
}
