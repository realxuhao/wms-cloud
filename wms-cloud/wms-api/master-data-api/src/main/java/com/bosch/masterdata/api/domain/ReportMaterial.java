package com.bosch.masterdata.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;


@Data
public class ReportMaterial extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * SSCC码
     */
    @Excel(name = "SSCC码")
    private String ssccNumber;

    /**
     * 工厂
     */
    @Excel(name = "工厂")
    private String plantNb;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码")
    private String wareCode;

    /**
     * 区域编码
     */
    @Excel(name = "区域编码")
    private String areaCode;
    /**
     * 跨编码
     */
    @Excel(name = "跨编码")
    private String frameCode;

    /**
     * 库位编码
     */
    @Excel(name = "库位编码")
    private String binCode;

    /**
     * 物料号
     */
    @Excel(name = "物料号")
    private String materialNb;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchNb;

    /**
     * 过期时间
     */
    @Excel(name = "过期时间",dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 总库存
     */
    @Excel(name = "总库存")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @Excel(name = "冻结库存")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @Excel(name = "可用库存")
    private Double availableStock;

    /**
     * PO号
     */
    @Excel(name = "PO号")
    private String fromPurchaseOrder;


    /**
     * 上架id
     */
    private Long binInId;

    /**
     * 质检状态
     */
    @Excel(name = "质检状态")
    private String qualityStatus;

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

}
