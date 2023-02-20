package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * 车辆调度Mapper接口
 *
 * @author taojd
 */
@Mapper
@Repository("driverDispatchMapper")
public interface DriverDispatchMapper extends BaseMapper<DriverDispatch> {


}
