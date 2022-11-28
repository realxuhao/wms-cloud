package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 14:15
 * @description:
 */
@Data
public class BinInQueryDTO extends PageDomain {

    /**
     * sscc
     */
    @ApiModelProperty(value = "plantNb")
    private String plantNb;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /**
     * 实际库位编码
     */
    @ApiModelProperty(value = "实际库位编码")
    private String actualBinCode;

    /**
     * 托盘编码
     */
    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;


    /**
     * 状态(0：待入库,1:已入库)
     */
    @ApiModelProperty(value = "状态(0：待入库,1:已入库)")
    private Integer status;


    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型")
    private String moveType;

}
