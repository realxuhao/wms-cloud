package com.bosch.product.service;

import com.bosch.product.api.domain.StockTakeDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.StockTakeDetailQueryDTO;
import com.bosch.product.api.domain.vo.StockTakeDetailVO;
import com.bosch.product.api.domain.vo.StockTakeTaskVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【stock_take_detail】的数据库操作Service
* @createDate 2023-04-14 13:40:25
*/
public interface IStockTakeDetailService extends IService<StockTakeDetail> {

    void issue(StockTakeDetailQueryDTO dto);

    List<StockTakeTaskVO> getTaskList(StockTakeDetailQueryDTO dto);

    List<StockTakeDetailVO> getDetailList(StockTakeDetailQueryDTO queryDTO);
}
