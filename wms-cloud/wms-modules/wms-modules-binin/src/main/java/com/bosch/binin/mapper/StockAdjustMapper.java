package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.StockAdjust;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-01 15:06
 **/
@Mapper
@Repository("stockAdjustMapper")
public interface StockAdjustMapper  extends BaseMapper<StockAdjust> {
}
