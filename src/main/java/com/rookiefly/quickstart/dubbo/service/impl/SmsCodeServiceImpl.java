package com.rookiefly.quickstart.dubbo.service.impl;

import com.rookiefly.quickstart.dubbo.bo.SmsCodeValidateResultBO;
import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;
import com.rookiefly.quickstart.dubbo.service.SmsCodeService;
import org.springframework.stereotype.Service;

@Service("smsCodeService")
public class SmsCodeServiceImpl implements SmsCodeService {
    @Override
    public String sendSmsCode(String mobile) {
        return null;
    }

    @Override
    public SmsCodeValidateResultBO validateSmsCode(SmsCodeParam smsCodeParam) {
        return null;
    }
}
