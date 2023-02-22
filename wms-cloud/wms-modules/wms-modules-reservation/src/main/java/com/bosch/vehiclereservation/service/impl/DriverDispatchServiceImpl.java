package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.enumeration.DispatchStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverDispatchServiceImpl extends ServiceImpl<DriverDispatchMapper, DriverDispatch> implements IDriverDispatchService {

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Override
    public List<DriverDispatchVO> selectTodaySignData(Long wareId) {
        List<DriverDispatchVO> driverDispatchVOS = driverDispatchMapper.selectTodaySignData(wareId);
        Map<Long, String> wareMap = new HashMap<>();
        Map<String, String> supplierMap = new HashMap<>();
        for (DriverDispatchVO dispatchVO : driverDispatchVOS) {
            if (dispatchVO.getWareId() != null) {
                if (!wareMap.keySet().contains(dispatchVO.getWareId())) {
                    Ware ware = remoteMasterDataService.getWareInfo(dispatchVO.getWareId().toString()).getData();
                    if (ware != null) {
                        wareMap.put(dispatchVO.getWareId(), ware.getName());
                    }
                }
                dispatchVO.setWareName(wareMap.get(dispatchVO.getWareId()));
            }
            if (StringUtils.isNotEmpty(dispatchVO.getSupplierCode())) {
                if (!supplierMap.keySet().contains(dispatchVO.getSupplierCode())) {
                    SupplierInfoVO supplierInfoVO = remoteMasterDataService.getSupplierInfoByCode(dispatchVO.getSupplierCode()).getData();
                    if (supplierInfoVO != null) {
                        supplierMap.put(dispatchVO.getSupplierCode(), supplierInfoVO.getName());
                    }
                }
                dispatchVO.setSupplierName(supplierMap.get(dispatchVO.getSupplierCode()));
            }
        }
        return driverDispatchVOS;
    }

    @Override
    public List<DriverDispatchVO> selectTodayNotSignData() {
        return driverDispatchMapper.selectTodayNotSignData();
    }

    @Override
    public boolean dispatchDock(DriverDispatchDTO driverDispatchDTO) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(driverDispatchDTO.getDispatchId());
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        driverDispatch.setWareId(driverDispatchDTO.getWareId());
        driverDispatch.setDockCode(driverDispatchDTO.getDockCode());
        return driverDispatchMapper.updateById(driverDispatch) > 0;
    }

    @Override
    public boolean dispatchEnter(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        driverDispatch.setStatus(DispatchStatusEnum.ENTER.getCode());
        return driverDispatchMapper.updateById(driverDispatch) > 0;
    }
}
