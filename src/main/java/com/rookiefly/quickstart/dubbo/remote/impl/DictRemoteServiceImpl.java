package com.rookiefly.quickstart.dubbo.remote.impl;

import com.rookiefly.quickstart.dubbo.bo.DictDataBO;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResultTool;
import com.rookiefly.quickstart.dubbo.remote.api.DictData;
import com.rookiefly.quickstart.dubbo.remote.api.DictRemoteService;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0")
public class DictRemoteServiceImpl implements DictRemoteService {

    @Resource
    private DictService dictService;

    @Override
    public RpcResult<DictData> queryDictDataByCode(Long code) {
        DictDataBO dictDataBO = dictService.queryDictDataByCode(code);
        DictData dictData = new DictData();
        BeanUtils.copyProperties(dictDataBO, dictData);
        return RpcResultTool.newSuccessRpcResult(dictData);
    }

    @Override
    public RpcResult<List<DictData>> queryDictDataByType(String type) {
        List<DictDataBO> dictDataBOList = dictService.queryDictDataByType(type);
        List<DictData> dictDataList = dictDataBOList.stream().map(dictDataBO -> {
            DictData dictData = new DictData();
            BeanUtils.copyProperties(dictDataBO, dictData);
            return dictData;
        }).collect(Collectors.toList());
        return RpcResultTool.newSuccessRpcResult(dictDataList);
    }
}