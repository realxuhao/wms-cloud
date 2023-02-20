package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.DriverPickup;
import com.bosch.vehiclereservation.api.domain.dto.DriverPickupDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverPickupVO;
import com.bosch.vehiclereservation.api.enumeration.ReserveTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.SignStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverPickupMapper;
import com.bosch.vehiclereservation.service.IDriverPickupService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DriverPickupServiceImpl extends ServiceImpl<DriverPickupMapper, DriverPickup> implements IDriverPickupService {

    @Autowired
    private DriverPickupMapper driverPickupMapper;

    @Override
    public List<DriverPickupVO> selectDriverPickupVO(DriverPickupDTO driverPickupDTO) {
        DriverPickup driverPickup = BeanConverUtil.conver(driverPickupDTO, DriverPickup.class);
        List<DriverPickup> driverPickups = driverPickupMapper.selectDriverPickupList(driverPickup);
        List<DriverPickupVO> driverDeliverVOS = BeanConverUtil.converList(driverPickups, DriverPickupVO.class);
        return driverDeliverVOS;
    }

    @Override
    public boolean deleteDriverPickupById(Long pickupId) {
        DriverPickup driverPickup = super.getById(pickupId);
        if (driverPickup == null) {
            throw new ServiceException("预约单不存在！");
        }
        if (driverPickup.getStatus() != SignStatusEnum.NOT_SIGN.getCode()) {
            throw new ServiceException("订单已到货，不允许删除！");
        }
        boolean res = super.removeById(pickupId);
        return res;
    }

    @Override
    public boolean insertDriverPickup(List<DriverPickupDTO> driverPickupDTOList) {
        Integer i = 0;
        List<DriverPickup> driverPickups = BeanConverUtil.converList(driverPickupDTOList, DriverPickup.class);
        for (DriverPickup driverPickup : driverPickups) {
            QueryWrapper<DriverPickup> wrapper = new QueryWrapper<>();
            wrapper.eq("wechat_id", driverPickup.getWechatId());
            wrapper.eq("driver_name", driverPickup.getDriverName());
            wrapper.eq("driver_phone", driverPickup.getDriverPhone());
            wrapper.eq("car_num", driverPickup.getCarNum());
            wrapper.eq("cell", driverPickup.getCell());
            wrapper.eq("pickup_date", driverPickup.getPickupDate());
            Optional<DriverPickup> first = driverPickupMapper.selectList(wrapper).stream().findFirst();
            if (!first.isPresent()) {
                driverPickup.setStatus(SignStatusEnum.NOT_SIGN.getCode());
                driverPickup.setReserveType(ReserveTypeEnum.RESERVED.getCode());
                i += driverPickupMapper.insert(driverPickup);
            }
        }
        if (i == 0) {
            throw new ServiceException("您已预约过！");
        }
        return i > 0;
    }
}
