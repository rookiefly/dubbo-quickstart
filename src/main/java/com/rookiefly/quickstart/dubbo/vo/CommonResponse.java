package com.rookiefly.quickstart.dubbo.vo;

import com.alibaba.fastjson.JSONObject;
import com.rookiefly.quickstart.dubbo.exception.BizErrorCodeEnum;
import com.rookiefly.quickstart.dubbo.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 接口响应数据
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MSG = "success";

    private String code;

    private String msg;

    private Object data;

    public static CommonResponse newSuccessResponse() {
        return new CommonResponse(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public static CommonResponse newErrorResponse() {
        return newErrorResponse(BizErrorCodeEnum.UNSPECIFIED);
    }

    public static CommonResponse newErrorResponse(ErrorCode errorCode) {
        return new CommonResponse(errorCode.getCode(), errorCode.getDescription(), null);
    }

    public static CommonResponse newErrorResponse(ErrorCode errorCode, String errorMsg) {
        return new CommonResponse(errorCode.getCode(), errorMsg, null);
    }

    public static CommonResponse blocked() {
        return newErrorResponse(BizErrorCodeEnum.NO_SERVICE, "Blocked by Sentinel");
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}