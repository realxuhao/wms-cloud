package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.DriverPickup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 司机取货预约单Mapper接口
 *
 * @author taojd
 */
@Mapper
@Repository("driverPickupMapper")
public interface DriverPickupMapper extends BaseMapper<DriverPickup> {


    /**
     * 查询司机取货信息列表
     *
     * @param driverPickup 查询条件
     * @return 司机送货信息列表
     */
    public List<DriverPickup> selectDriverPickupList(@Param("driver") DriverPickup driverPickup);
}
