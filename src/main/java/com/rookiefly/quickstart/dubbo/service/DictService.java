package com.rookiefly.quickstart.dubbo.service;

import com.rookiefly.quickstart.dubbo.model.DictData;

import java.util.List;

public interface DictService {

    DictData queryDictDataByCode(Long code);

    List<DictData> queryDictDataByType(String type);
}