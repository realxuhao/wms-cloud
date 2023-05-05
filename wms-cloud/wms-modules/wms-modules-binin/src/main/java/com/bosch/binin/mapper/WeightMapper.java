package com.bosch.binin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.storagein.api.domain.Weight;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-05-05 09:22
 **/
@Mapper
@Repository("weightMapper")
public interface WeightMapper extends BaseMapper<Weight> {

}
