package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductFrameDTO extends PageDomain {
    /** id */
    @ExcelIgnore
    @ApiModelProperty(value = "ID")
    private Long id;

    /** 物料id */
    @ExcelIgnore
    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    /** 物料code */
    @ExcelProperty(value = "成品代码")
    @ApiModelProperty(value = "成品代码")
    private String materialCode;

    /** 可用跨编码 */
    @ExcelProperty(value = "可用跨编码")
    @ApiModelProperty(value = "可用跨编码")
    private String frameTypeCode;

    /** 分配至该库位的优先级 */
    @ExcelProperty(value = "优先级")
    @ApiModelProperty(value = "优先级")
    private Long priorityLevel;

    /** 状态（1：启用，0：停用） */
    @ExcelIgnore
    @ApiModelProperty(value = "状态（1：启用，0：停用）")
    private Long status;
}
