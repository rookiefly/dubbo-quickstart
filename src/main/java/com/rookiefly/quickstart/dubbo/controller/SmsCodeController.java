package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.bo.SmsCodeBO;
import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;
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
import java.util.HashMap;

@RestController
@RequestMapping("/smsCode")
public class SmsCodeController {

    @Resource
    private SmsCodeService smsCodeService;

    @GetMapping("/send/{mobile}")
    public CommonResponse sendSmsCode(@PathVariable("mobile") @NotBlank(message = "手机号不能为空") String mobile) {
        SmsCodeBO smsCodeBO = smsCodeService.sendSmsCode(mobile);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        HashMap<Object, SmsCodeBO> data = new HashMap<>();
        successResponse.setData(data);
        data.put("smsCode", smsCodeBO);
        return successResponse;
    }

    @PostMapping("/validate")
    public CommonResponse validateSmsCode(@RequestBody @Valid SmsCodeParam smsCodeParam) {
        Boolean result = smsCodeService.validateSmsCode(smsCodeParam);
        return result ? CommonResponse.newSuccessResponse() : CommonResponse.newErrorResponse();
    }
}