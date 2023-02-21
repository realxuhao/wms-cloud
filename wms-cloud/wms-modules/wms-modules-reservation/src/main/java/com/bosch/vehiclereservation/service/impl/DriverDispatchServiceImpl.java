package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverDispatchServiceImpl extends ServiceImpl<DriverDispatchMapper, DriverDispatch> implements IDriverDispatchService {

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;

    @Override
    public List<DriverDispatchVO> selectTodaySignData(Long wareId) {
        return driverDispatchMapper.selectTodaySignData(wareId);
    }
}
