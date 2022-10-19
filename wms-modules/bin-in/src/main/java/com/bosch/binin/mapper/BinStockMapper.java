package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.storagein.api.domain.BinIn;
import com.bosch.storagein.api.domain.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:56
 * @description:
 */
@Mapper
@Repository("binStockMapper")
public interface BinStockMapper extends BaseMapper<Stock> {
}
