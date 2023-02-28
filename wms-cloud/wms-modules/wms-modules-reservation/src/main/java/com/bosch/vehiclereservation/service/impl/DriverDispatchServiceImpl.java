package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverSortDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.enumeration.DispatchStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.DispatchTypeEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DriverDispatchServiceImpl extends ServiceImpl<DriverDispatchMapper, DriverDispatch> implements IDriverDispatchService {

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;

    @Autowired
    private DriverDeliverMapper driverDeliverMapper;

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Override
    public List<DriverDispatchVO> selectTodaySignData(DriverDispatchDTO driverDispatchDTO) {
        List<DriverDispatchVO> driverDispatchVOS = driverDispatchMapper.selectTodaySignData(driverDispatchDTO.getWareId(), driverDispatchDTO.getStatusList(), driverDispatchDTO.isToday(), driverDispatchDTO.getDriverType());
        Map<Long, String> wareMap = new HashMap<>();
        Map<String, String> supplierMap = new HashMap<>();
        for (DriverDispatchVO dispatchVO : driverDispatchVOS) {
            if (dispatchVO.getWareId() != null) {
                if (!wareMap.keySet().contains(dispatchVO.getWareId())) {
                    Ware ware = remoteMasterDataService.getWareInfo(dispatchVO.getWareId().toString()).getData();
                    if (ware != null) {
                        wareMap.put(dispatchVO.getWareId(), ware.getCode());
                    }
                }
                dispatchVO.setWareCode(wareMap.get(dispatchVO.getWareId()));
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
        if (driverDispatch.getWareId() == null || StringUtils.isEmpty(driverDispatch.getDockCode())) {
            throw new ServiceException("请先分配仓库和道口！");
        }
        driverDispatch.setStatus(DispatchStatusEnum.ENTER.getCode());
        driverDispatch.setComeinDate(DateUtils.getNowDate());
        return driverDispatchMapper.updateById(driverDispatch) > 0;
    }

    @Override
    public boolean dispatchComplete(Long dispatchId) {
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(dispatchId);
        if (driverDispatch == null) {
            throw new ServiceException("调度信息不存在！");
        }
        if (driverDispatch.getStatus() != DispatchStatusEnum.ENTER.getCode()) {
            throw new ServiceException("车辆状态异常！");
        }
        driverDispatch.setCompleteDate(DateUtils.getNowDate());
        driverDispatch.setStatus(DispatchStatusEnum.COMPLETE.getCode());
        int i = driverDispatchMapper.updateById(driverDispatch);
        if (i > 0 && driverDispatch.getDriverType() == DispatchTypeEnum.DELIVER.getCode()) {
            Long driverId = driverDispatch.getDriverId();
            DriverDeliver driverDeliver = driverDeliverMapper.selectById(driverId);
            if (StringUtils.isNotEmpty(driverDeliver.getReserveNo())) {
                QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
                wrapper.eq("reserve_no", driverDeliver.getReserveNo());
                Optional<SupplierReserve> first = supplierReserveMapper.selectList(wrapper).stream().findFirst();
                if (first.isPresent()) {
                    SupplierReserve supplierReserve = first.get();
                    supplierReserve.setStatus(ReserveStatusEnum.COMPLETE.getCode());
                    supplierReserveMapper.updateById(supplierReserve);
                }
            }
        }
        return i > 0;
    }

    @Override
    public boolean dispatchSort(DriverSortDTO driverDispatchDTO) {
        if (driverDispatchDTO.getDispatchId() == null) {
            return false;
        }
        DriverDispatch driverDispatch = driverDispatchMapper.selectById(driverDispatchDTO.getDispatchId());
        if (driverDispatch == null) {
            return false;
        }
        if (driverDispatch.getSortNo() == driverDispatchDTO.getNewSortNo()) {
            return false;
        }
        Integer sortNO = driverDispatch.getSortNo();
        driverDispatch.setSortNo(driverDispatchDTO.getNewSortNo());
        int i = driverDispatchMapper.updateById(driverDispatch);
        if (i > 0) {
            if (sortNO > driverDispatchDTO.getNewSortNo()) {
                driverDispatchMapper.updateSortNo(driverDispatchDTO.getDispatchId(), driverDispatchDTO.getNewSortNo(), sortNO, "add");
            }
            if (sortNO < driverDispatchDTO.getNewSortNo()) {
                driverDispatchMapper.updateSortNo(driverDispatchDTO.getDispatchId(), sortNO, driverDispatchDTO.getNewSortNo(), "sub");
            }
        }
        return false;
    }
}
