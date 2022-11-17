package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.StockLog;
import com.bosch.binin.api.domain.MaterialShift;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:46
 **/
@Mapper
@Repository("materialShiftMapper")
public interface MaterialShiftMapper  extends BaseMapper<MaterialShift> {
}
