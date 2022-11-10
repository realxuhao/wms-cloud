package com.bosch.binin.api.domain.vo;

import com.bosch.binin.api.domain.Stock;
import lombok.Data;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-09 14:50
 **/
@Data
public class MaterialCallVO extends Stock {

    /**
     * 任务类型，0：下架任务，1：拆托任务
     */
    private Integer taskType;
}
