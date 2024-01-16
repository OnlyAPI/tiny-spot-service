package com.yifan.admin.api.controller;

import com.yifan.admin.api.service.IndexService;
import com.yifan.admin.api.model.vo.LineChartDataVO;
import com.yifan.admin.api.model.vo.StatisticalDataVO;
import com.yifan.admin.api.result.BaseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/7/11 15:24
 */
@RestController
@RequestMapping("/index")
public class IndexStrategyController {

    @Autowired
    private IndexService indexService;


    @GetMapping("/statistical")
    @ApiOperation("获取统计数据")
    public BaseResult<StatisticalDataVO> getStatisticalData(){
        return BaseResult.ok(indexService.getStatisticalData());
    }

    @GetMapping("/lineChart")
    @ApiOperation("获取折线数据")
    public BaseResult<LineChartDataVO> getLineChartData(@RequestParam(value = "type", defaultValue = "user") String type,
                                                        @RequestParam(value = "days", defaultValue = "7") int days){
        return BaseResult.ok(indexService.getLineChartData(type, days));
    }

}
