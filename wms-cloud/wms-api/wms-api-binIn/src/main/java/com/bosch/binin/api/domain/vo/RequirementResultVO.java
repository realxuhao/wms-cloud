package com.bosch.binin.api.domain.vo;

import com.sun.tools.doclets.formats.html.LinkInfoImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 13:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementResultVO {

    //部分满足的物料号
    @ApiModelProperty(value = "部分满足的物料号")
    private List<MaterialOrder> unStatisfiedMaterialNbs;
    //完全满足的物料号
    @ApiModelProperty(value = "完全满足的物料号")
    private List<MaterialOrder> fullStatisfiedMaterialNbs;
    //没有库存的物料号
    @ApiModelProperty(value = "没有满足的物料号")
    private List<MaterialOrder> noStockMaterialNbs;

    @ApiModelProperty(value = "库存不足的物料")
    private List<NotEnoughStock> notEnoughStocks;


    @Data
    @Builder
    public static class MaterialOrder {
        @ApiModelProperty(value = "订单号")
        private String orderNb;
        @ApiModelProperty(value = "物料号")
        private String materialNb;
    }

    @Data
    public static class NotEnoughStock {
        @ApiModelProperty(value = "物料号")
        private String materialNb;
        @ApiModelProperty(value = "可用库存")
        private Double avaliableQuantity;
    }

}
