package com.yifan.admin.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/7/11
 */
@Getter
@AllArgsConstructor
public enum LineChartDataTypeEnum {

    USER("user", "用户新增", "userLineChartStrategyImpl"),
    ARTICLE("article", "文章新增", "articleLineChartStrategyImpl"),
    LOGIN("login", "登录新增", "loginLineChartStrategyImpl"),
    MUSIC("music", "歌曲新增", "musicLineChartStrategyImpl")
    ;


    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String name;

    /**
     * 策略
     */
    private final String strategy;

    public static LineChartDataTypeEnum getLineChartDataTypeEnum(String type) {
//        return Arrays.stream(LineChartDataTypeEnum.values()).filter(e -> {
//            return e.getType().equalsIgnoreCase(type);
//        }).findFirst().orElseThrow(() -> new ApiException("折线图数据类型错误"));
        return Arrays.stream(LineChartDataTypeEnum.values()).filter(e -> {
            return e.getType().equalsIgnoreCase(type);
        }).findFirst().orElse(USER);
    }
}
