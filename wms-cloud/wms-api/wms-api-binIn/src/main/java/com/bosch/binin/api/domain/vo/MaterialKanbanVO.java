package com.bosch.binin.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String orderNumber;

    /**
     * 源工厂编码
     */
    @ExcelProperty(value = "源工厂编码")
    @ApiModelProperty(value = "源工厂编码")
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
    private String materialCode;
    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    @ApiModelProperty(value = "数量")
    private Double quantity;
    /**
     * 移动类型
     */
    @ExcelProperty(value = "移动类型")
    @ApiModelProperty(value = "移动类型 （0： 收货，1：入库，2：上架，3：生产叫料）")
    private String moveType;

    /**
     * SSCC码
     */
    @ExcelProperty(value = "SSCC码")
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;
    /**
     * 需求cell
     */
    @ExcelProperty(value = "需求cell")
    @ApiModelProperty(value = "需求cell")
    private String cell;
    /**
     * 动作类型（整托下架、拆托下架）
     */
    @ExcelProperty(value = "动作类型（整托下架、拆托下架）")
    @ApiModelProperty(value = "动作类型（0：整托下架 1：拆托下架）")
    private Integer type;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    @ApiModelProperty(value = "状态：0：待下发（源工厂为7751的没有） 1：已下发 2：已下架（下架时PDA扫描SSCC）、配送完成（产线PDA扫描接收）")
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
