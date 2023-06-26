package com.bosch.masterdata.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
public class ReportWareShiftDTO extends PageDomain {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "移库单号")
    @Excel(name = "移库单号")
    private String orderNb;


    @ApiModelProperty(value = "车牌号")
    @Excel(name = "车牌号")
    private String carNb;

    /**
     * sscc
     */
    @ApiModelProperty(value = "sscc")
    @Excel(name = "sscc")
    private String ssccNb;



    /**
     * 物料号
     */
    @ApiModelProperty(value = "物料号")
    @Excel(name = "物料号")
    private String materialNb;

    @ApiModelProperty(value = "materialName")
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    @Excel(name = "批次号")
    private String batchNb;

    @ApiModelProperty(value = "移库数量")
    @Excel(name = "移库数量")
    private Double quantity;


    @ApiModelProperty(value = "拆托标记，0：整托，1：拆托")
    private Integer splitType;

    @ApiModelProperty(value = "拆托数量")
    private Double splitQuality;

    /**
     * 源工厂
     */
    @ApiModelProperty(value = "源工厂")
    @Excel(name = "源工厂")
    private String sourcePlantNb;

    /**
     * 源仓库
     */
    @ApiModelProperty(value = "源仓库")
    @Excel(name = "源仓库")
    @TableField(value = "source_ware_code")
    private String source_ware_code;

    /**
     * 源存储区
     */
    @ApiModelProperty(value = "源存储区")
    @Excel(name = "源存储区")
    private String sourceAreaCode;



    /**
     * 源库位
     */
    @ApiModelProperty(value = "源库位")
    @Excel(name = "源库位")
    private String sourceBinCode;



    /**
     * 移动类型
     */
    @ApiModelProperty(value = "移动类型, (\"0\", \"收货\"),(\"1\", \"入库\"),(“2”, \"上架\"),(\"3\",\"生产叫料\"),(\"4\", \"移库\"),(\"5\",\"拆托上架\"),(\"6\",\"仓库内移动\"),(\"7\",\"生产退料\");")
    private String moveType;



    /**
     * bbd过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "bbd过期时间")
    @Excel(name = "bbd过期时间",dateFormat = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 目的工厂
     */
    @ApiModelProperty(value = "目的工厂")
    @Excel(name = "目的工厂")
    private String targetPlant;

    /**
     * 目的仓库
     */
    @ApiModelProperty(value = "目的仓库")
    @Excel(name = "目的仓库")
    private String targetWareCode;

    /**
     * 状态：待下架（来自于②）、待发运（外库pda下架）、待收货（外库发运扫描）、已收货（目的仓库收货扫描）、已上架（pda扫描上架
     */
    @ApiModelProperty(value = "状态：待下架（来自于②）、待发运（外库pda下架）、待收货（外库发运扫描）、已收货（目的仓库收货扫描）、已上架（pda扫描上架")
    @Excel(name = "状态",readConverterExp = "-1=已取消,1=待下架,2=待发运,4=待收货,5=待上架,7=完成")
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
    @Excel(name = "目的存储区code")
    private String targetAreaCode;

    @ApiModelProperty(value = "recommendBinCode")
    @Excel(name = "推荐库位")
    private String recommendBinCode;


    /**
     * 目的库位code
     */
    @ApiModelProperty(value = "目的库位code")
    @Excel(name = "目的库位code")
    private String targetBinCode;


    @ApiModelProperty(value = "所属需求订单号")
    private String orderNumber;


    /** 备注 */
    @Excel(name = "备注")
    private String remark;



}
