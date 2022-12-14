package com.bosch.binin.api.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bosch.binin.api.domain.WareShift;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-07 14:32
 **/
@Data
public class WareShiftVO  {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "recommendBinCode")
    private String recommendBinCode;
    @ApiModelProperty(value = "materialName")
    private String materialName;

    /**
     * 源工厂
     */
    @ApiModelProperty(value = "源工厂")
    private String sourcePlantNb;

    /**
     * 源仓库
     */
    @ApiModelProperty(value = "源仓库")
    private String sourceWareCode;

    /**
     * 源存储区
     */
    @ApiModelProperty(value = "源存储区")
    private String sourceAreaCode;

    /**
     * 源库位
     */
    @ApiModelProperty(value = "源库位")
    private String sourceBinCode;

    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型")
    private String moveType;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    private String ssccNb;

    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    private String materialNb;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNb;

    /**
     * bbd过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "bbd过期时间")
    private Date expireDate;

    /**
     * 目的工厂
     */
    @ApiModelProperty(value = "目的工厂")
    private String targetPlant;

    /**
     * 目的仓库
     */
    @ApiModelProperty(value = "目的仓库")
    private String targetWareCode;

    /**
     * 状态：待下架（来自于②）、待发运（外库pda下架）、待收货（外库发运扫描）、已收货（目的仓库收货扫描）、已上架（pda扫描上架
     */
    @ApiModelProperty(value = "状态：待下架（来自于②）、待发运（外库pda下架）、待收货（外库发运扫描）、已收货（目的仓库收货扫描）、已上架（pda扫描上架")
    private Integer status;

    /**
     * 删除标记1：删除，0:可用
     */
    @ApiModelProperty(value = "删除标记1：删除，0:可用")
    private Integer deleteFlag;


    /**
     * 目的存储区code
     */
    @ApiModelProperty(value = "目的存储区code")
    private String targetAreaCode;


    /**
     * 目的库位code
     */
    @ApiModelProperty(value = "目的库位code")
    private String targetBinCode;
    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

}
