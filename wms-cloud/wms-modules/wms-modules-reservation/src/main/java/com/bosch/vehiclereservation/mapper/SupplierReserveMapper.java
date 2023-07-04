package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository("supplierReserveMapper")
public interface SupplierReserveMapper extends BaseMapper<SupplierReserve> {

    /**
     * 获取预约的信息列表
     *
     * @param wareId      仓库id
     * @param reserveDate 预约的日期
     * @return 预约信息列表
     */
    public List<SupplierReserve> selectReserveDateList(@Param("wareId") Long wareId, @Param("reserveDate") Date reserveDate);


    /**
     * 获取最后一条数据
     *
     * @return 预约信息
     */
    public SupplierReserve getLastData();

    /**
     * 查询预约信息列表
     *
     * @param supplierReserve 预约信息
     * @return 预约信息列表
     */
    public List<SupplierReserve> selectSupplierReserveList(SupplierReserve supplierReserve);

    public List<String> selectReserveOnList(SupplierReserve supplierReserve);

    public List<String> getSupplierName();

}
