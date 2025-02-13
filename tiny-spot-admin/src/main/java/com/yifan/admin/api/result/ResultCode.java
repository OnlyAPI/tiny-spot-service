package com.yifan.admin.api.result;

public enum ResultCode {

    OK(2000, "OK"),
    FAILED(9999, "操作失败"),


    VALIDATE_FAILED(5001, "参数检验失败"),
    URL_ERROR(5002, "未找到资源"),
    METHOD_ERROR(5003, "提交方式错误"),
    MEDIA_TYPE_ERROR(5004, "数据格式错误"),
    OAUTH2_LOGIN_ERROR(5005, "三方授权登录失败"),

    TOKEN_CHECK_FAILED(6000, "请先登录"),
    FORBIDDEN(6001, "暂无权限"),
    USER_DISABLE(6002, "账户不可用"),
    USER_EXISTS(6003, "账户已存在，请登录!"),
    TOKEN_EXPIRED(6004, "登录已过期"),
    ;

    private Integer code;
    private String msg;

    private ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
