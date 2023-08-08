package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProductReceiveVO {

    /**
     * id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;


    /**
     * 仓库
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;


    @ApiModelProperty(value = "仓库")
    private String wareCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * BBD(过期时间)
     */
    @ApiModelProperty(value = "BBD过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    @ApiModelProperty(value = "成产批次号")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Double quantity;

    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromProdOrder;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    /**
     * 删除标记1:可用，0:删除
     */
    @ApiModelProperty(value = "删除标记1:可用，0:删除")
    private Integer deleteFlag;

    /**
     * 物料流转状态:0:待入库，1：已入库
     */
    @ApiModelProperty(value = "物料流转状态:0:待入库，1：已入库")
    private Integer status;

    private Double totalStockSum;

}
