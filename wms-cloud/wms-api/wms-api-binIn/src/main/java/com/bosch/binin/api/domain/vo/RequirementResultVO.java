package com.bosch.binin.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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



    @Data
    @Builder
    public static class MaterialOrder {
        @ApiModelProperty(value = "订单号")
        private String orderNb;
        @ApiModelProperty(value = "物料号")
        private String materialNb;
    }



}
