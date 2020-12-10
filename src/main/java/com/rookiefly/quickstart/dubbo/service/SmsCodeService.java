package com.rookiefly.quickstart.dubbo.service;

import com.rookiefly.quickstart.dubbo.bo.SmsCodeValidateResultBO;
import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;

/**
 * 短信验证码服务
 */
public interface SmsCodeService {

    String sendSmsCode(String mobile);

    SmsCodeValidateResultBO validateSmsCode(SmsCodeParam smsCodeParam);
}
