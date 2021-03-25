package com.rookiefly.quickstart.dubbo.remote.api;

import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;

import java.util.List;

/**
 * 城市数据查询服务
 *
 * @dubbo
 */
public interface CityRemoteService {

    /**
     * 城市id查询城市
     *
     * @param cityId
     * @return
     */
    RpcResult<CityData> queryCityDataByCityId(Long cityId);

    /**
     * 分类id查询城市
     *
     * @param type 0：省，1：市，2：区，3：镇
     * @return
     */
    RpcResult<List<CityData>> queryCityDataByType(Integer type);
}