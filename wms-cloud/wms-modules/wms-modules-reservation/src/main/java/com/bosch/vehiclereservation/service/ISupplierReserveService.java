package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;

import java.util.List;

public interface ISupplierReserveService extends IService<SupplierReserve> {

    /**
     * 获取某个仓库的时间窗口列表
     *
     * @param wareId 仓库id
     * @return 时间窗口列表
     */
    public List<TimeWindowVO> selectTimeWindowList(Long wareId);

}
