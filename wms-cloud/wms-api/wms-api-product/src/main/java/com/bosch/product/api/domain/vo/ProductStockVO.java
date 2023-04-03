package com.bosch.product.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:01
 **/
@Data
public class ProductStockVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;


    /**
     * 生产订单号
     */
    @ApiModelProperty(value = "生产订单号")
    private String fromProdOrder;


    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;


    /**
     * 存储区编码
     */
    @ApiModelProperty(value = "存储区编码")
    private String areaCode;

    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    private String frameCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(value = "库位编码")
    private String binCode;

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
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "库存量")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    private Double availableStock;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 质检状态
     */
    @ApiModelProperty(value = "质检状态")
    private String qualityStatus;

    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    /**
     * 整托标记，0：整托，1：零托
     */
    @ApiModelProperty(value = "整托标记，0：整托，1：零托")
    private int wholeFlag;

    /**
     * 变更状态（0：未变更，1：已变更）
     */
    @ApiModelProperty(value = "变更状态（0：未变更，1：已变更）")
    private Integer changeStatus;


    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 单位
     */
    @ApiModelProperty(value = "unit")
    private String unit;

    private String cell;

    @ApiModelProperty(value = "成产批次号")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date productionDate;

    @ApiModelProperty(value = "上架标识，0:不需要上架，1:待上架，2：已上架")
    private Integer binInFlag;

    @ApiModelProperty(value = "推荐库位编码")
    private String recommendBinCode;

}
