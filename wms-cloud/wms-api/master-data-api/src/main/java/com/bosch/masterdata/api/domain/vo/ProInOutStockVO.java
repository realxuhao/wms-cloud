package com.bosch.masterdata.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProInOutStockVO {

    /**
     * 物料号
     */
    @ApiModelProperty(value = "material_nb")
    @Excel(name = "物料号")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "batch_nb")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 操作库存
     */
    @TableField(value = "operation_stock")
    @Excel(name = "期初数量")
    private BigDecimal operationStock;

    /**
     * 操作类型
     */
    @TableField(value = "operation_type")
//    @Excel(name = "操作类型",readConverterExp = "-1=期初,-2=期末,0=入库,1=销售出库,2=其他出库")
    private String operationType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")
    //@Excel(name = "创建时间")
    private String createTime;

    /**
     * 操作库存
     */
    @TableField(value = "bs")
    @Excel(name = "期末数量")
    private BigDecimal bs;

    /**
     * 操作类型
     */
    @TableField(value = "bc")
//    @Excel(name = "操作类型",readConverterExp = "-1=期初,-2=期末,0=入库,1=销售出库,2=其他出库")
    private String bt;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "bc")
    //@Excel(name = "创建时间")
    private String bc;
    /**
     * 操作库存
     */
    @TableField(value = "bs")
    @Excel(name = "入库数量")
    private BigDecimal cs;

    /**
     * 操作类型
     */
    @TableField(value = "bc")
//    @Excel(name = "操作类型",readConverterExp = "-1=期初,-2=期末,0=入库,1=销售出库,2=其他出库")
    private String ct;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "bc")
    //@Excel(name = "创建时间")
    private String cc;

    /**
     * 操作库存
     */
    @TableField(value = "bs")
    @Excel(name = "销售出库数量")
    private BigDecimal ds;

    /**
     * 操作类型
     */
    @TableField(value = "bc")
//    @Excel(name = "操作类型",readConverterExp = "-1=期初,-2=期末,0=入库,1=销售出库,2=其他出库")
    private String dt;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "bc")
    //@Excel(name = "创建时间")
    private String dc;
    /**
     * 操作库存
     */
    @TableField(value = "bs")
    @Excel(name = "其他出库数量")
    private BigDecimal es;

    /**
     * 操作类型
     */
    @TableField(value = "bc")
//    @Excel(name = "操作类型",readConverterExp = "-1=期初,-2=期末,0=入库,1=销售出库,2=其他出库")
    private String et;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "bc")
    //@Excel(name = "创建时间")
    private String ec;
}
