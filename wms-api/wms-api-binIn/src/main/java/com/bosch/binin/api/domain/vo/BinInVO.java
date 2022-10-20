package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:24
 * @description: 上架VO
 */
@Data
public class BinInVO {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    private String ssccNumber;

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
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /**
     * 推荐库位编码
     */
    @ApiModelProperty(value = "推荐库位编码")
    private String recommendBinCode;

    /**
     * 实际库位编码
     */
    @ApiModelProperty(value = "实际库位编码")
    private String actualBinCode;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer quantity;

    /**
     * 托盘编码
     */
    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    /**
     * 托盘类型
     */
    @ApiModelProperty(value = "托盘类型")
    private String palletType;

    /**
     * 状态(0：待上架,1:已上架)
     */
    @ApiModelProperty(value = "status，状态(0：待上架,1:已上架)")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
