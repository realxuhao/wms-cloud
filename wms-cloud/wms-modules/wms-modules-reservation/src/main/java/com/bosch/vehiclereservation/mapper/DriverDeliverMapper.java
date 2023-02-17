package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 司机送货预约单Mapper接口
 *
 * @author taojd
 */
@Mapper
@Repository("driverDeliverMapper")
public interface DriverDeliverMapper extends BaseMapper<DriverDeliver> {


    /**
     * 查询司机送货信息列表
     *
     * @param driverDeliver 查询条件
     * @return 司机送货信息列表
     */
    public List<DriverDeliver> selectDriverDeliverList(@Param("driver") DriverDeliver driverDeliver);

}
