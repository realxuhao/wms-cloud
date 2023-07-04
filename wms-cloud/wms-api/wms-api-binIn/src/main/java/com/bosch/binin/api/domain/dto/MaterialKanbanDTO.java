package com.bosch.binin.api.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class MaterialKanbanDTO extends PageDomain {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * 订单号
     */
    @NotEmpty(message = "订单号不能为空")
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

    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 料号
     */
    @NotEmpty(message = "料号不能为空")
    @ApiModelProperty(value = "料号")
    private String materialCode;

    /**
     * SSCC码
     */
    @NotEmpty(message = "SSCC码不能为空")
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
    @ApiModelProperty(value = "CANCEL(-1,\"取消任务\"),\n" +
            "WAITING_ISSUE(0, \"待下发\"),\n" +
            "WAITING_BIN_DOWN(1, \"待下架\"),\n" +
            "OUT_DOWN(2, \"外库待发运\"),\n" +
            "INNER_DOWN(3, \"产线待收货（下架扫描之后）\"),\n" +
            "INNER_RECEIVING(4, \"主库待收货\"),\n" +
            "\n" +
            "INNER_BIN_IN(5, \"待上架\"),\n" +
            "\n" +
            "LINE_RECEIVED(6, \"产线已收货\"),\n" +
            "\n" +
            "FINISH(7, \"完成\");")
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
    @NotNull(message = "数量不能为空")
    @Min(value = 0,message = "数量不能小于0")
    @ApiModelProperty(value = "数量")
    private Double quantity;

    @ApiModelProperty(value = "物料类型")
    private List<String> materialTypeList;
}
