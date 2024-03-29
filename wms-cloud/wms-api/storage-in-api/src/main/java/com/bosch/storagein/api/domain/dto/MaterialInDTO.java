package com.bosch.storagein.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/8/2022 14:39
 * @description:
 */
@Data
@TableName("si_material_receive")
public class MaterialInDTO extends PageDomain {

    /**
     * id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;

    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 检查类型
     */
    @ApiModelProperty(value = "检验类型，0：称重，1：数数，2：免检，3：该批次已检")
    private Integer checkType;

    /**
     * 来源PO号
     */
    @ApiModelProperty(value = "来源PO号")
    private String fromPurchaseOrder;

    /**
     * 应检查数量
     */
    @ApiModelProperty(value = "应检查数量")
    private Integer checkQuantity;

    /**
     * 最小标准
     */
    @ApiModelProperty(value = "最小标准")
    private Double minStandard;

    /**
     * 最大标准
     */
    @ApiModelProperty(value = "最大标准")
    private Double maxStandard;

    /**
     * 实际数量
     */
    @ApiModelProperty(value = "实际数量")
    private Integer actualQuantity;

    /**
     * 实际称重、数数结果
     */
    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;

    /**
     * 实际平均结果
     */
    @ApiModelProperty(value = "实际平均结果")
    private Double averageResult;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;

    /**
     * 虚拟货位
     */
    @ApiModelProperty(value = "虚拟货位")
    private String virtualBinCode;


    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;

    /**
     * 原托数
     */
    @ApiModelProperty(value = "原托数")
    private Integer originalPalletQuantity;

    /**
     * 该托的物料数量
     */
    @ApiModelProperty(value = "该托的物料数量")
    private Integer quantity;

    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型")
    private String moveType;
}
