package com.rookiefly.quickstart.dubbo.service.impl;

import com.rookiefly.quickstart.dubbo.bo.CityDataBO;
import com.rookiefly.quickstart.dubbo.mapper.CityMapper;
import com.rookiefly.quickstart.dubbo.model.CityDataDO;
import com.rookiefly.quickstart.dubbo.service.CityService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    @Resource
    private CityMapper cityMapper;

    @Override
    @Cacheable(value = "cityCache", keyGenerator = "myKeyGenerator")
//    @CacheEvict(cacheNames = "cityCache", keyGenerator = "myKeyGenerator")
    public CityDataBO queryCityDataByCityId(Long cityId) {
        CityDataDO cityDataDO = cityMapper.queryCityDataByCityId(cityId);
        CityDataBO cityDataBO = new CityDataBO();
        BeanUtils.copyProperties(cityDataDO, cityDataBO);
        return cityDataBO;
    }

    @Override
    @Cacheable(value = "cityCache", keyGenerator = "myKeyGenerator")
    public List<CityDataBO> queryCityDataByType(Integer type) {
        List<CityDataDO> cityDataDOList = cityMapper.queryCityDataByType(type);
        return cityDataDOList.stream().map(cityDataDO -> {
            CityDataBO cityDataBO = new CityDataBO();
            BeanUtils.copyProperties(cityDataDO, cityDataBO);
            return cityDataBO;
        }).collect(Collectors.toList());
    }
}