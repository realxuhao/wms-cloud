package com.bosch.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.Stock;
import com.bosch.product.api.domain.dto.EditStockDTO;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:56
 * @description:
 */
public interface IMaterialStockService extends IService<Stock> {
    void editStock(EditStockDTO dto);
}
