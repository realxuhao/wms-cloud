package com.bosch.masterdata.api.domain.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EfficiencyVO  {


    /**
     * 创建人
     */
    @ApiModelProperty(value = "cell")
    private String cell;

    /** 创建时间 */
//    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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


    @ApiModelProperty(value = "耗时")
    private BigDecimal timeConsuming;
}
