package com.yifan.admin.api.enums;

/**
 * 状态枚举
 * @author TaiYi
 * @ClassName
 * @date 2023/5/4 16:45
 */
public enum StatusEnum {

    DISABLE(0, "不可用"),
    USABLE(1, "可用"),
    ERROR(2, "异常"),
    ACTIVE(0, "未完成"),
    COMPLETED(1, "已完成"),
    DELETED(2, "删除")
    ;


    private final int status;

    private final String desc;

    StatusEnum(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
