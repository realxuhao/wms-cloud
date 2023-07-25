package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

/**
 * 成品包装量
 * @TableName md_product_packaging
 */
@Data
public class MdProductPackagingDTO extends PageDomain {
    /**
     * id
     */
    @ExcelIgnore
    @TableId(type = IdType.AUTO)
    private Long id;


    /**
     * 状态（1：启用，0：停用）
     */
    @ExcelIgnore
    private Integer status;

    /**
     * 备注
     */
    @ExcelIgnore
    private String remark;

    /**
     * 删除标记1：删除，0:可用
     */
    @ExcelIgnore
    private Integer deleteFlag;

    /**
     * 成品料号
     */
    @ExcelProperty(value = "成品料号")
    private String productNo;

    /**
     * type
     */
    @ExcelProperty(value = "类别")
    private String type;

    @ExcelProperty(value = "名称")
    private String productName;

    /**
     * Cell
     */
    @ExcelProperty(value = "Cell")
    private String cell;

    /**
     * 运输单位(Tr)
     */
    @ExcelProperty(value = "运输单位(Tr)")
    private String transportUnit;

    /**
     * 箱 Tr 对应包装规格
     */
    @ExcelProperty(value = "箱 Tr 对应包装规格")
    private Double boxSpecification;

    /**
     * 标准 Tr/托
     */
    @ExcelProperty(value = "标准 Tr/托")
    private String standardUnits;

    /**
     * 重量（Tr）
     */
    @ExcelProperty(value = "重量(Tr)")
    private String weight;

    /**
     * 体积 (Tr)
     */
    @ExcelProperty(value = "体积 (Tr)")
    private String volume;
}