//package com.rookiefly.quickstart.dubbo.service.impl;
//
//import com.rookiefly.quickstart.dubbo.bo.SmsCodeBO;
//import com.rookiefly.quickstart.dubbo.exception.BizErrorCodeEnum;
//import com.rookiefly.quickstart.dubbo.exception.BizException;
//import com.rookiefly.quickstart.dubbo.param.SmsCodeParam;
//import com.rookiefly.quickstart.dubbo.service.SmsCodeService;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
//@Service("smsCodeService")
//public class SmsCodeServiceImpl implements SmsCodeService {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public static final int SMS_CODE_LENGTH = 6;
//    public static final int TIMEOUT = 5;
//    private static final String SMS_CODE_KEY = "smscode:%s";
//
//    @Value("${rocketmq.sms.topic}")
//    private String smsTopic;
//
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Resource
//    private RocketMQTemplate rocketMQTemplate;
//
//    @Override
//    public SmsCodeBO sendSmsCode(String mobile) {
//        String smsCode = RandomStringUtils.randomNumeric(SMS_CODE_LENGTH);
//        String smsCodeKey = getSmsCodeKey(mobile);
//        stringRedisTemplate.opsForValue().set(smsCodeKey, smsCode, TIMEOUT, TimeUnit.MINUTES);
//        SmsCodeBO smsCodeBO = new SmsCodeBO();
//        smsCodeBO.setSmsCode(smsCode);
//        smsCodeBO.setMobile(mobile);
//        try {
//            rocketMQTemplate.send(smsTopic, MessageBuilder.withPayload(smsCodeBO).build());
//        } catch (Exception e) {
//            logger.error("send mq error", e);
//        }
//        return smsCodeBO;
//    }
//
//    @Override
//    public Boolean validateSmsCode(SmsCodeParam smsCodeParam) {
//        String smsCodeKey = getSmsCodeKey(smsCodeParam.getMobile());
//        String actualSmsCode = stringRedisTemplate.opsForValue().get(smsCodeKey);
//        if (StringUtils.isBlank(actualSmsCode)) {
//            throw new BizException(BizErrorCodeEnum.SMS_CODE_NOT_EXITS);
//        } else if (smsCodeParam.getSmsCode().equals(actualSmsCode)) {
//            return true;
//        } else {
//            throw new BizException(BizErrorCodeEnum.SMS_CODE_ILLEGAL);
//        }
//    }
//
//    private String getSmsCodeKey(String mobile) {
//        String smsCodeKey = String.format(SMS_CODE_KEY, mobile);
//        return smsCodeKey;
//    }
//}
