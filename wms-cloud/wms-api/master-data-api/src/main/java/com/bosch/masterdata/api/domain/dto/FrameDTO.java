package com.bosch.masterdata.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FrameDTO extends PageDomain {

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * id
     */
    @ExcelProperty(value = "区域id")
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    @ExcelProperty(value = "存储区编码")
    @ApiModelProperty(value = "区域Code")
    private String areaCode;
    /**
     * 跨编码
     */
    @ExcelProperty(value = "跨编码")
    @ApiModelProperty(value = "跨编码")
    private String code;

    /**
     * 跨类型编码
     */
    @ExcelProperty(value = "跨类型编码")
    @ApiModelProperty(value = "跨类型编码")
    private String typeCode;

    /**
     * 跨名称
     */
    @ExcelProperty(value = "跨名称")
    @ApiModelProperty(value = "跨名称")
    private String name;

    /**
     * 宽度
     */
    @ExcelProperty(value = "宽度[CM]")
    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    /**
     * 承重
     */
    @ExcelProperty(value = "承重[KG]")
    @ApiModelProperty(value = "承重")
    private BigDecimal bearWeight;

    /**
     * 高度
     */
    @ExcelProperty(value = "高度[CM]")
    @ApiModelProperty(value = "高度")
    private BigDecimal height;

    @ApiModelProperty(value = "wareCode")
    private String wareCode;

    @ApiModelProperty(value = "frameType")
    private String frameType;

}
