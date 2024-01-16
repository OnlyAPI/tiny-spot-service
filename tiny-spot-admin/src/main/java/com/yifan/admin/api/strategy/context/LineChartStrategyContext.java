package com.yifan.admin.api.strategy.context;

import com.yifan.admin.api.strategy.lineChart.LineChartStrategy;
import com.yifan.admin.api.enums.LineChartDataTypeEnum;
import com.yifan.admin.api.model.vo.LineChartDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/12 10:30
 */
@Component
public class LineChartStrategyContext {

    @Autowired
    private Map<String, LineChartStrategy> lineChartStrategyMap;


    public LineChartDataVO executeLineChart(LineChartDataTypeEnum typeEnum, int days) {
        return lineChartStrategyMap.get(typeEnum.getStrategy()).getLineChartData(typeEnum.getType(), days);
    }

}
