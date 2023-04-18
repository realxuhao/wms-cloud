package com.bosch.product.mapper;

import com.bosch.product.api.domain.StockTakeDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.dto.StockTakeDetailQueryDTO;
import com.bosch.product.api.domain.vo.StockTakeDetailVO;
import com.bosch.product.api.domain.vo.StockTakeTaskVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【stock_take_detail】的数据库操作Mapper
* @createDate 2023-04-14 13:40:25
* @Entity com.bosch.product.api.domain.StockTakeDetail
*/
@Mapper
public interface StockTakeDetailMapper extends BaseMapper<StockTakeDetail> {

    List<StockTakeDetailVO> getDetailList(StockTakeDetailQueryDTO dto);

    List<StockTakeTaskVO> getTaskList(StockTakeDetailQueryDTO dto);


}




