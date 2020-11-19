package com.rookiefly.quickstart.dubbo.remote.impl;

import com.rookiefly.quickstart.dubbo.remote.api.DictRemoteService;
import com.rookiefly.quickstart.dubbo.model.DictData;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(version = "1.0.0")
public class DictRemoteServiceImpl implements DictRemoteService {

    @Resource
    private DictService dictService;

    @Override
    public DictData queryDictDataByCode(Long code) {
        return dictService.queryDictDataByCode(code);
    }

    @Override
    public List<DictData> queryDictDataByType(String type) {
        return dictService.queryDictDataByType(type);
    }
}