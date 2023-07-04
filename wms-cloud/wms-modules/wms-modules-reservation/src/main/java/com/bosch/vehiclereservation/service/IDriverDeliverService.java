package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierOnTimeDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierOnTimeVO;

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
     * 查询供应商准时率（单个供应商数据）
     *
     * @param supplierOnTimeDTO 查询条件
     * @return 单个供应商准时率
     */
    public List<SupplierOnTimeVO> selectSupplierOnTime(SupplierOnTimeDTO supplierOnTimeDTO);

    public List<SupplierOnTimeVO> selectAllSupplierOnTimeList(Integer year);

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

    /**
     * 查询司机的送货预约信息
     *
     * @param wechatId 微信id
     * @return 查询结果
     */
    public List<DriverDeliverVO> selectDriverDeliverInfo(String wechatId);

    /**
     * 司机送货签到(已预约)
     *
     * @param id 主键id
     * @return
     */
    public boolean signIn(Long id);

    /**
     * 司机送货签到(未预约)
     *
     * @param driverDeliverDTO 签到信息
     * @return
     */
    public boolean signInDriverDeliver(DriverDeliverDTO driverDeliverDTO);
}
