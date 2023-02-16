package com.bosch.binin.api.domain.dto;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-14 10:50
 **/
@Data
public class WeightDTO {
    private String ip;
    private Integer port;
    private Double totalWeight;


}
