package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository("supplierReserveMapper")
public interface SupplierReserveMapper extends BaseMapper<SupplierReserve> {

    /**
     * 获取今天预约的信息列表
     *
     * @param wareId 仓库id
     * @return 预约信息列表
     */
    public List<SupplierReserve> selectCurdateList(Long wareId);

}
