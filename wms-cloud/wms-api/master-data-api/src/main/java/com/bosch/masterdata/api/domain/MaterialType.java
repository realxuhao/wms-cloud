package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 物料类型对象 md_material_type
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Data
@TableName("md_material_type")
public class MaterialType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 物料类型代码 */
    @Excel(name = "物料类型代码")
    private String code;

    /** 物料类型描述 */
    @Excel(name = "物料类型描述")
    private String description;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private Long departmentId;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;


}
