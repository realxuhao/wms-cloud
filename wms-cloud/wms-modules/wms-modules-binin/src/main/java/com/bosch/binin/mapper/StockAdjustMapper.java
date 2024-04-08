package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.StockAdjust;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.StockAdjustVO;
import com.bosch.binin.api.domain.vo.StockVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-01 15:06
 **/
@Mapper
@Repository("stockAdjustMapper")
public interface StockAdjustMapper  extends BaseMapper<StockAdjust> {

    List<StockAdjustVO> selectStockAdjustVOList(StockQueryDTO stockQueryDTO);

}
