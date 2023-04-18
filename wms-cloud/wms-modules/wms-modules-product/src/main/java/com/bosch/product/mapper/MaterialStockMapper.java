package com.bosch.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:56
 * @description:
 */
@Mapper
@Repository("materialStockMapper")
public interface MaterialStockMapper extends BaseMapper<Stock> {

}