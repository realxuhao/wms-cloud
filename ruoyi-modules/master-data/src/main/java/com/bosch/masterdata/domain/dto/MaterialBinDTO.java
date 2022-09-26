package com.bosch.masterdata.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialBinDTO extends PageDomain {
    /** id */
    @ApiModelProperty(value = "id")
    private Long id;

    /** 物料id */
    @ApiModelProperty(value = "物料id")
    private Long materialId;

    /** 物料code */
    @ApiModelProperty(value = "物料code")
    private String materialCode;

    /** 库位id */
    @ApiModelProperty(value = "库位id")
    private Long binId;

    /** 库位code */
    @ApiModelProperty(value = "库位code")
    private String binCode;

    /** 分配至该库位的优先级 */
    @ApiModelProperty(value = "分配至该库位的优先级")
    private Long priorityLevel;

}
