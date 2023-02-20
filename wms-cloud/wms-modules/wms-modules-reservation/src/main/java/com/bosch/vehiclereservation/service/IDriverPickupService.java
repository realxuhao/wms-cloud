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

    /**
     * 删除司机预约信息
     *
     * @param pickupId 预约id
     * @return boolean
     */
    public boolean deleteDriverPickupById(Long pickupId);

    /**
     * 新增司机的预约信息
     *
     * @param driverPickupDTOList 司机的预约信息
     * @return boolean
     */
    public boolean insertDriverPickup(List<DriverPickupDTO> driverPickupDTOList);

    /**
     * 查询司机的取货预约信息
     *
     * @param wechatId 微信id
     * @return 查询结果
     */
    public List<DriverPickupVO> selectDriverPickupInfo(String wechatId);

    /**
     * 司机取货签到(已预约)
     *
     * @param id 主键id
     * @return
     */
    public boolean signIn(Long id);

    /**
     * 司机取货签到(未预约)
     *
     * @param driverPickupDTO 签到信息
     * @return
     */
    boolean signInDriverPickup(DriverPickupDTO driverPickupDTO);
}
