package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-02 15:16
 **/
@Data
public class StockVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    @Excel(name = "sscc")
    private String ssccNumber;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @Excel(name = "工厂")
    private String plantNb;


    /**
     * PO号
     */
    @ApiModelProperty(value = "PO号")
    @Excel(name = "PO号")
    private String fromPurchaseOrder;


    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    @Excel(name = "仓库编码")
    private String wareCode;


    /**
     * 存储区编码
     */
    @ApiModelProperty(value = "存储区编码")
    @Excel(name = "存储区编码")
    private String areaCode;

    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    @Excel(name = "跨编码")
    private String frameCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(value = "库位编码")
    @Excel(name = "库位编码")
    private String binCode;

    /**
     * 单位
     */
    @ApiModelProperty(value = "unit")
    @Excel(name = "unit")
    private String unit;

    @Excel(name = "cell")
    private String cell;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间",dateFormat = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "库存量")
    @Excel(name = "库存量")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    @Excel(name = "冻结库存")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    @Excel(name = "可用库存")
    private Double availableStock;


    @ApiModelProperty(value = "任务描述")
    @Excel(name = "任务描述")
    private String jobDesc;

    @ApiModelProperty(value = "任务状态")
    @Excel(name = "任务状态")
    private String jobStatus;


    /**
     * 上架id
     */
    @ApiModelProperty(value = "上架id")
    private Long binInId;

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
    @Excel(name = "质检状态")
    private String qualityStatus;

    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    /**
     * 整托标记，0：整托，1：零托
     */
    @ApiModelProperty(value = "整托标记，0：整托，1：零托")
    @Excel(name = "整托标记",readConverterExp = "0=整托,1=零托")
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


    /** iqc更新者 */
    @ApiModelProperty(value = "IQC更新人")
    private String iqcUpdateBy;

    /** iqc更新时间 */
    @ApiModelProperty(value = "IQC更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date iqcUpdateTime;

}
