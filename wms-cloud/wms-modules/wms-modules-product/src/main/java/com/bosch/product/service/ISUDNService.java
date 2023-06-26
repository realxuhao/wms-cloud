package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.api.domain.vo.SUDNVO;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:53
 **/
public interface ISUDNService extends IService<SUDN> {

    List<SUDNVO> getList(SUDNDTO sudndto);

    void batchDelete(List<Long> ids);

    void generate(List<Long> idsList);
}
