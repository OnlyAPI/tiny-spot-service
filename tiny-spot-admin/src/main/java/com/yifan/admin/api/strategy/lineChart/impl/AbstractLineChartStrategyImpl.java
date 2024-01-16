package com.yifan.admin.api.strategy.lineChart.impl;

import com.yifan.admin.api.strategy.lineChart.LineChartStrategy;
import com.yifan.admin.api.enums.LineChartDataTypeEnum;
import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.model.dto.LineChartDataDTO;
import com.yifan.admin.api.model.vo.LineChartDataVO;
import com.yifan.admin.api.result.ResultCode;
import com.yifan.admin.api.utils.DateTimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/11
 */
public abstract class AbstractLineChartStrategyImpl implements LineChartStrategy {


    @Override
    public LineChartDataVO getLineChartData(String type, int days) {
        //1. 先获取前n天的字符串集合
        List<String> dayStrList = DateTimeUtil.getDayStrList(days);
        if (dayStrList.isEmpty()) {
            throw new ApiException(ResultCode.VALIDATE_FAILED);
        }

        final String beginTime = DateTimeUtil.buildBeginTime(dayStrList.get(0));
        final String endTime = DateTimeUtil.buildEndTime(dayStrList.get(dayStrList.size() - 1));

        final String todayStr = DateTimeUtil.currentYMD();
        List<String> dateList = dayStrList.stream().map( e -> {
            return e.equals(todayStr) ? "今天" : e;
        }).collect(Collectors.toList());

        List<Integer> statisticsData;

        //2.获取每日数据
        List<LineChartDataDTO> everyDayData = getEveryDayData(beginTime, endTime);

        if (everyDayData.isEmpty()) {
            statisticsData = new ArrayList<>(Collections.nCopies(days, 0));

        } else {
            //3.将有日期数据填充，没有则赋值0
            Map<String, Integer> dataMap = everyDayData.stream().collect(Collectors.toMap(LineChartDataDTO::getCrdDateTime, LineChartDataDTO::getTotal));

            statisticsData = new ArrayList<>(days);
            dayStrList.forEach(e -> {
                statisticsData.add(dataMap.getOrDefault(e, 0));
            });
        }

        //4 组装数据
        return new LineChartDataVO(LineChartDataTypeEnum.getLineChartDataTypeEnum(type).getName(), dateList, statisticsData);
    }

    public abstract List<LineChartDataDTO> getEveryDayData(String beginTime, String endTime);

}

