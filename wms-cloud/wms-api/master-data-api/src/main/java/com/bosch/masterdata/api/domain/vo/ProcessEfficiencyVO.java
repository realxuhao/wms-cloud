package com.bosch.masterdata.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProcessEfficiencyVO {

    @ApiModelProperty(value = "cell")
    private String cell;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;



    @ApiModelProperty(value = "入库耗时")
    private BigDecimal binInConsuming;

    @ApiModelProperty(value = "IQC耗时")
    private BigDecimal iqcConsuming;

    @ApiModelProperty(value = "叫料耗时")
    private BigDecimal callConsuming;
}
