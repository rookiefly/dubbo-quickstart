package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.bo.CityDataBO;
import com.rookiefly.quickstart.dubbo.service.CityService;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 城市查询服务
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;

    /**
     * 根据城市类型查询城市数据
     *
     * @param type 0：省，1：市，2：区，3：镇
     * @return
     */
    @GetMapping("/type/{type}")
    public CommonResponse queryCityDataByType(@PathVariable("type") @NotBlank(message = "城市类型不能为空") Integer type) {
        List<CityDataBO> dictDataBOList = cityService.queryCityDataByType(type);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        successResponse.setData(dictDataBOList);
        return successResponse;
    }

    /**
     * 根据城市编码查询城市数据
     *
     * @param cityId 城市id
     * @return
     */
    @GetMapping("/{cityId}")
    public CommonResponse queryCityDataByCode(@PathVariable("cityId") @NotNull(message = "城市ID不能为空") Long cityId) {
        CityDataBO cityDataBO = cityService.queryCityDataByCityId(cityId);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        successResponse.setData(cityDataBO);
        return successResponse;
    }
}
