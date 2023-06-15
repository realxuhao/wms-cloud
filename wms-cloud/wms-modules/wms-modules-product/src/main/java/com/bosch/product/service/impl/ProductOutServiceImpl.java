package com.bosch.product.service.impl;

import com.bosch.binin.api.enumeration.SPDNStatusEnum;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.service.IProductOutService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 10:10
 **/
@Service
public class ProductOutServiceImpl implements IProductOutService {
    @Override
    public void validList(List<SPDNDTO> dtos) {
        dtos.stream().forEach(item -> {
            item.setQty(item.getQtyString());
            item.setStatus(SPDNStatusEnum.WAITING_APPROVE.code());
        });
    }
}
