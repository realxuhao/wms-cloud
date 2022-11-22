package com.bosch.binin.api.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-22 13:37
 **/
@Data
public class MaterialCalJobRequestDTO {


    @Data
    public static class SystemGenerateJob {
        @ApiModelProperty(value = "订单号")
        private List<Long> callIds;
        @ApiModelProperty(value = "是否继续，默认false")
        private boolean continueFlag;
    }




}
