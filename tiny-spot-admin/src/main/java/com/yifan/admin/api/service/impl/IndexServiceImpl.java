package com.yifan.admin.api.service.impl;

import com.yifan.admin.api.service.*;
import com.yifan.admin.api.strategy.context.LineChartStrategyContext;
import com.yifan.admin.api.enums.LineChartDataTypeEnum;
import com.yifan.admin.api.model.vo.LineChartDataVO;
import com.yifan.admin.api.model.vo.StatisticalDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/11 17:04
 */
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private SysUserService userService;
    @Autowired
    private BlogArticleService blogArticleService;
    @Autowired
    private UserLoginLogService userLoginLogService;
    @Autowired
    private MusicService toolMusicService;
    @Autowired
    private LineChartStrategyContext lineChartStrategyContext;

    @Override
    public StatisticalDataVO getStatisticalData() {
        long userCount = userService.count();
        long articleCount = blogArticleService.count();
        long loginCount = userLoginLogService.count();
        long musicCount = toolMusicService.count();

        return new StatisticalDataVO(userCount, articleCount, loginCount, musicCount);
    }

    @Override
    public LineChartDataVO getLineChartData(String type, int days) {
        return lineChartStrategyContext.executeLineChart(LineChartDataTypeEnum.getLineChartDataTypeEnum(type), days);
    }
}
