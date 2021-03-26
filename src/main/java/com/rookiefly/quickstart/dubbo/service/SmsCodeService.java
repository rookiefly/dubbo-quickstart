package com.rookiefly.quickstart.dubbo.service;

import com.rookiefly.quickstart.dubbo.bo.SmsCodeBO;
import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;

/**
 * 短信验证码服务
 */
public interface SmsCodeService {

    SmsCodeBO sendSmsCode(String mobile);

    Boolean validateSmsCode(SmsCodeParam smsCodeParam);
}
