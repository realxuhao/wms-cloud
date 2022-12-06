package com.bosch.binin.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MaterialInfoVO extends MaterialKanbanVO{

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;
}
