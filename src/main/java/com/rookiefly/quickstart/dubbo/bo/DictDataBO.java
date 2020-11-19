package com.rookiefly.quickstart.dubbo.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DictDataBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字典编码
     */
    private Long code;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典键值
     */
    private String value;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 是否可编辑
     */
    private Integer editable;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新操作者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 创建时间
     */
    private Date gmtCreated;
}