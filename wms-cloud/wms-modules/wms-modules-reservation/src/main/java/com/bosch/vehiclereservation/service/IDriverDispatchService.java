package com.bosch.vehiclereservation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.dto.DispatchSendWxDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverSortDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;

import java.io.IOException;
import java.util.List;

public interface IDriverDispatchService extends IService<DriverDispatch> {

    /**
     * 获取今天签到车辆数据
     *
     * @param driverDispatchDTO 查询条件
     * @return 车辆调度信息列表
     */
    public List<DriverDispatchVO> selectTodaySignData(DriverDispatchDTO driverDispatchDTO);

    /**
     * 获取今天未签到车辆数据
     *
     * @return 车辆预约信息列表
     */
    public List<DriverDispatchVO> selectTodayNotSignData(DriverDispatchDTO driverDispatchDTO);

    /**
     * 分配道口信息
     *
     * @param driverDispatchDTO 道口信息
     * @return boolean
     */
    boolean dispatchDock(DriverDispatchDTO driverDispatchDTO);

    /**
     * 进厂
     *
     * @param dispatchId 主键id
     * @return boolean
     */
    boolean dispatchEnter(Long dispatchId);

    /**
     * 完成(取货/送货)
     *
     * @param dispatchId 主键id
     * @return
     */
    boolean dispatchComplete(Long dispatchId);

    /**
     * 取消
     *
     * @param dispatchId 主键id
     * @return
     */
    boolean dispatchCancel(Long dispatchId);

    /**
     * 车辆排序
     *
     * @param driverDispatchDTO
     * @return
     */
    boolean dispatchSort(DriverSortDTO driverDispatchDTO);

    /**
     * 根据appid及密钥获取access_token
     *
     * @return
     */
    String getWxToken();

    /**
     * 推送入厂消息到司机微信
     *
     * @return
     */
    boolean sendMsgToWx(DispatchSendWxDTO dispatchSendWxDTO);

    /**
     * 处理异常超时数据
     *
     * @return
     */
    public void syncErrorData();
}
