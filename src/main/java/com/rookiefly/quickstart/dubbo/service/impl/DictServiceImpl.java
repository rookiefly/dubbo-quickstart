package com.rookiefly.quickstart.dubbo.service.impl;

import com.rookiefly.quickstart.dubbo.bo.DictDataBO;
import com.rookiefly.quickstart.dubbo.mapper.DictMapper;
import com.rookiefly.quickstart.dubbo.model.DictDataDO;
import com.rookiefly.quickstart.dubbo.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    @Cacheable(value = "dictCache", keyGenerator = "myKeyGenerator")
    public DictDataBO queryDictDataByCode(Long code) {
        DictDataDO dictDataDO = dictMapper.queryDictDataByCode(code);
        DictDataBO dictDataBO = new DictDataBO();
        BeanUtils.copyProperties(dictDataDO, dictDataBO);
        return dictDataBO;
    }

    @Override
    @Cacheable(value = "dictCache", keyGenerator = "myKeyGenerator")
    public List<DictDataBO> queryDictDataByType(String type) {
        List<DictDataDO> dictDataDOList = dictMapper.queryDictDataByType(type);
        return dictDataDOList.stream().map(dictDataDO -> {
            DictDataBO dictDataBO = new DictDataBO();
            BeanUtils.copyProperties(dictDataDO, dictDataBO);
            return dictDataBO;
        }).collect(Collectors.toList());
    }
}
