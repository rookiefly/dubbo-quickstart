package com.rookiefly.quickstart.dubbo.service;

import com.rookiefly.quickstart.dubbo.bo.DictDataBO;

import java.util.List;

public interface DictService {

    DictDataBO queryDictDataByCode(Long code);

    List<DictDataBO> queryDictDataByType(String type);
}