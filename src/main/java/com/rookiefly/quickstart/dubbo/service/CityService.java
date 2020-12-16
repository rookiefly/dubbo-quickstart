package com.rookiefly.quickstart.dubbo.service;

import com.rookiefly.quickstart.dubbo.bo.CityDataBO;

import java.util.List;

public interface CityService {

    /**
     * 城市id查询城市
     *
     * @param cityId
     * @return
     */
    CityDataBO queryCityDataByCityId(Long cityId);

    /**
     * 分类id查询城市
     *
     * @param type 0：省，1：市，2：区，3：镇
     * @return
     */
    List<CityDataBO> queryCityDataByType(Integer type);
}