package com.bosch.vehiclereservation.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DriverSortDTO {

    @ApiModelProperty(value = "主键id")
    private Long dispatchId;

    @ApiModelProperty(value = "新的排序号")
    private Integer newSortNo;

}
