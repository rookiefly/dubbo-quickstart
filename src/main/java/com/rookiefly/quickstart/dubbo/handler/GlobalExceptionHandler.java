package com.rookiefly.quickstart.dubbo.handler;

import com.rookiefly.quickstart.dubbo.model.CommonResponse;
import com.rookiefly.quickstart.dubbo.monitor.PrometheusCustomMonitor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private PrometheusCustomMonitor monitor;

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public CommonResponse handle(Exception e) {
        monitor.getRequestErrorCount().increment();
        return CommonResponse.newErrorResponse();
    }
}