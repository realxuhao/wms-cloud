package com.bosch.binin.mapper;

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
@Repository("stockMapper")
public interface StockMapper extends BaseMapper<Stock> {

    List<StockVO> selectIQCManagementList(IQCManagementQueryDTO iqcManagementQueryDTO);

    List<StockVO> selectStockVOList(StockQueryDTO stockQueryDTO);

    int validateStatus(Long id);

    Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO);

    List<StockVO> selectStockVOListBySSCCList(@Param("ssccList") List<String> ssccList);

    List<StockVO> selectStockVOBySortType(StockQueryDTO stockQueryDTO);

    int selectCountByList(@Param("dtos") List<MaterialKanbanDTO> dtos);

    List<Stock> selectStockList(@Param("dtos") List<MaterialKanbanDTO> dtos);

    List<StockVO> getBinStockLog(@Param("binCode")String binCode);
}
