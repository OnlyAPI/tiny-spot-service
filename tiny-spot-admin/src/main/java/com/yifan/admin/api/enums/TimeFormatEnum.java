package com.yifan.admin.api.enums;

/**
 * @author TaiYi
 * @ClassName
 * @date 2022/11/25 20:01
 */
public enum TimeFormatEnum {

    HOUR("HOUR", "%H:00"),
    DAY("DAY", "%Y-%m-%d"),
    WEEK("WEEK", "%Y-%u"),
    MONTH("MONTH", "%Y-%m");

    private final String timeFormatKey;
    private final String timeFormatVal;

    TimeFormatEnum(String timeFormatKey, String timeFormatVal) {
        this.timeFormatKey = timeFormatKey;
        this.timeFormatVal = timeFormatVal;
    }

    public String getTimeFormatKey() {
        return timeFormatKey;
    }

    public String getTimeFormatVal() {
        return timeFormatVal;
    }

    public static String getTimeFormatVal(String timeFormatKey){
        TimeFormatEnum[] values = TimeFormatEnum.values();
        for (TimeFormatEnum formatEnum : values) {
            if (formatEnum.getTimeFormatKey().equalsIgnoreCase(timeFormatKey)) {
                return formatEnum.getTimeFormatVal();
            }
        }
        return HOUR.getTimeFormatVal();
    }
}
