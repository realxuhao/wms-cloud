package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaDTO extends PageDomain {
    /** id */
    @ExcelIgnore
    @ApiModelProperty(value = "id")
    private Long id;

    /** 仓库id */
    @ApiModelProperty(value = "仓库id")
    @ExcelIgnore
    private Long wareId;

    /** 仓库Code */
    @ApiModelProperty(value = "仓库编码")
    @ExcelProperty(value = "仓库编码")
    private String wareCode;

    /** 存储区编码 */
    @ApiModelProperty(value = "存储区编码")
    @ExcelProperty(value = "存储区编码")
    private String code;

    /** 存储区名称 */
    @ApiModelProperty(value = "存储区名称")
    @ExcelProperty(value = "存储区名称")
    private String name;


    /** 存储区类型 */
    @ExcelIgnore
    @ApiModelProperty(value = "存储区类型")
    private Integer areaType;


    /** 存储区类型 */
    @ApiModelProperty(value = "存储区类型")
    @ExcelProperty(value = "存储区类型")
    private String areaTypeDesc;

}
