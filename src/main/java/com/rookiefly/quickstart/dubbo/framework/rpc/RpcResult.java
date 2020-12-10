package com.rookiefly.quickstart.dubbo.framework.rpc;

import java.io.Serializable;

public class RpcResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code = "500";

    private String msg;

    private T data;

    private Double providerTimeCost;

    public RpcResult() {
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

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Double getProviderTimeCost() {
        return this.providerTimeCost;
    }

    public void setProviderTimeCost(Double providerTimeCost) {
        this.providerTimeCost = providerTimeCost;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("RpcResult{");
        sb.append("code=").append(this.code);
        sb.append(", msg='").append(this.msg).append('\'');
        sb.append(", data=").append(this.data);
        sb.append(", providerTimeCost=").append(this.providerTimeCost);
        sb.append('}');
        return sb.toString();
    }
}