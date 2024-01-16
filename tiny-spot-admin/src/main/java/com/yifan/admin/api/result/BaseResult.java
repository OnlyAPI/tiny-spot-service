package com.yifan.admin.api.result;

import java.io.Serializable;

/**
 * 通用返回对象
 */
public class BaseResult<T> {
    private long code;
    private String msg;
    private T data;

    protected BaseResult() {
    }

    protected BaseResult(long code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public static <T> BaseResult<T> ok() {
        return ok(ResultCode.OK);
    }

    public static <T> BaseResult<T> ok(T data) {
        return ok(ResultCode.OK, data);
    }

    public static <T> BaseResult<T> ok(ResultCode code) {
        return ok(code.getCode(), code.getMsg(), null);
    }

    public static <T> BaseResult<T> ok(ResultCode code, T data) {
        return ok(code.getCode(), code.getMsg(), data);
    }

    public static <T> BaseResult<T> ok(long code, String message, T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(code);
        result.setMsg(message);
        result.setData(data);
        return result;
    }


    public static <T> BaseResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    public static <T> BaseResult<T> failed(ResultCode errorCode) {
        return failed(errorCode.getCode(), errorCode.getMsg(), null);
    }

    public static <T> BaseResult<T> failed(String message) {
        return failed(ResultCode.FAILED.getCode(), message, null);
    }

    public static <T> BaseResult<T> failed(ResultCode errorCode, T data) {
        return failed(errorCode.getCode(), errorCode.getMsg(), data);
    }

    public static <T> BaseResult<T> failed(long errorCode, String message, T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setCode(errorCode);
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    public static BaseResult<Serializable> result(int code, String msg) {
        return result(code, msg, null);
    }

    public static <T> BaseResult<T> result(int code, String msg, T data) {
        BaseResult<T> result = new BaseResult<T>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> BaseResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> BaseResult<T> validateFailed(String message) {
        return new BaseResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> BaseResult<T> unauthorized(T data) {
        return new BaseResult<T>(ResultCode.TOKEN_CHECK_FAILED.getCode(), ResultCode.TOKEN_CHECK_FAILED.getMsg(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> BaseResult<T> forbidden(T data) {
        return new BaseResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMsg(), data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
