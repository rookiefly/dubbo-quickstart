package com.rookiefly.quickstart.dubbo.remote.api;

import lombok.Getter;
import lombok.Setter;
import org.apache.dubbo.apidocs.annotations.ResponseProperty;

import java.io.Serializable;

@Getter
@Setter
public class CityData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市编号
     */
    @ResponseProperty(value = "城市编号")
    private Long id;

    /**
     * 上级ID
     */
    @ResponseProperty(value = "上级ID")
    private Long pid;

    /**
     * 层级深度；0：省，1：市，2：区，3：镇
     */
    @ResponseProperty(value = "层级深度；0：省，1：市，2：区，3：镇")
    private Integer deep;

    /**
     * 城市名称
     */
    @ResponseProperty(value = "城市名称")
    private String name;

    /**
     * name的拼音前缀，取的是pinyin第一个字母
     */
    @ResponseProperty(value = "name的拼音前缀")
    private String pinyinPrefix;

    /**
     * name的完整拼音
     */
    @ResponseProperty(value = "name的完整拼音")
    private String pinyin;

    /**
     * 数据源原始的编号；如果是添加的数据，此编号为0
     */
    @ResponseProperty(value = "数据源原始的编号；如果是添加的数据，此编号为0")
    private Long extId;

    /**
     * 数据源原始的名称，为未精简的名称
     */
    @ResponseProperty(value = "数据源原始的名称，为未精简的名称")
    private String extName;
}