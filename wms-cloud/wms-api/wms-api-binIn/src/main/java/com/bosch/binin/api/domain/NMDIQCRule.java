package com.bosch.binin.api.domain;

import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 09:54
 **/
@Data
public class NMDIQCRule {
    private Long id;
    private Long min_total_quantity;
    private Long max_total_quantity;
    private String check_level;
    private Integer relaxation;
    private Integer stricture;
    private Integer normal;
    private String code;
    private Double number;
}
