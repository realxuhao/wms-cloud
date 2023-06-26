package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.dto.WeightDTO;
import com.bosch.storagein.api.domain.Weight;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-05 09:16
 **/
public interface IWeightService extends IService<Weight> {

    void addWeight(WeightDTO weightDTO);

    Weight getByPort(String ip);
}
