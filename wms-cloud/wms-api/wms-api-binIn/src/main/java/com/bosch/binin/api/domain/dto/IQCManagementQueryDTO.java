package com.bosch.binin.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-02 15:28
 **/
@Data
public class IQCManagementQueryDTO extends PageDomain {
    /**
     * SSCC码
     */
    @ApiModelProperty(value = "SSCC码")
    private String ssccNumber;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;


    /**
     * cell
     */
    @ApiModelProperty(value = "cell")
    private String cell;


    /**
     * PO号
     */
    @ApiModelProperty(value = "PO号")
    private String fromPurchaseOrder;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String areaCode;

    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;
    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    private String frameCode;

    /**
     * 库位号
     */
    @ApiModelProperty(value = "库位号")
    private String binCode;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 托盘编码
     */
    @ApiModelProperty(value = "托盘编码")
    private String palletCode;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期开始时间")
    private Date startExpireDate;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期结束时间")
    private Date endExpireDate;

    /**
     * 质检状态
     */
    @ApiModelProperty(value = "质检状态")
    private String qualityStatus;

    @ApiModelProperty(value = "排序类型,0基于有效期，1、基于先主库后外库")
    private Integer sortType;

    @ApiModelProperty(value = "ids列表")
    private List<Long> ids;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;


    /**
     * 已质检状态
     */
    @ApiModelProperty(value = "已质检状态")
    private String changeStatus;

    /**
     * 开始更新时间
     */
    @ApiModelProperty(value = "开始更新时间")
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "结束更新时间")
    private Date endUpdateTime;
}
