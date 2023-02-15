package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteTimeWindowService;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.api.enumeration.WinTimeStatusEnum;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
import com.ruoyi.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SupplierReserveServiceImpl extends ServiceImpl<SupplierReserveMapper, SupplierReserve> implements ISupplierReserveService {

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private RemoteTimeWindowService remoteTimeWindowService;

    @Override
    public List<TimeWindowVO> selectTimeWindowList(Long wareId) {
        R<List<TimeWindowVO>> result = remoteTimeWindowService.getListByWareId(wareId);
        List<TimeWindowVO> timeWindowVOList = result.getData();
        List<TimeWindowVO> data = timeWindowVOList.stream().filter(c -> c.getStatus().intValue() == WinTimeStatusEnum.ENABL.getCode()).collect(Collectors.toList());
        List<SupplierReserve> supplierReserves = supplierReserveMapper.selectCurdateList(wareId);
        for (TimeWindowVO timeWindowVO : data) {
            String timeWindow = timeWindowVO.getStartTime() + "-" + timeWindowVO.getEndTime();
            long count = Long.parseLong(timeWindowVO.getDockNum()) - supplierReserves.stream().filter(c -> c.getTimeWindow().equals(timeWindow)).count();
            timeWindowVO.setDockNum(String.valueOf(count));
            if (count <= 0) {
                timeWindowVO.setStatus(WinTimeStatusEnum.DISABLE.getCode().longValue());
            } else {
                timeWindowVO.setStatus(WinTimeStatusEnum.ENABL.getCode().longValue());
            }
        }
        return data;
    }
}
