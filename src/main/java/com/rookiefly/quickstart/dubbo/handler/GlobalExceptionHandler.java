package com.rookiefly.quickstart.dubbo.handler;

import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
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
import java.util.Optional;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String CONTROLLER_ERROR = "error";
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
        log.error(CONTROLLER_ERROR, ex);
        monitor.getRequestErrorCount().increment();
        if (ex instanceof NotLoginException) {    // 如果是未登录异常
            return CommonResponse.newErrorResponse(BizErrorCodeEnum.NOT_LOGIN_ERROR);
        } else if (ex instanceof NotRoleException) {        // 如果是角色异常
            return CommonResponse.newErrorResponse(BizErrorCodeEnum.NO_ROLE_ERROR);
        } else if (ex instanceof NotPermissionException) {    // 如果是权限异常
            return CommonResponse.newErrorResponse(BizErrorCodeEnum.NO_PERMISSION_ERROR);
        } else if (ex instanceof DisableLoginException) {    // 如果是被封禁异常
            return CommonResponse.newErrorResponse(BizErrorCodeEnum.ACCOUNT_BANNED_ERROR);
        }
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
        log.error(CONTROLLER_ERROR, ex);
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
        log.error(CONTROLLER_ERROR, ex);
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> objectErrorList = bindingResult.getAllErrors();
        Optional<ObjectError> objectErrorOptional = objectErrorList.stream().findFirst();
        String msg = objectErrorOptional.isPresent() ? objectErrorOptional.get().getDefaultMessage() : "参数异常";
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
        log.error(CONTROLLER_ERROR, ex);
        String msg = ex instanceof BindException ? ((BindException) ex).getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage()
                : ((ConstraintViolationException) ex).getConstraintViolations().stream().findFirst().get().getMessage();
        return CommonResponse.newErrorResponse(BizErrorCodeEnum.REQUEST_ERROR, msg);
    }
}