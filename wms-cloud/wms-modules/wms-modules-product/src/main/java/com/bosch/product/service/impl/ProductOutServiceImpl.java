package com.bosch.product.service.impl;

import com.bosch.binin.api.enumeration.SPDNStatusEnum;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.service.IProductOutService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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
            setQty(item.getQtyString(),item);
            item.setStatus(SPDNStatusEnum.WAITING_APPROVE.code());
        });
    }
    public void setQty(String qty,SPDNDTO spdndto) {
        try {
            if (StringUtils.isNotEmpty(qty)) {
                spdndto.setQty(new DecimalFormat().parse(qty).doubleValue());
            }

        }catch(Exception e){
            throw new ServiceException("Qty列格式不正确");
        }
    }


}
