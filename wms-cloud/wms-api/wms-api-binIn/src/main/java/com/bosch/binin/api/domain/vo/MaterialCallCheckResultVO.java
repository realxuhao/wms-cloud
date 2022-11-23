package com.bosch.binin.api.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-23 11:00
 **/

@Data
public class MaterialCallCheckResultVO {
    private Boolean checkFlag;
    private List<NotEnoughStock> notEnoughStockList;


    @Data
    public static class NotEnoughStock {
        @ApiModelProperty(value = "物料号")
        private String materialNb;
        @ApiModelProperty(value = "可用库存")
        private Double avaliableQuantity;
    }
}
