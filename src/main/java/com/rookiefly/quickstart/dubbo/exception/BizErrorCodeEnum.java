package com.rookiefly.quickstart.dubbo.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务错误码
 */
public enum BizErrorCodeEnum implements ErrorCode {

    /**
     * 未指明的异常
     */
    UNSPECIFIED("500", "网络异常，请稍后再试"),
    NO_SERVICE("404", "网络异常, 服务器熔断"),

    // 通用异常
    REQUEST_ERROR("400", "入参异常,请检查入参后再次调用"),
    PAGE_NUM_IS_NULL("4001", "页码不能为空"),
    PAGE_SIZE_IS_NULL("4002", "页数不能为空"),
    ID_IS_NULL("4003", "ID不能为空"),
    SEARCH_IS_NULL("4004", "搜索条件不能为空"),
    REQUEST_RATE_LIMIT_ERROR("4005", "请求被限流"),


    // 短信相关
    SEND_MASSAGE_FAIL("30001", "发送短消息失败"),
    SMS_CODE_NOT_EXITS("30002", "无效的验证码，请重试"),
    SMS_CODE_ILLEGAL("30003", "验证码错误"),
    ;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 描述
     */
    private final String description;

    /**
     * @param code        错误码
     * @param description 描述
     */
    BizErrorCodeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据编码查询枚举。
     *
     * @param code 编码。
     * @return 枚举。
     */
    public static BizErrorCodeEnum getByCode(String code) {
        for (BizErrorCodeEnum value : BizErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return value;
            }
        }
        return UNSPECIFIED;
    }

    /**
     * 枚举是否包含此code
     *
     * @param code 枚举code
     * @return 结果
     */
    public static Boolean contains(String code) {
        for (BizErrorCodeEnum value : BizErrorCodeEnum.values()) {
            if (StringUtils.equals(code, value.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
