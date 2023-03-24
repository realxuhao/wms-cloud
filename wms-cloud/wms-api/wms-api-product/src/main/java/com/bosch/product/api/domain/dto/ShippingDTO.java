package com.bosch.product.api.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ShippingDTO {


    @ApiModelProperty(value = "序号")
    private String historyIndex;

    @ApiModelProperty(value = "打包运输标记")
    private String shippingMark;

    @ApiModelProperty(value = "ETO_PO号")
    private String etoPo;


    @ApiModelProperty(value = "打包总托数")
    private String afterPacking;


    @ApiModelProperty(value = "SSCC号码")
    private String ssccNumbers;


    @ApiModelProperty(value = "打包创建者")
    private String historyCreateBy;

    /**
     * 开始创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始创建时间")
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

}
