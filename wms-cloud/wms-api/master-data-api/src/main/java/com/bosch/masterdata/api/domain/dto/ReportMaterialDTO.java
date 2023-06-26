package com.bosch.masterdata.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
import lombok.Data;

import java.util.Date;


@Data
public class ReportMaterialDTO extends PageDomain {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * SSCC码
     */
    @Excel(name = "sscc")
    private String ssccNumber;

    /**
     * 工厂
     */
    @Excel(name = "plantNb")
    private String plantNb;

    /**
     * 仓库编码
     */
    @Excel(name = "wareCode")
    private String wareCode;

    /**
     * 区域编码
     */
    @Excel(name = "areaCode")
    private String areaCode;
    /**
     * 跨编码
     */
    @Excel(name = "frameCode")
    private String frameCode;

    /**
     * 库位编码
     */
    @Excel(name = "binCode")
    private String binCode;

    /**
     * 物料号
     */
    @Excel(name = "materialNb")
    private String materialNb;

    /**
     * 批次号
     */
    @Excel(name = "batchNb")
    private String batchNb;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "expireDate")
    private Date expireDate;

    /**
     * 总库存
     */
    @Excel(name = "totalStock")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @Excel(name = "freezeStock")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @Excel(name = "availableStock")
    private Double availableStock;

    /**
     * PO号
     */
    @Excel(name = "fromPurchaseOrder")
    private String fromPurchaseOrder;


    /**
     * 上架id
     */
    private Long binInId;

    /**
     * 质检状态
     */
    @Excel(name = "qualityStatus")
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

}
