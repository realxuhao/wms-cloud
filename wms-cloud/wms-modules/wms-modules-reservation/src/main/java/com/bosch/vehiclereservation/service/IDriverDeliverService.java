package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;

import java.util.List;

public interface IDriverDeliverService extends IService<DriverDeliver> {

    /**
     * 查询司机送货信息列表
     *
     * @param driverDeliverDTO 查询条件
     * @return 司机送货信息列表
     */
    public List<DriverDeliverVO> selectDriverDeliverVO(DriverDeliverDTO driverDeliverDTO);

    /**
     * 删除司机预约信息
     *
     * @param deliverId 预约id
     * @return boolean
     */
    public boolean deleteDriverDeliverById(Long deliverId);

    /**
     * 新增司机的预约信息
     *
     * @param driverDeliverDTO 司机的预约信息
     * @return boolean
     */
    public boolean insertDriverDeliver(DriverDeliverDTO driverDeliverDTO);

}
