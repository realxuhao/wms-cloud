package com.bosch.masterdata.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FrameDTO extends PageDomain {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * id
     */
    @ApiModelProperty(value = "区域id")
    private Long areaId;

    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    private String code;

    /**
     * 跨名称
     */
    @ApiModelProperty(value = "跨名称")
    private String name;

    /**
     * 宽度
     */
    @ApiModelProperty(value = "宽度")
    private BigDecimal width;

    /**
     * 承重
     */
    @ApiModelProperty(value = "承重")
    private BigDecimal bearWeight;

}
