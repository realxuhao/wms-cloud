package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaDTO extends PageDomain {
    /** id */
    @ExcelProperty(value = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    /** 仓库id */
    @ApiModelProperty(value = "仓库id")
    @ExcelProperty(value = "仓库id")
    private Long wareId;

    /** 仓库Code */
    @ApiModelProperty(value = "仓库Code")
    @ExcelProperty(value = "仓库Code")
    private String wareCode;

    /** 存储区编码 */
    @ApiModelProperty(value = "存储区编码")
    @ExcelProperty(value = "存储区编码")
    private String code;

    /** 存储区名称 */
    @ApiModelProperty(value = "存储区名称")
    @ExcelProperty(value = "存储区名称")
    private String name;

}
