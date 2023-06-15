package com.bosch.product.service;

import com.bosch.product.api.domain.dto.SPDNDTO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 10:09
 **/
public interface IProductOutService {

    void validList (List<SPDNDTO> dtos);
}
