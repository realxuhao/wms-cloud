package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.SignStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDeliverService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

@Service
public class DriverDeliverServiceImpl extends ServiceImpl<DriverDeliverMapper, DriverDeliver> implements IDriverDeliverService {

    @Autowired
    private DriverDeliverMapper driverDeliverMapper;

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;


    @Override
    public List<DriverDeliverVO> selectDriverDeliverVO(DriverDeliverDTO driverDeliverDTO) {
        if (StringUtils.isEmpty(driverDeliverDTO.getSupplierName()) && StringUtils.isEmpty(driverDeliverDTO.getWechatId())) {
            throw new ServiceException("无权限查看数据");
        }
        DriverDeliver driverDeliver = BeanConverUtil.conver(driverDeliverDTO, DriverDeliver.class);
        // 供应商查看司机预约的信息
        String supplierName = driverDeliverDTO.getSupplierName();
        if (StringUtils.isNotEmpty(supplierName)) {
            SupplierReserve supplierReserve = new SupplierReserve();
            supplierReserve.setSupplierCode(supplierName);
            List<SupplierReserve> selectSupplierReserveList = supplierReserveMapper.selectSupplierReserveList(supplierReserve);
            List<String> reserveNoList = selectSupplierReserveList.stream().map(c -> c.getReserveNo()).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(reserveNoList)) {
                driverDeliver.setReserveNoList(reserveNoList);
            } else {
                List<DriverDeliverVO> driverDeliverVOS = new ArrayList<>();
                return driverDeliverVOS;
            }
        }
        startPage();
        List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverList(driverDeliver);
        List<DriverDeliverVO> driverDeliverVOS = BeanConverUtil.converList(driverDelivers, DriverDeliverVO.class);
        return driverDeliverVOS;
    }

    @Override
    public boolean deleteDriverDeliverById(Long deliverId) {
        DriverDeliver driverDeliver = super.getById(deliverId);
        if (driverDeliver.getStatus() != SignStatusEnum.NOT_SIGN.getCode()) {
            throw new ServiceException("订单已到货，不允许删除！");
        }
        boolean res = super.removeById(deliverId);
        if (res) {
            QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
            wrapper.eq("reserve_no", driverDeliver.getReserveNo());
            SupplierReserve supplierReserve = supplierReserveMapper.selectList(wrapper).get(0);
            if (supplierReserve != null && supplierReserve.getStatus() == ReserveStatusEnum.ON_ORDER.getCode()) {
                supplierReserve.setStatus(ReserveStatusEnum.RESERVED.getCode());
                supplierReserveMapper.updateById(supplierReserve);
            }
        }
        return res;
    }
}
