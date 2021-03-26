package com.rookiefly.quickstart.dubbo.remote.impl;

import com.rookiefly.quickstart.dubbo.bo.CityDataBO;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResultTool;
import com.rookiefly.quickstart.dubbo.remote.api.CityData;
import com.rookiefly.quickstart.dubbo.remote.api.CityRemoteService;
import com.rookiefly.quickstart.dubbo.service.CityService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0")
public class CityRemoteServiceImpl implements CityRemoteService {

    @Resource
    private CityService cityService;

    @Override
    public RpcResult<CityData> queryCityDataByCityId(Long cityId) {
        CityDataBO cityDataBO = cityService.queryCityDataByCityId(cityId);
        CityData cityData = new CityData();
        BeanUtils.copyProperties(cityDataBO, cityData);
        return RpcResultTool.newSuccessRpcResult(cityData);
    }

    @Override
    public RpcResult<List<CityData>> queryCityDataByType(Integer type) {
        List<CityDataBO> cityDataBOList = cityService.queryCityDataByType(type);
        List<CityData> cityDataList = cityDataBOList.stream().map(cityDataBO -> {
            CityData cityData = new CityData();
            BeanUtils.copyProperties(cityDataBO, cityData);
            return cityData;
        }).collect(Collectors.toList());
        return RpcResultTool.newSuccessRpcResult(cityDataList);
    }
}