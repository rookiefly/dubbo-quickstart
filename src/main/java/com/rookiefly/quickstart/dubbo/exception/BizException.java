package com.rookiefly.quickstart.dubbo.exception;

import com.rookiefly.quickstart.dubbo.framework.rpc.RpcBizException;

public class BizException extends RpcBizException {

    private static final long serialVersionUID = 1L;

    public BizException(int code, String msg) {
        super(code, msg);
    }

    public BizException(int code, String msg, Throwable e) {
        super(code, msg, e);
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        return msg == null ? this.toString() : msg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BizException{");
        sb.append("code=").append(this.code);
        sb.append(", msg='").append(this.msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}