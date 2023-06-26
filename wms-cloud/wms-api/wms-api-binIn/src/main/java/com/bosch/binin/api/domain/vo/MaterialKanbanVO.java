package com.bosch.binin.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialKanbanVO extends BaseEntity {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @ExcelProperty(value = "订单号")
    @Excel(name = "生产需求号")
    private String orderNumber;

    /**
     * 源工厂编码
     */
    @ExcelProperty(value = "源工厂编码")
    @ApiModelProperty(value = "源工厂编码")
    @Excel(name = "源工厂编码")
    private String factoryCode;
    /**
     * 源工厂id
     */
    @ExcelProperty(value = "源工厂id")
    @ApiModelProperty(value = "源工厂id")
    private Long factoryId;

    /**
     * 源仓库编码
     */
    @ExcelProperty(value = "源仓库编码")
    @ApiModelProperty(value = "源仓库编码")
    @Excel(name = "源仓库编码")
    private String wareCode;
    /**
     * 源仓库id
     */
    @ExcelProperty(value = "源仓库id")
    @ApiModelProperty(value = "源仓库id")
    private Long wareId;

    /**
     * 源存储区编码
     */
    @ExcelProperty(value = "源存储区编码")
    @ApiModelProperty(value = "源存储区编码")
    @Excel(name = "源存储区编码")
    private String areaCode;
    /**
     * 源存储区id
     */
    @ExcelProperty(value = "源存储区id")
    @ApiModelProperty(value = "源存储区id")
    private Long areaId;

    /**
     * 源库位编码
     */
    @ExcelProperty(value = "源库位编码")
    @ApiModelProperty(value = "源库位编码")
    @Excel(name = "源库位编码")
    private String binCode;
    /**
     * 源库位id
     */
    @ExcelProperty(value = "源库位id")
    @ApiModelProperty(value = "源库位id")
    private Long binId;

    /**
     * 料号
     */
    @ExcelProperty(value = "料号")
    @ApiModelProperty(value = "料号")
    @Excel(name = "物料号")
    private String materialCode;


    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 料号
     */
    @ExcelProperty(value = "物料名称")
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    @ApiModelProperty(value = "数量")
    @Excel(name = "数量")
    private Double quantity;
    /**
     * 移动类型
     */
    @ExcelProperty(value = "移动类型")
    @Excel(name = "移动类型",readConverterExp = "0=收货,1=入库,2=上架,3=生产叫料")
    @ApiModelProperty(value = "移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）")
    private String moveType;

    /**
     * SSCC码
     */
    @ExcelProperty(value = "SSCC码")
    @ApiModelProperty(value = "SSCC码")
    @Excel(name = "SSCC")
    private String ssccNumber;
    /**
     * 需求cell
     */
    @ExcelProperty(value = "需求cell")
    @Excel(name = "cell")
    @ApiModelProperty(value = "需求cell")
    private String cell;
    /**
     * 动作类型（整托下架、拆托下架）
     */
    @ExcelProperty(value = "动作类型（整托下架、拆托下架）")
    @Excel(name = "动作类型",readConverterExp = "0=整托下架,1=拆托下架")
    @ApiModelProperty(value = "动作类型（0：整托下架 1：拆托下架）")
    private Integer type;
    /**
     * 状态
     */
    @Excel(name = "状态",readConverterExp = "-1=取消任务,0=待下发,1=待下架,2=外库待发运,3=产线待收货,4=主库待收货,5=待上架,6=产线已收货,7=完成")
    @ExcelProperty(value = "状态")
    @ApiModelProperty(value = "(-1,\"取消任务\"),\n" +
            "(0, \"待下发\"),\n" +
            "(1, \"待下架\"),\n" +
            "(2, \"外库已下架\"),\n" +
            "(3, \"主库已下架\"),\n" +
            "(4, \"主库待收货\"),\n" +
            "(5, \"主库入库\"),\n" +
            "(6, \"主库上架\"),\n" +
            " (7, \"产线收货\");")
    private Integer status;
    /**
     * 状态描述
     */
    @ExcelProperty(value = "状态描述")
    @ApiModelProperty(value = "状态描述")
    private String typeDesc;
    /**
     * deleteFlag
     */
    @ExcelProperty(value = "deleteFlag")
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;
}
