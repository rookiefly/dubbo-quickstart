package com.rookiefly.quickstart.dubbo.utils;

import com.google.common.collect.Lists;
import com.rookiefly.quickstart.dubbo.exception.BizErrorCodeEnum;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcBizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class ValidationUtils {

    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj obj
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            List<String> tipList = Lists.newArrayList();
            constraintViolations.forEach(constraintViolationImpl -> tipList.add(constraintViolationImpl.getMessage()));
            throw new RpcBizException(BizErrorCodeEnum.UNSPECIFIED.getCode(), StringUtils.join(tipList, ","));
        }
    }
}