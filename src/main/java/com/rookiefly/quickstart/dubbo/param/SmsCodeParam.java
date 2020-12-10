package com.rookiefly.quickstart.dubbo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SmsCodeParam {

    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    @NotBlank(message = "手机号不能为空")
    private String mobile;
}
