package com.rookiefly.quickstart.dubbo.facade.impl;

import com.rookiefly.quickstart.dubbo.facade.api.DictFacadeService;
import com.rookiefly.quickstart.dubbo.model.DictData;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

@DubboService(version = "1.0.0")
public class DictFacadeServiceImpl implements DictFacadeService {

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