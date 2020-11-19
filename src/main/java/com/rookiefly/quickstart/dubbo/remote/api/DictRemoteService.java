package com.rookiefly.quickstart.dubbo.remote.api;

import java.util.List;

public interface DictRemoteService {

    DictData queryDictDataByCode(Long code);

    List<DictData> queryDictDataByType(String type);
}