package com.rookiefly.quickstart.dubbo.handler;

import com.rookiefly.quickstart.dubbo.exception.BizErrorCodeEnum;
import com.rookiefly.quickstart.dubbo.exception.BizException;
import com.rookiefly.quickstart.dubbo.monitor.PrometheusCustomMonitor;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GlobalExceptionHandler {

    @Resource
    private PrometheusCustomMonitor monitor;

    /**
     * 通用异常处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public CommonResponse handle(Exception ex) {
        log.error("error", ex);
        monitor.getRequestErrorCount().increment();
        return CommonResponse.newErrorResponse();
    }

    /**
     * 业务异常处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = BizException.class)
    public CommonResponse handle(BizException ex) {
        log.error("error", ex);
        monitor.getRequestErrorCount().increment();
        return CommonResponse.newErrorResponse(ex.getErrorCode());
    }

    /**
     * 参数不合法异常
     *
     * @param ex
     * @return CommonResponse
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleException(MethodArgumentNotValidException ex) {
        log.error("error", ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        String msg = objectErrorList.stream().findFirst().get().getDefaultMessage();
        return CommonResponse.newErrorResponse(BizErrorCodeEnum.REQUEST_ERROR, msg);
    }

    /**
     * 数据校验处理
     *
     * @param ex
     * @return CommonResponse
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public CommonResponse validatorExceptionHandler(Exception ex) {
        log.error("error", ex);
        String msg = ex instanceof BindException ? ((BindException) ex).getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage()
                : ((ConstraintViolationException) ex).getConstraintViolations().stream().findFirst().get().getMessage();
        return CommonResponse.newErrorResponse(BizErrorCodeEnum.REQUEST_ERROR, msg);
    }
}