package com.bosch.binin.api.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
public class RequirementResultVO {

    //部分满足的物料号
    private List<MaterialOrder> unStatisfiedMaterialNbs;
    //完全满足的物料号
    private List<MaterialOrder> fullStatisfiedMaterialNbs;
    //没有库存的物料号
    private List<MaterialOrder> noStockMaterialNbs;


    @Data
    @Builder
    public static class MaterialOrder {
        private String orderNb;
        private String materialNb;
    }
}
