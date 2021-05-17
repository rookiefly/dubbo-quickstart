package com.rookiefly.quickstart.dubbo.framework.rpc;

public class RpcBizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String code = "500";

    protected String msg = "服务器内部错误";

    public RpcBizException() {
    }

    public RpcBizException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public RpcBizException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RpcBizException{");
        sb.append("code=").append(this.code);
        sb.append(", msg='").append(this.msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}