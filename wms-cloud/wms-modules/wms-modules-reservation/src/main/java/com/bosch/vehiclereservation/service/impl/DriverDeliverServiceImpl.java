package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDeliverService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            driverDeliver.setReserveNoList(reserveNoList);
        }
        startPage();
        List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverList(driverDeliver);
        List<DriverDeliverVO> driverDeliverVOS = BeanConverUtil.converList(driverDelivers, DriverDeliverVO.class);
        return driverDeliverVOS;
    }
}
