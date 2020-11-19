package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.service.DictService;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/type/{type}")
    public CommonResponse queryDictDataByType(@PathVariable("type") String type) {
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        HashMap<Object, Object> data = new HashMap<>();
        successResponse.setData(data);
        data.put("dictDataList", dictService.queryDictDataByType(type));
        return successResponse;
    }

    @GetMapping("/{code}")
    public CommonResponse queryDictDataByCode(@PathVariable("code") Long code) {
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        HashMap<Object, Object> data = new HashMap<>();
        successResponse.setData(data);
        data.put("dictData", dictService.queryDictDataByCode(code));
        return successResponse;
    }
}
