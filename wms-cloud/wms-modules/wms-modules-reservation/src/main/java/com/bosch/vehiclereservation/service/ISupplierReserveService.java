package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierReserveDTO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveVO;

import java.util.Date;
import java.util.List;

public interface ISupplierReserveService extends IService<SupplierReserve> {

    /**
     * 获取某个仓库的时间窗口列表
     *
     * @param wareId      仓库id
     * @param reserveDate 预约的日期
     * @return 时间窗口列表
     */
    public List<TimeWindowVO> selectTimeWindowList(Long wareId, Date reserveDate);


    /**
     * 新增供应商预约信息
     *
     * @param supplierReserveDTO 保存预约数据
     * @return boolean
     */
    public boolean insertSupplierReserve(SupplierDTO supplierReserveDTO);


    /**
     * 查询供应商预约信息
     *
     * @param supplierReserveDTO 预约查询条件
     * @return 供应商预约信息列表
     */
    public List<SupplierReserveVO> selectSupplierReserveVO(SupplierReserveDTO supplierReserveDTO);

    /**
     * 删除供应商预约信息
     *
     * @param reserveId 预约id
     * @return boolean
     */
    public boolean deleteSupplierReserveById(Long reserveId);
}
