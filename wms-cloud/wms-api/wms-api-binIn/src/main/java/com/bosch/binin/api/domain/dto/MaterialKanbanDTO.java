package com.bosch.binin.api.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialKanbanDTO extends PageDomain {

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
     * 源仓库编码
     */
    @ApiModelProperty(value = "源仓库编码")
    private String wareCode;

    /**
     * 源存储区编码
     */
    @ApiModelProperty(value = "源存储区编码")
    private String areaCode;

    /**
     * 源库位编码
     */
    @ApiModelProperty(value = "源库位编码")
    private String binCode;

    /**
     * 料号
     */
    @ApiModelProperty(value = "料号")
    private String materialCode;

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
    @ApiModelProperty(value = "动作类型（0：整托下架 1：拆托下架）")
    private Integer type;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态：0：待下发（源工厂为7751的没有） 1：已下发 2：已下架（下架时PDA扫描SSCC）、配送完成（产线PDA扫描接收）")
    private Integer status;

    /** 创建者 */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    /** 更新者 */
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeStart;
    /** 更新时间 */
    @ApiModelProperty(value = "更新时间止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTimeEnd;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Double quantity;
}
