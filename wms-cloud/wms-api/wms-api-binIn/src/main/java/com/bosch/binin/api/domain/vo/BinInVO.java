package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
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
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @Excel(name = "工厂")
    private String plantNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNb;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    @Excel(name = "sscc")
    private String ssccNumber;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;


    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间")
    private Date expireDate;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    @Excel(name = "仓库编码")
    private String wareCode;

    /**
     * 推荐库位编码
     */
    @ApiModelProperty(value = "推荐库位编码")
    @Excel(name = "推荐库位编码")
    private String recommendBinCode;

    /**
     * 跨id
     */
    @ApiModelProperty(value = "跨id")
    private Long frameId;

    /**
     * 跨code
     */
    @ApiModelProperty(value = "跨code")
    @Excel(name = "跨code")
    private String frameCode;

    /**
     * 实际库位编码
     */
    @ApiModelProperty(value = "实际库位编码")
    @Excel(name = "实际库位编码")
    private String actualBinCode;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    @Excel(name = "数量")
    private Double quantity;

    /**
     * 托盘编码
     */
    @ApiModelProperty(value = "托盘编码")
    @Excel(name = "托盘编码")
    private String palletCode;

    @ApiModelProperty(value = "区域编码")
    @Excel(name = "区域编码")
    private String areaCode;

    /**
     * 托盘类型
     */
    @ApiModelProperty(value = "托盘类型")
    @Excel(name = "托盘类型")
    private String palletType;

    /**
     * 状态(0：待上架,1:已上架)
     */
    @ApiModelProperty(value = "status，状态(0：待上架,1:已上架)")
    @Excel(name = "状态",readConverterExp = "0=待上架,1:已上架")
    private Integer status;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人")
    private String createBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private Date createTime;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "入库人")
    @Excel(name = "入库人")
    private String receiveBy;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "更新人")
    @Excel(name = "更新人")
    private String updateBy;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间")
    private Date updateTime;

    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型")
    private String moveType;
}
