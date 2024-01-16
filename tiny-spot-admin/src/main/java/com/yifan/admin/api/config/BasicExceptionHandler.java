package com.yifan.admin.api.config;

import com.yifan.admin.api.exception.ApiException;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.ResultCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.Serializable;


@Slf4j
@ControllerAdvice
public class BasicExceptionHandler {

    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> apiException(ApiException e) {
        log.error("[apiException] msg: {}.", e.getMessage());
        return BaseResult.result(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> constraintViolationException(ConstraintViolationException ex) {
        log.error("constraintViolationException.", ex);
        return BaseResult.result(ResultCode.VALIDATE_FAILED.getCode(), ex.getMessage());
    }

    /**
     * 参数验证失败
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> badRequestException(ValidationException ex) {
        log.error("validationException.", ex);
        return BaseResult.result(ResultCode.VALIDATE_FAILED.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> badRequestException(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException.", ex);
        return BaseResult.result(ResultCode.VALIDATE_FAILED.getCode(), ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("httpMessageNotReadableException.", ex);
        return BaseResult.result(ResultCode.VALIDATE_FAILED.getCode(), "参数格式或内容不正确");
    }

    /**
     * 未处理异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResult<Serializable> exception(Exception ex, HttpServletResponse response) {

        log.error("系统错误", ex);

        if (ex instanceof ServletRequestBindingException || ex instanceof BindException) {
            // 参数映射失败
            return BaseResult.failed(ResultCode.VALIDATE_FAILED);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            // 请求方式不支持
            return BaseResult.failed(ResultCode.METHOD_ERROR);
        } else if (ex instanceof NoHandlerFoundException) {
            // 没找到处理方法
            return BaseResult.failed(ResultCode.URL_ERROR);
        } else if (ex instanceof FeignException) {
            return BaseResult.failed(ResultCode.OAUTH2_LOGIN_ERROR);
        } else if (ex instanceof HttpMediaTypeException) {
            // 参数提交格式不正确
            return BaseResult.failed(ResultCode.MEDIA_TYPE_ERROR);
        } else {
            //系统未知错误
            return BaseResult.failed(ResultCode.FAILED);
        }
    }
}
