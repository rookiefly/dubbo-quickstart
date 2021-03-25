package com.rookiefly.quickstart.dubbo.remote.api;

import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;

import java.util.List;

/**
 * 字典查询服务
 *
 * @dubbo
 */
public interface DictRemoteService {

    /**
     * 根据字典code查询字典明细数据
     *
     * @param code
     * @return
     */
    RpcResult<DictData> queryDictDataByCode(Long code);

    /**
     * 根据字典类型查询字典列表明细数据
     *
     * @param type
     * @return
     */
    RpcResult<List<DictData>> queryDictDataByType(String type);
}