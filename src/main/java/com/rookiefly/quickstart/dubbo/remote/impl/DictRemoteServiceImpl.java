package com.rookiefly.quickstart.dubbo.remote.impl;

import com.rookiefly.quickstart.dubbo.bo.DictDataBO;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResult;
import com.rookiefly.quickstart.dubbo.framework.rpc.RpcResultTool;
import com.rookiefly.quickstart.dubbo.remote.api.DictData;
import com.rookiefly.quickstart.dubbo.remote.api.DictRemoteService;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.apache.dubbo.apidocs.annotations.ApiDoc;
import org.apache.dubbo.apidocs.annotations.ApiModule;
import org.apache.dubbo.apidocs.annotations.RequestParam;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@DubboService(version = "1.0.0")
@ApiModule(value = "数据字典服务", apiInterface = DictRemoteService.class, version = "1.0.0")
public class DictRemoteServiceImpl implements DictRemoteService {

    @Resource
    private DictService dictService;

    @Override
    @ApiDoc(value = "queryDictDataByCode", version = "1.0.0", description = "根据字典code查询字典数据", responseClassDescription = "字典数据bean")
    public RpcResult<DictData> queryDictDataByCode(@RequestParam(value = "code", required = true) Long code) {
        DictDataBO dictDataBO = dictService.queryDictDataByCode(code);
        DictData dictData = new DictData();
        BeanUtils.copyProperties(dictDataBO, dictData);
        return RpcResultTool.newSuccessRpcResult(dictData);
    }

    @Override
    @ApiDoc(value = "queryDictDataByType", version = "1.0.0", description = "根据字典类型查询字典数据", responseClassDescription = "字典数据列表")
    public RpcResult<List<DictData>> queryDictDataByType(@RequestParam(value = "type", required = true, description = "例子：sys_user_sex") String type) {
        List<DictDataBO> dictDataBOList = dictService.queryDictDataByType(type);
        List<DictData> dictDataList = dictDataBOList.stream().map(dictDataBO -> {
            DictData dictData = new DictData();
            BeanUtils.copyProperties(dictDataBO, dictData);
            return dictData;
        }).collect(Collectors.toList());
        return RpcResultTool.newSuccessRpcResult(dictDataList);
    }
}