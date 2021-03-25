package com.rookiefly.quickstart.dubbo.controller;

import com.rookiefly.quickstart.dubbo.bo.DictDataBO;
import com.rookiefly.quickstart.dubbo.service.DictService;
import com.rookiefly.quickstart.dubbo.vo.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

/**
 * 字典查询服务
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 根据字典类型查询字典列表明细数据
     *
     * @param type
     * @return
     */
    @GetMapping("/type/{type}")
    public CommonResponse queryDictDataByType(@PathVariable("type") @NotBlank(message = "字典类型不能为空") String type) {
        List<DictDataBO> dictDataBOList = dictService.queryDictDataByType(type);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        HashMap<Object, Object> data = new HashMap<>();
        successResponse.setData(data);
        data.put("dictDataList", dictDataBOList);
        return successResponse;
    }

    /**
     * 根据字典code查询字典明细数据
     *
     * @param code
     * @return
     */
    @GetMapping("/{code}")
    public CommonResponse queryDictDataByCode(@PathVariable("code") @NotNull(message = "字典编码不能为空") Long code) {
        DictDataBO dictDataBO = dictService.queryDictDataByCode(code);
        CommonResponse successResponse = CommonResponse.newSuccessResponse();
        HashMap<Object, Object> data = new HashMap<>();
        successResponse.setData(data);
        data.put("dictData", dictDataBO);
        return successResponse;
    }
}
