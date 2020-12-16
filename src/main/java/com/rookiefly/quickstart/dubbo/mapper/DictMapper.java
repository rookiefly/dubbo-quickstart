package com.rookiefly.quickstart.dubbo.mapper;

import com.rookiefly.quickstart.dubbo.model.DictDataDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictMapper {

    /**
     * code查询字典
     *
     * @param code
     * @return
     */
    DictDataDO queryDictDataByCode(Long code);

    /**
     * 分类id查询字典列表
     *
     * @param type
     * @return
     */
    List<DictDataDO> queryDictDataByType(String type);

}
