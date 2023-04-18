package com.bosch.product.service;

import com.bosch.product.api.domain.StockTakePlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.api.domain.dto.StockTakePlanDTO;
import com.bosch.product.api.domain.vo.StockTakePlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【stock_take_plan】的数据库操作Service
* @createDate 2023-04-14 13:40:35
*/
public interface IStockTakePlanService extends IService<StockTakePlan> {

    void addStockTakePlan(StockTakeAddDTO dto);

    void delete(@Param("id") Long id);

    /**
     * 盘点计划列表
     * @param dto
     * @return
     */
    List<StockTakePlan> list(StockTakePlanDTO dto);
}
