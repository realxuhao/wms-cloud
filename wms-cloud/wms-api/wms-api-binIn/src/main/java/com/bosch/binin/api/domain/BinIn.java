package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:16
 * @description: 上架DO
 */
@Data
@TableName("bi_in")
public class BinIn extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
     * 工厂
     */
    private String plantNb;

    /**
     * 仓库编码
     */
    private String wareCode;

    /**
     * 推荐库位编码
     */
    private String recommendBinCode;

    /**
     * 实际库位编码
     */
    private String actualBinCode;

    /**
     * 数量
     */
    private Double quantity;

    /**
     * 托盘编码
     */
    private String palletCode;

    /**
     * 托盘类型
     */
    private String palletType;

    /**
     * 状态(0：待上架,1:已上架)
     */
    private Integer status;

    /**
     * SSCC码
     */
    private String ssccNumber;


    /**
     * area码
     */
    private String areaCode;

    /**
     * 推荐跨id
     */
    private Long recommendFrameId;


    /**
     * 实际跨id
     */
    private Long actualFrameId;
    /**
     * 推荐跨code
     */
    private String recommendFrameCode;
    /**
     * 实际跨code
     */
    private String actualFrameCode;

    /**
     * 移动类型
     */
    private String moveType;

    /**
     * PO号
     */
    private String fromPurchaseOrder;


    /**
     * PO号
     */
    private Integer deleteFlag;

    @TableField(exist = false)
    private String qualityStatus;



}
