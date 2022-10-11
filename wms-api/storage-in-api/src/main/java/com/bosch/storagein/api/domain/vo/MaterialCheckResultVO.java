package com.bosch.storagein.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: UWH4SZH
 * @since: 10/9/2022 13:17
 * @description:
 */
@Data
@Accessors(chain = true)
public class MaterialCheckResultVO extends MaterialInCheckVO {

    @ApiModelProperty(value = "校验成功标记")
    private Boolean checkFlag;

    @ApiModelProperty(value = "SSCC码")
    private String mesBarCode;

    @ApiModelProperty(value = "实际数量")
    private Integer actualQuantity;

    @ApiModelProperty(value = "实际称重、数数结果")
    private Double actualResult;

    @ApiModelProperty(value = "平均")
    private Double averageResult;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operateUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private Date operateTime;


}
