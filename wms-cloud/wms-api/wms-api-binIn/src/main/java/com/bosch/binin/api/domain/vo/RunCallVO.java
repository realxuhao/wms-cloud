package com.bosch.binin.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-12 10:37
 **/
@Data
public class RunCallVO  {


    private Long callId;
    /**
     * 订单号
     */
    @Excel(name = "订单号")
    @ExcelProperty(value = "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNb;

    /**
     * 物料号
     */
    @Excel(name = "物料号")
    @ExcelProperty(value = "物料号")
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    @ExcelProperty(value = "物料名称")
    @ApiModelProperty(value = "物料名称")
    private String materialName;


    /**
     * 需求量
     */
    @Excel(name = "数量")
    @ExcelProperty(value = "数量")
    @ApiModelProperty(value = "需求量")
    private Double quantity;

    /**
     * 主库库存量
     */
    @Excel(name = "主库库存量")
    @ExcelProperty(value = "主库库存量")
    @ApiModelProperty(value = "主库库存量")
    private Double mainStock;


    /**
     * 外库库存量
     */
    @Excel(name = "外库库存量")
    @ExcelProperty(value = "外库库存量")
    @ApiModelProperty(value = "外库库存量")
    private Double outStock;

    /**
     * 在途量
     */
    @Excel(name = "在途量")
    @ExcelProperty(value = "在途量")
    @ApiModelProperty(value = "在途量")
    private Double inTransStock;

    /**
     * 已下发量
     */
    @Excel(name = "已下发量")
    @ApiModelProperty(value = "已下发量")
    private Double issuedQuantity;

    private boolean isEnough;


    @ApiModelProperty(value = "推荐移库量")
    private Double recommendShiftQuantity;





}
