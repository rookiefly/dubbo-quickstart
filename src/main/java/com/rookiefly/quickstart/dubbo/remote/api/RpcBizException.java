package com.rookiefly.quickstart.dubbo.remote.api;

public class RpcBizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_MSG = "默认系统错误";

    protected int code = -1;

    protected String msg = "默认系统错误";

    public RpcBizException() {
    }

    public RpcBizException(Throwable cause) {
        super(cause);
    }

    public RpcBizException(int code) {
        super("默认系统错误");
        this.code = code;
    }

    public RpcBizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public RpcBizException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RpcBizException{");
        sb.append("code=").append(this.code);
        sb.append(", msg='").append(this.msg).append('\'');
        sb.append('}');
        return sb.toString();
    }
}