package com.bosch.binin.api.domain.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RegisterBomDTO {

    /**
     * id
     */
    @ExcelProperty( "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单号
     */
    @ExcelProperty( "订单号")
    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    /**
     * 物料号
     */
    @ExcelProperty( "BOM component")
    @ApiModelProperty(value = "物料代码")
    private String bomComponent;

    /**
     * 数量
     */
    @ExcelProperty( "Component quantity")
    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    @Min(value = 0,message = "数量必须大于0")
    private BigDecimal componentQuantity;

}
