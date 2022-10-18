package com.bosch.binin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:24
 * @description: 库位分配VO
 */
@Data
public class BinAllocationVO {

    @ApiModelProperty(value = "mesBarCode")
    private String mesBarCode;

    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    @ApiModelProperty(value = "物料号")
    private String materialNb;

    @ApiModelProperty(value = "托盘类型")
    private String palletType;

    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    @ApiModelProperty(value = "推荐库位")
    private String recommendBinCode;


}
