package com.rookiefly.quickstart.dubbo.remote.impl;

import com.rookiefly.quickstart.dubbo.bo.CityDataBO;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResultTool;
import com.rookiefly.quickstart.dubbo.remote.api.CityData;
import com.rookiefly.quickstart.dubbo.remote.api.CityRemoteService;
import com.rookiefly.quickstart.dubbo.service.CityService;
import org.apache.dubbo.apidocs.annotations.ApiDoc;
import org.apache.dubbo.apidocs.annotations.ApiModule;
import org.apache.dubbo.apidocs.annotations.RequestParam;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0")
@ApiModule(value = "城市数据服务", apiInterface = CityRemoteService.class, version = "1.0.0")
public class CityRemoteServiceImpl implements CityRemoteService {

    @Resource
    private CityService cityService;

    @Override
    @ApiDoc(value = "queryCityDataByCityId", version = "1.0.0", description = "根据城市ID查询城市数据", responseClassDescription = "城市数据bean")
    public RpcResult<CityData> queryCityDataByCityId(@RequestParam(value = "cityId", required = true) Long cityId) {
        CityDataBO cityDataBO = cityService.queryCityDataByCityId(cityId);
        CityData cityData = new CityData();
        BeanUtils.copyProperties(cityDataBO, cityData);
        return RpcResultTool.newSuccessRpcResult(cityData);
    }

    @Override
    @ApiDoc(value = "queryCityDataByType", version = "1.0.0", description = "根据类型查询城市数据", responseClassDescription = "城市数据列表")
    public RpcResult<List<CityData>> queryCityDataByType(@RequestParam(value = "type", required = true, description = "类型枚举，0：省，1：市，2：区，3：镇") Integer type) {
        List<CityDataBO> cityDataBOList = cityService.queryCityDataByType(type);
        List<CityData> cityDataList = cityDataBOList.stream().map(cityDataBO -> {
            CityData cityData = new CityData();
            BeanUtils.copyProperties(cityDataBO, cityData);
            return cityData;
        }).collect(Collectors.toList());
        return RpcResultTool.newSuccessRpcResult(cityDataList);
    }
}