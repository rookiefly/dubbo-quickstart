package com.rookiefly.quickstart.dubbo.mapper;

import com.rookiefly.quickstart.dubbo.model.CityDataDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {

    /**
     * 城市id查询城市
     *
     * @param cityId
     * @return
     */
    CityDataDO queryCityDataByCityId(Long cityId);

    /**
     * 分类id查询城市
     *
     * @param type 0：省，1：市，2：区，3：镇
     * @return
     */
    List<CityDataDO> queryCityDataByType(Integer type);

}
