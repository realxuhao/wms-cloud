package com.bosch.binin.api.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterBatchVO {

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    @Excel(name = "生产需求号")
    private String orderNumber;

    /**
     * 料号
     */
    @ApiModelProperty(value = "物料编码")
    @Excel(name = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    @Excel(name = "SSCC码")
    private String ssccNumber;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 需求数量
     */
    @ApiModelProperty(value = "需求量")
    @Excel(name = "需求量", isMerge = true)
    private Double quantity;

    /**
     * 仓库拣配量
     */
    @ApiModelProperty(value = "仓库拣配量")
    @Excel(name = "仓库拣配量")
    private Double pickQuantity;

    /**
     * 仓库拣配日期
     */
    @ApiModelProperty(value = "仓库拣配日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "仓库拣配日期", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pickingTime;

    /**
     * 车间接收数量
     */
    @ApiModelProperty(value = "车间接收数量")
    @Excel(name = "车间接收数量")
    private Double receivedQuantity;

    /**
     * 车间接收日期
     */
    @ApiModelProperty(value = "车间接收日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "车间接收日期", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receivedTime;

    /**
     * 车间退库数量
     */
    @ApiModelProperty(value = "车间退库数量")
    @Excel(name = "车间退库数量")
    private Double returnQuantity;

    /**
     * 车间退库日期
     */
    @ApiModelProperty(value = "车间退库日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "车间退库日期", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /**
     * 仓库接收数量
     */
    @ApiModelProperty(value = "仓库接收数量")
    @Excel(name = "仓库接收数量")
    private Double returnReceivedQuantity;

    /**
     * 仓库接收日期
     */
    @ApiModelProperty(value = "仓库接收日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "仓库接收日期", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnReceivedTime;

    /**
     * 车间消耗数量
     */
    @ApiModelProperty(value = "车间消耗数量")
    @Excel(name = "车间消耗数量")
    private Double productQuantity;

    /**
     * 标准配方量
     */
    @ApiModelProperty(value = "标准配方量")
    @Excel(name = "标准配方量", isMerge = true)
    private Double componentQuantity;

    /**
     * 消耗与配方差异量
     */
    @ApiModelProperty(value = "消耗与配方差异量")
    @Excel(name = "消耗与配方差异量", isMerge = true)
    private Double gapQuantity;

    /**
     * 合并行
     */
    @ApiModelProperty(value = "合并行")
    private Integer rowSpan;

}
