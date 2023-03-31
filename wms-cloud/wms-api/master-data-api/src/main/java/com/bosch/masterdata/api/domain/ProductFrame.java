package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 物料库位分配策略对象 md_material_bin
 * 
 * @author xuhao
 * @date 2022-09-22
 */
@Data
@TableName("md_product_frame")
public class ProductFrame extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 物料code */
    @ApiModelProperty(value = "物料code")
    private String materialCode;

    /** 可用跨编码 */
    @ApiModelProperty(value = "可用跨编码")
    private String frameTypeCode;



    /** 分配至该库位的优先级 */
    @Excel(name = "分配至该库位的优先级")
    private Long priorityLevel;

    /** 状态（1：启用，0：停用） */
    @Excel(name = "状态", readConverterExp = "1=：启用，0：停用")
    private Long status;



}
