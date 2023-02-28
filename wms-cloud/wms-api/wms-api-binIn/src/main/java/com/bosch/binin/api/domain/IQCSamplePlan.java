package com.bosch.binin.api.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 15:31
 **/
@Data
@TableName("iqc_sample_plan")
public class IQCSamplePlan extends BaseEntity {

    private Long id;

    @ApiModelProperty(value = "sscc")
    private String ssccNb;
    @ApiModelProperty(value = "cell部门")
    private String cell;
    @ApiModelProperty(value = "物料号")
    private String materialNb;
    @ApiModelProperty(value = "下架库位")
    private String binDownCode;
    @ApiModelProperty(value = "下架人")
    private String binDownUser;
    @ApiModelProperty(value = "下架时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date binDownTime;
    @ApiModelProperty(value = "推荐抽样数量")
    private Double recommendSampleQuantity;
    @ApiModelProperty(value = "实际抽样数量")
    private Double sampleQuantity;
    @ApiModelProperty(value = "数量")
    private Double quantity;
    @ApiModelProperty(value = "抽样人")
    private String sampleUser;
    @ApiModelProperty(value = "抽样时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sampleTime;
    @ApiModelProperty(value = "上架人")
    private String binInUser;
    @ApiModelProperty(value = "上架时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date binInTime;
    @ApiModelProperty(value = "上架库位")
    private String binInCode;


    @ApiModelProperty(value = "状态，0：待下架，1：待收养，2：待上架，3：完成")
    private Integer status;

    @ApiModelProperty(value = "批次号")
    private String batchNb;
    @ApiModelProperty(value = "BBD过期时间")
    private Date expireDate;
    @ApiModelProperty(value = "仓库编码")
    private String wareCode;


    private Integer deleteFlag;

}
