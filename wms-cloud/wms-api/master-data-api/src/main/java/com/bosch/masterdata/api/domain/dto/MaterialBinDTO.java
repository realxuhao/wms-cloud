package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialBinDTO extends PageDomain {
    /** id */
    @ExcelProperty( "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /** 物料id */
    @ExcelProperty( "物料id")
    @ApiModelProperty(value = "物料id")
    private Long materialId;

    /** 物料code */
    @ExcelProperty( "物料代码")
    @ApiModelProperty(value = "物料code")
    private String materialCode;


    /** 可用跨编码 */
    @ExcelProperty( "可用跨编码")
    @ApiModelProperty(value = "可用跨编码")
    private String frameTypeCode;

    /** 分配至该库位的优先级 */
    @ExcelProperty( "优先级")
    @ApiModelProperty(value = "分配至该库位的优先级")
    private Long priorityLevel;

}
