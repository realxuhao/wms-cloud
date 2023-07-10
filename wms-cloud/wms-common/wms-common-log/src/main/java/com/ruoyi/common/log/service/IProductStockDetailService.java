package com.ruoyi.common.log.service;

import com.bosch.system.api.domain.ProductStockDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author GUZ1CGD4
* @description 针对表【product_stock_detail(库存期初期末（job执行）)】的数据库操作Service
* @createDate 2023-07-07 09:44:16
*/
public interface IProductStockDetailService extends IService<ProductStockDetail> {

    void genProStock();
}
