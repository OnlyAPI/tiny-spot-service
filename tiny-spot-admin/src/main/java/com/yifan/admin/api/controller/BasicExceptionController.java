package com.yifan.admin.api.controller;

import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Slf4j
@RestController
@RequestMapping(value = "/error")
public class BasicExceptionController implements ErrorController {

	@RequestMapping
	public ResponseEntity<BaseResult<Serializable>> handleError(HttpServletRequest request, HttpServletResponse response) {
		Object error = request.getAttribute("javax.servlet.error.exception");
		log.error("request ERROR :{}", error);

		return new ResponseEntity<>(BaseResult.failed(ResultCode.FAILED,"请求的地址不存在"), HttpStatus.OK);
	}

}
