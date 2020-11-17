package com.rookiefly.quickstart.dubbo.model;

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

    private int code;

    private String msg;

    private Object data;

    public static CommonResponse newSuccessResponse() {
        return new CommonResponse(200, "success", null);
    }

    public static CommonResponse newErrorResponse() {
        return new CommonResponse(500, "error", null);
    }
}