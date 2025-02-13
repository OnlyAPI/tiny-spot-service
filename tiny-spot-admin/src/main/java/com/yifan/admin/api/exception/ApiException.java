package com.yifan.admin.api.exception;


import com.yifan.admin.api.result.ResultCode;

/**
 * 自定义API异常
 */
public class ApiException extends RuntimeException {

    private int errorCode;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(ResultCode code){
        super(code.getMsg());
        this.errorCode = code.getCode();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public static ApiException tokenExpiredException() {
        return new ApiException(ResultCode.TOKEN_EXPIRED);
    }

    public static ApiException tokenBlankException() {
        return new ApiException(ResultCode.TOKEN_CHECK_FAILED);
    }
}
