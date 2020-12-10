package com.rookiefly.quickstart.dubbo.handler;

import com.rookiefly.quickstart.dubbo.monitor.PrometheusCustomMonitor;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.List;

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

    /**
     * 数据校验处理
     *
     * @param e
     * @return CommonResponse
     */
    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public CommonResponse validatorExceptionHandler(Exception e) {
        String msg = e instanceof BindException ? ((BindException) e).getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage()
                : ((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get().getMessage();
        CommonResponse errorResponse = CommonResponse.newErrorResponse();
        errorResponse.setMsg(msg);
        return errorResponse;
    }

    /**
     * 参数不合法异常
     *
     * @param ex
     * @return CommonResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResponse handleException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        CommonResponse errorResponse = CommonResponse.newErrorResponse();
        if (CollectionUtils.isNotEmpty(objectErrorList)) {
            errorResponse.setMsg(objectErrorList.stream().findFirst().get().getDefaultMessage());
        }
        return errorResponse;
    }
}