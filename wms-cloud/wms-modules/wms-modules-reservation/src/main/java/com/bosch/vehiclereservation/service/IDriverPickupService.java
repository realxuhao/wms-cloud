package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.DriverPickup;
import com.bosch.vehiclereservation.api.domain.dto.DriverPickupDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverPickupVO;

import java.util.List;

public interface IDriverPickupService extends IService<DriverPickup> {

    /**
     * 查询司机取货信息列表
     *
     * @param driverPickupDTO 查询条件
     * @return 司机取货信息列表
     */
    public List<DriverPickupVO> selectDriverPickupVO(DriverPickupDTO driverPickupDTO);

    public boolean deleteDriverPickupById(Long pickupId);

    public boolean insertDriverPickup(List<DriverPickupDTO> driverPickupDTOList);

    public List<DriverPickupVO> selectDriverPickupInfo(String wechatId);

    public boolean signIn(Long id);
}
