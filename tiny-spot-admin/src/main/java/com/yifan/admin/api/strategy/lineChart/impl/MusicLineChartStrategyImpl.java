package com.yifan.admin.api.strategy.lineChart.impl;

import com.yifan.admin.api.mapper.MusicMapper;
import com.yifan.admin.api.enums.TimeFormatEnum;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/12 11:40
 */
@Slf4j
@Service(value = "musicLineChartStrategyImpl")
public class MusicLineChartStrategyImpl extends AbstractLineChartStrategyImpl {

    @Autowired
    private MusicMapper toolMusicMapper;

    @Override
    public List<LineChartDataDTO> getEveryDayData(String beginTime, String endTime) {
        return toolMusicMapper.getMusicEveryDayData(beginTime, endTime, TimeFormatEnum.DAY.getTimeFormatVal());
    }
}
