package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.dto.SUDNShipDTO;
import com.bosch.product.api.domain.vo.ProductPickExportVO;
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

    public void validList(List<SUDNDTO> dtos);

    void batchDelete(List<Long> ids);

    void generate(List<Long> idsList);

    void cancel(Long id);

    void  modifyQuantity(Long id,Double quantity);


    List<SUDNVO> getUnFinishedSUDN();

    List<SUDNVO> getFinishedSUDN();


    List<SUDNVO> getUnFinishedShipSUDN();

    List<SUDNVO> getFinishedShipSUDN();

    void ship(SUDNShipDTO shipDTO);


}
