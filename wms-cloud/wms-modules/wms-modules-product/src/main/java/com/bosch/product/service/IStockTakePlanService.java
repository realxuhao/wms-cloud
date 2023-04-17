package com.bosch.product.service;

import com.bosch.product.api.domain.StockTakePlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import org.apache.ibatis.annotations.Param;

/**
* @author GUZ1CGD4
* @description 针对表【stock_take_plan】的数据库操作Service
* @createDate 2023-04-14 13:40:35
*/
public interface IStockTakePlanService extends IService<StockTakePlan> {

    void addStockTakePlan(StockTakeAddDTO dto);

    void delete(@Param("id") Long id);
}
