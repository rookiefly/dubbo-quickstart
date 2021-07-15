package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.bo.SmsCodeBO;
import com.rookiefly.quickstart.dubbo.exception.BizException;
import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;
import com.rookiefly.quickstart.dubbo.ratelimiter.RateLimiter;
import com.rookiefly.quickstart.dubbo.ratelimiter.RateLimiterFactory;
import com.rookiefly.quickstart.dubbo.service.SmsCodeService;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.rookiefly.quickstart.dubbo.exception.BizErrorCodeEnum.REQUEST_RATE_LIMIT_ERROR;

@RestController
@RequestMapping("/smsCode")
public class SmsCodeController {

    @Resource
    private SmsCodeService smsCodeService;

    @Resource
    private RateLimiterFactory rateLimiterFactory;

    @GetMapping("/send/{mobile}")
    public CommonResponse sendSmsCode(@PathVariable("mobile") @NotBlank(message = "手机号不能为空") String mobile) {
        RateLimiter rateLimiter = rateLimiterFactory.getRateLimiter("sendSmsCode", 3, 30);
        if (!rateLimiter.acquire()) {
            throw new BizException(REQUEST_RATE_LIMIT_ERROR);
        }
        SmsCodeBO smsCodeBO = smsCodeService.sendSmsCode(mobile);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        successResponse.setData(smsCodeBO);
        return successResponse;
    }

    @PostMapping("/validate")
    public CommonResponse validateSmsCode(@RequestBody @Valid SmsCodeParam smsCodeParam) {
        Boolean result = smsCodeService.validateSmsCode(smsCodeParam);
        return result ? CommonResponse.newSuccessResponse() : CommonResponse.newErrorResponse();
    }
}
