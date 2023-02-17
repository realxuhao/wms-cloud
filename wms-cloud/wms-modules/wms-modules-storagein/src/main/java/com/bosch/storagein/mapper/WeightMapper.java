package com.bosch.storagein.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.storagein.api.domain.MaterialReceive;
import com.bosch.storagein.api.domain.Weight;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-17 10:42
 **/
@Mapper
@Repository("weightMapper")
public interface WeightMapper extends BaseMapper<Weight> {
}
