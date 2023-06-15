package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.vo.SPDNVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:53
 **/
public interface ISPDNService  extends IService<SPDN> {

    List<SPDNVO> getList(SPDNDTO spdndto);

    void batchDelete(List<Long> ids);

    void approve(List<Long> idsList);
}
