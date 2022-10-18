package com.bosch.binin.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:24
 * @description: 上架VO
 */
public class BinInVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "materialNb")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "materialName")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "batchNb")
    private String batchNb;


    /**
     * 过期时间
     */
    @ApiModelProperty(value = "expireDate")
    private Date expireDate;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "wareCode")
    private String wareCode;

    /**
     * 推荐库位编码
     */
    @ApiModelProperty(value = "recommendBinCode")
    private String recommendBinCode;

    /**
     * 实际库位编码
     */
    @ApiModelProperty(value = "actualBinCode")
    private String actualBinCode;

    /**
     * 数量
     */
    @ApiModelProperty(value = "quantity")
    private Integer quantity;

    /**
     * 托盘编码
     */
    @ApiModelProperty(value = "palletCode")
    private String palletCode;

    /**
     * 托盘类型
     */
    @ApiModelProperty(value = "palletType")
    private String palletType;

    /**
     * 状态(0：待入库,1:已入库)
     */
    @ApiModelProperty(value = "state")
    private Integer state;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "createUser")
    private String createUser;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "creteTime")
    private Date creteTime;
}
