package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.StockVO;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:56
 * @description:
 */
public interface IStockService extends IService<Stock> {

    List<StockVO> selectStockVOList(StockQueryDTO stockQuerySTO);

    List<StockVO> selectStockVOBySortType(StockQueryDTO stockQuerySTO);

    Double countAvailableStock(StockQueryDTO stockQueryDTO);

    StockVO getOneBySSCC(String ssccs);
}
