package com.rookiefly.quickstart.dubbo.bo;

import lombok.Data;

@Data
public class SmsCodeValidateResultBO {

    private Boolean checked;

    private String errorMsg;
}
