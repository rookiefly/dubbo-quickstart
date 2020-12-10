package com.rookiefly.quickstart.dubbo.remote.api;

import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;

import java.util.List;

public interface DictRemoteService {

    RpcResult<DictData> queryDictDataByCode(Long code);

    RpcResult<List<DictData>> queryDictDataByType(String type);
}