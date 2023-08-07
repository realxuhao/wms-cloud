package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:08
 * @description:　库存
 */
@Data
@TableName("bi_stock")
public class Stock extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * SSCC码
     */
    private String ssccNumber;

    /**
     * 工厂
     */
    private String plantNb;

    /**
     * 仓库编码
     */
    private String wareCode;

    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 跨编码
     */
    private String frameCode;

    /**
     * 库位编码
     */
    private String binCode;

    /**
     * 物料号
     */
    private String materialNb;

    /**
     * 批次号
     */
    private String batchNb;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 总库存
     */
    private Double totalStock;

    /**
     * 冻结库存
     */
    private Double freezeStock;

    /**
     * 可用库存
     */
    private Double availableStock;

    /**
     * PO号
     */
    private String fromPurchaseOrder;


    /**
     * 上架id
     */
    private Long binInId;

    /**
     * 质检状态
     */
    private String qualityStatus;

    /**
     * 质检状态
     */
    private Integer deleteFlag;


    private String palletCode;

    /**
     * 整托标记，0：整托，1：零托
     */
    private int wholeFlag;

    /**
     * 变更状态（0：未变更，1：已变更）
     */
    private int changeStatus;

    /** iqc更新者 */
    private String iqcUpdateBy;

    /** iqc更新时间 */
    private Date iqcUpdateTime;
}
