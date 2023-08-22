package com.bosch.masterdata.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WorkloadVO {
    /**
     *
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "create_by")
    @Excel(name = "操作账号")
    private String createBy;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "nick_name")
    @Excel(name = "操作人")
    private String nickName;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "update_by")

    private String updateBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")

    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "update_time")

    private Date updateTime;



    /**
     * 物料、成品号
     */
    @ApiModelProperty(value = "code")
    private String code;

    /**
     *
     */
    @ApiModelProperty(value = "sscc_number")
    private String ssccNumber;

    /**
     *
     */
    @ApiModelProperty(value = "operation_type")
    private String operationType;

    /**
     * 上架
     */
    @ApiModelProperty(value = "上架")
    @Excel(name = "上架")
    private Integer binIn;

    /**
     * 拣配下架
     */
    @ApiModelProperty(value = "拣配下架")
    @Excel(name = "拣配下架")
    private Integer binOut;

    /**
     * 其他下架
     */
    @ApiModelProperty(value = "其他下架")
    @Excel(name = "其他下架")
    private Integer binOutOther;

    /**
     * 拆托
     */
    @ApiModelProperty(value = "拆托")
    @Excel(name = "拆托")
    private Integer palletSplit;

    /**
     * 翻托
     */
    @ApiModelProperty(value = "翻托")
    @Excel(name = "翻托")
    private Integer palletTurnover;
    /**
     * 移库上架
     */
    @ApiModelProperty(value = "移库上架")
    @Excel(name = "移库上架")
    private Integer shiftBinIn;

    /**
     * IQC上架
     */
    @ApiModelProperty(value = "IQC上架")
    @Excel(name = "IQC上架")
    private Integer iqcBinIn;

    /**
     * IQC下架
     */
    @ApiModelProperty(value = "IQC下架")
    @Excel(name = "IQC下架")
    private Integer iqcBinOut;


    /**
     * 转储
     */
    @ApiModelProperty(value = "转储")
    @Excel(name = "转储")
    private Integer manualTrans;
}
