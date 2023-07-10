package com.bosch.vehiclereservation.api.domain.dto;

import com.ruoyi.common.core.web.page.PageDomain;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SupplierOnTimeDTO extends PageDomain {

    @ApiModelProperty(value = "供应商姓名")
    private String supplierName;

    /**
     * 查询的年份
     */
    @ApiModelProperty(value = "查询的年份")
    private Integer searchYear;
}
