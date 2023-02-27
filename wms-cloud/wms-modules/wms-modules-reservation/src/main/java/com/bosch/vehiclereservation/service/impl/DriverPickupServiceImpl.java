package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.DriverPickup;
import com.bosch.vehiclereservation.api.domain.dto.DriverPickupDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverPickupVO;
import com.bosch.vehiclereservation.api.enumeration.DispatchStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.DispatchTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.SignStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.mapper.DriverPickupMapper;
import com.bosch.vehiclereservation.service.IDriverPickupService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverPickupServiceImpl extends ServiceImpl<DriverPickupMapper, DriverPickup> implements IDriverPickupService {

    @Autowired
    private DriverPickupMapper driverPickupMapper;

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;

    @Override
    public List<DriverPickupVO> selectDriverPickupVO(DriverPickupDTO driverPickupDTO) {
        DriverPickup driverPickup = BeanConverUtil.conver(driverPickupDTO, DriverPickup.class);
        driverPickup.setSelectType(1);
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
                driverPickup.setCreateTime(DateUtils.getNowDate());
                driverPickup.setCreateBy(SecurityUtils.getUsername());
                i += driverPickupMapper.insert(driverPickup);
            }
        }
        if (i == 0) {
            throw new ServiceException("您已预约过！");
        }
        return i > 0;
    }

    @Override
    public List<DriverPickupVO> selectDriverPickupInfo(String wechatId) {
        DriverPickup driverPickup = new DriverPickup();
        driverPickup.setWechatId(wechatId);
        driverPickup.setReserveType(ReserveTypeEnum.RESERVED.getCode());
        driverPickup.setStatus(SignStatusEnum.NOT_SIGN.getCode());
        driverPickup.setSelectType(0);
        List<DriverPickup> driverPickups = driverPickupMapper.selectDriverPickupList(driverPickup);
        List<DriverPickupVO> driverPickupVOS = BeanConverUtil.converList(driverPickups, DriverPickupVO.class);
        return driverPickupVOS;
    }

    @Override
    public boolean signIn(Long id) {
        DriverPickup driverPickup = driverPickupMapper.selectById(id);
        if (driverPickup == null) {
            throw new ServiceException("预约信息不存在！");
        }
        driverPickup.setStatus(SignStatusEnum.SIGNED.getCode());
        driverPickup.setSigninDate(DateUtils.getNowDate());
        int i = driverPickupMapper.updateById(driverPickup);
        if (i > 0) {
            saveDriverDispatch(id);
        }
        return i > 0;
    }

    @Override
    public boolean signInDriverPickup(DriverPickupDTO driverPickupDTO) {
        DriverPickup driverPickup = BeanConverUtil.conver(driverPickupDTO, DriverPickup.class);
        driverPickup.setStatus(SignStatusEnum.SIGNED.getCode());
        driverPickup.setReserveType(ReserveTypeEnum.NOT_RESERVE.getCode());
        driverPickup.setSigninDate(DateUtils.getNowDate());
        driverPickup.setCreateTime(DateUtils.getNowDate());
        driverPickup.setCreateBy(SecurityUtils.getUsername());
        boolean res = super.save(driverPickup);
        if (res) {
            saveDriverDispatch(driverPickup.getPickupId());
        }
        return res;
    }

    private void saveDriverDispatch(Long id) {
        DriverDispatch driverDispatch = new DriverDispatch();
        driverDispatch.setDriverId(id);
        driverDispatch.setDriverType(DispatchTypeEnum.PICKUP.getCode());
        driverDispatch.setStatus(DispatchStatusEnum.WAITE.getCode());
        driverDispatch.setCreateTime(DateUtils.getNowDate());
        driverDispatch.setCreateBy(SecurityUtils.getUsername());
        driverDispatchMapper.insert(driverDispatch);
    }
}
