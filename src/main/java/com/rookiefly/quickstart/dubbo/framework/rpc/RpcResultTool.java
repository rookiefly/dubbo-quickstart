package com.rookiefly.quickstart.dubbo.framework.rpc;

public class RpcResultTool {

    public static <T> RpcResult<T> newSuccessRpcResult(T data) {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(RpcCode.SUCCESS);
        rpcResult.setData(data);
        return rpcResult;
    }

    public static <T> RpcResult<T> newErrorRpcResult(T data) {
        RpcResult<T> rpcResult = new RpcResult<>();
        rpcResult.setCode(RpcCode.ERROR);
        rpcResult.setData(data);
        return rpcResult;
    }
}
