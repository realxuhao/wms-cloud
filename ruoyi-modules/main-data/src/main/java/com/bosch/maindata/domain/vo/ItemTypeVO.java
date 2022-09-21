package com.bosch.maindata.domain.vo;

import lombok.Data;

@Data
public class ItemTypeVO {

    /** 物料类型代码 */
    private String code;

    /** 物料类型描述 */
    private String description;

    /** 所属部门 */
    private Long cellId;

    /** 所属部门名称 */
    private String cellDesc;

    /** 状态（1：启用，0：停用） */
    private Long status;
}
