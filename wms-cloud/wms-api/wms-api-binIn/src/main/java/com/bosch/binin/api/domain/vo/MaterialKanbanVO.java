package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class MaterialKanbanVO extends BaseEntity {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    /**
     * 源工厂编码
     */
    @ApiModelProperty(value = "源工厂编码")
    private String factoryCode;
    /**
     * 源工厂id
     */
    @ApiModelProperty(value = "id")
    private Long factoryId;

    /**
     * 源仓库编码
     */
    @ApiModelProperty(value = "源仓库编码")
    private String wareCode;
    /**
     * 源仓库id
     */
    @ApiModelProperty(value = "id")
    private Long wareId;

    /**
     * 源存储区编码
     */
    @ApiModelProperty(value = "源存储区编码")
    private String areaCode;
    /**
     * 源存储区id
     */
    @ApiModelProperty(value = "id")
    private Long areaId;

    /**
     * 源库位编码
     */
    @ApiModelProperty(value = "源库位编码")
    private String binCode;
    /**
     * 源库位id
     */
    @ApiModelProperty(value = "id")
    private Long binId;

    /**
     * 料号
     */
    @ApiModelProperty(value = "料号")
    private String materialCode;
    /**
     * 数量
     */
    @ApiModelProperty(value = "id")
    private Double quantity;
    /**
     * 移动类型
     */
    @ApiModelProperty(value = "id")
    private String moveType;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;
    /**
     * 需求cell
     */
    @ApiModelProperty(value = "需求cell")
    private String cell;
    /**
     * 动作类型（整托下架、拆托下架）
     */
    @ApiModelProperty(value = "动作类型")
    private Integer type;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * deleteFlag
     */
    @ApiModelProperty(value = "deleteFlag")
    private Integer deleteFlag;
}
