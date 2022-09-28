package com.bosch.masterdata.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WareDTO extends PageDomain {
    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 仓库编码
     */
    @ExcelProperty(value = "仓库编码")
    @ApiModelProperty(value = "仓库编码")
    private String code;

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    @ApiModelProperty(value = "仓库名称")
    private String name;

    /**
     * 仓库地址
     */
    @ExcelProperty(value = "仓库地址")
    @ApiModelProperty(value = "仓库地址")
    private String location;

    /**
     * 备注
     */
    @ExcelProperty(value = "")
    @ApiModelProperty(value = "备注")
    private String remark;

}
