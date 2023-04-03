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
    private Integer id;


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
     * Cell
     */
    @ExcelProperty(value = "Cell")
    private String cell;

    /**
     * 总数量/托
     */
    @ExcelProperty(value = "总数量/托")
    private String totalQuantity;

    /**
     * 最小包装数量/托
     */
    @ExcelProperty(value = "最小包装数量/托")
    private String minQuantity;

    @ExcelProperty(value = "成品名称")
    private String productName;
}