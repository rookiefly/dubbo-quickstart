package com.rookiefly.quickstart.dubbo.bo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DictTypeBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典名称
     */
    private String type;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 排序
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