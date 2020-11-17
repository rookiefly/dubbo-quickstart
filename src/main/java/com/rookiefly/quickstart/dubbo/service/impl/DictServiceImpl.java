package com.rookiefly.quickstart.dubbo.service.impl;

import com.rookiefly.quickstart.dubbo.mapper.DictMapper;
import com.rookiefly.quickstart.dubbo.model.DictData;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    @Cacheable(value = "dictCache", key = "targetClass + methodName + #code")
    public DictData queryDictDataByCode(Long code) {
        return dictMapper.queryDictDataByCode(code);
    }

    @Override
    @Cacheable(value = "dictCache", key = "targetClass + methodName + #type")
    public List<DictData> queryDictDataByType(String type) {
        return dictMapper.queryDictDataByType(type);
    }
}
