package com.bosch.vehiclereservation.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;

import java.util.List;

public interface IDriverDispatchService extends IService<DriverDispatch> {

    /**
     * 获取今天签到车辆数据
     *
     * @param wareId 仓库id
     * @return 车辆调度信息列表
     */
    public List<DriverDispatchVO> selectTodaySignData(Long wareId);

}
