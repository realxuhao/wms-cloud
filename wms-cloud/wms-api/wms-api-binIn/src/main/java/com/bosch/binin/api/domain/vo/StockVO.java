package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String ssccNumber;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    private String plantNb;


    /**
     * PO号
     */
    @ApiModelProperty(value = "PO号")
    private String fromPurchaseOrder;


    /**
     * 仓库编码
     */
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;


    /**
     * 存储区编码
     */
    @ApiModelProperty(value = "存储区编码")
    private String areaCode;

    /**
     * 跨编码
     */
    @ApiModelProperty(value = "跨编码")
    private String frameCode;

    /**
     * 库位编码
     */
    @ApiModelProperty(value = "库位编码")
    private String binCode;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireDate;

    /**
     * 总库存
     */
    @ApiModelProperty(value = "库存量")
    private Double totalStock;

    /**
     * 冻结库存
     */
    @ApiModelProperty(value = "冻结库存")
    private Double freezeStock;

    /**
     * 可用库存
     */
    @ApiModelProperty(value = "可用库存")
    private Double availableStock;


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
    private String qualityStatus;

}
