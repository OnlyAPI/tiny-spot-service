package com.yifan.admin.api.service;

import com.yifan.admin.api.model.vo.LineChartDataVO;
import com.yifan.admin.api.model.vo.StatisticalDataVO;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/11 17:04
 */
public interface IndexService {

    StatisticalDataVO getStatisticalData();

    LineChartDataVO getLineChartData(String type, int days);

}
