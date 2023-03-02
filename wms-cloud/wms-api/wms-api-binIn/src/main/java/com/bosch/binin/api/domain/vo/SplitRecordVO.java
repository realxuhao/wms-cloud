package com.bosch.binin.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 10:45
 **/
@Data
public class SplitRecordVO {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 源sscc码
     */
    @ApiModelProperty(value = "源sscc码")
    private String sourceSscc;

    /**
     * 新的sscc编码
     */
    @ApiModelProperty(value = "新的sscc编码")
    private String newMesBarCode;

    /**
     * 拆托数量
     */
    @ApiModelProperty(value = "拆托数量")
    private Double splitQuantity;

    /**
     * sscc编码
     */
    @ApiModelProperty(value = "sscc编码")
    private String ssccNb;

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


    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记")
    private Integer deleteFlag;


    @ApiModelProperty(value = "拆托后总库存")
    private String targetTotalStock;


    @ApiModelProperty(value = "目的库位编码")
    private String targetBinCode;


    @ApiModelProperty(value = "目的区域编码")
    private String targetAreaCode;


    @ApiModelProperty(value = "源库位编码")
    private String sourceBinCode;


    @ApiModelProperty(value = "源区域编码")
    private String sourceAreaCode;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "仓库")
    private String wareCode;

    @ApiModelProperty(value = "状态")
    private Integer status;



}
