package com.rookiefly.quickstart.dubbo.facade.api;

import com.rookiefly.quickstart.dubbo.model.DictData;

import java.util.List;

public interface DictFacadeService {

    DictData queryDictDataByCode(Long code);

    List<DictData> queryDictDataByType(String type);
}