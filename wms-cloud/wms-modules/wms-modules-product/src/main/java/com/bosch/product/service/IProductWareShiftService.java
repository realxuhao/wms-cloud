package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.ProductWareShift;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:51
 **/
public interface IProductWareShiftService extends IService<ProductWareShift> {
    void addByStockId(Long stockId);

    void cancel(Long id);

    void ship(List<String> ssccList);
}
