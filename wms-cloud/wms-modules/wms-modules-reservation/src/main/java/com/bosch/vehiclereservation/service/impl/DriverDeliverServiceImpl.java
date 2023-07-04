package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteTimeWindowService;
import com.bosch.masterdata.api.domain.dto.BlackDriverDTO;
import com.bosch.masterdata.api.domain.vo.BlackDriverVO;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.api.enumeration.WinTimeStatusEnum;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierOnTimeDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierOnTimeVO;
import com.bosch.vehiclereservation.api.enumeration.*;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.DriverDispatchMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.IDriverDeliverService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

@Service
public class DriverDeliverServiceImpl extends ServiceImpl<DriverDeliverMapper, DriverDeliver> implements IDriverDeliverService {

    @Autowired
    private DriverDeliverMapper driverDeliverMapper;

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Autowired
    private RemoteTimeWindowService remoteTimeWindowService;

    @Autowired
    private DriverDispatchMapper driverDispatchMapper;


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
        driverDeliver.setSelectType(1);
        List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverList(driverDeliver);
        List<DriverDeliverVO> driverDeliverVOS = BeanConverUtil.converList(driverDelivers, DriverDeliverVO.class);
        return driverDeliverVOS;
    }

    @Override
    public List<SupplierOnTimeVO> selectSupplierOnTime(SupplierOnTimeDTO supplierOnTimeDTO) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SupplierOnTimeVO> result = new ArrayList<>();
        String supplierName = supplierOnTimeDTO.getSupplierName();
        Integer year = supplierOnTimeDTO.getSearchYear();
        for(int i = 0; i < 12; i++){
            SupplierOnTimeVO supplier = new SupplierOnTimeVO();
            supplier.setSupplierName(supplierOnTimeDTO.getSupplierName());
            supplier.setYearOn(year);
            supplier.setMonthOn((i + 1));
            supplier.setTotalCount(0);
            supplier.setLateCount(0);
            supplier.setOnTimeRatio("0");
            result.add(supplier);
        }
        SupplierReserve supplierReserve = new SupplierReserve();
        supplierReserve.setSupplierCode(supplierName);
        List<String> selectReserveOnList = supplierReserveMapper.selectReserveOnList(supplierReserve);
        if(selectReserveOnList.isEmpty()){
            return result;
        }
        DriverDeliver driverDeliver = new DriverDeliver();
        driverDeliver.setReserveNoList(selectReserveOnList);
        driverDeliver.setSelectYear(year);
        List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverOnTimeList(driverDeliver);
        if(driverDelivers.isEmpty()){
            return result;
        }
        for(int i = 0; i < 12; i++){
            int finalI = i + 1;
            List<DriverDeliver> deliverFilterList = driverDelivers.stream().filter(
                    e -> {
                        LocalDateTime eDate = e.getReserveDate().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
                        if (eDate.getMonth().getValue() == finalI) {
                            return true;
                        }
                        return false;
                    }
            ).collect(Collectors.toList());
            if (!deliverFilterList.isEmpty()) {
                Long totalReserve = (long)deliverFilterList.size();
                Long onTimeCount = deliverFilterList.stream().filter(
                        e -> {
                            if(e.getSigninDate() == null){
                                return false;
                            }
                            String reserveDateStr = timeFormat.format(e.getReserveDate());
                            String windowLastTimeStr = e.getTimeWindow().split("-")[1];
                            LocalDateTime windowLastTime = LocalDateTime.parse(reserveDateStr + "T" + windowLastTimeStr + ":00");
                            return windowLastTime.isAfter(e.getSigninDate().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime());
                        }
                ).count();
                result.get(i).setOnTimeRatio(String.format("%.2f", ((onTimeCount.doubleValue() / totalReserve.doubleValue()) * 100)));
                result.get(i).setTotalCount(deliverFilterList.size());
                result.get(i).setLateCount((int)(totalReserve - onTimeCount));

            }
        }
        return result;
    }

    @Override
    public List<SupplierOnTimeVO> selectAllSupplierOnTimeList(Integer year) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<SupplierOnTimeVO> result = new ArrayList<>();
        List<String> supplierNameList = supplierReserveMapper.getSupplierName();
        supplierNameList.forEach(supplierName -> {
            List<SupplierOnTimeVO> addList = new ArrayList<>();
            for(int i = 0; i < 12; i++){
                SupplierOnTimeVO supplier = new SupplierOnTimeVO();
                supplier.setSupplierName(supplierName);
                supplier.setYearOn(year);
                supplier.setMonthOn((i + 1));
                supplier.setTotalCount(0);
                supplier.setLateCount(0);
                supplier.setOnTimeRatio("0");
                addList.add(supplier);
            }
            SupplierReserve supplierReserve = new SupplierReserve();
            supplierReserve.setSupplierCode(supplierName);
            List<String> selectReserveOnList = supplierReserveMapper.selectReserveOnList(supplierReserve);
            if(!selectReserveOnList.isEmpty()){
                DriverDeliver driverDeliver = new DriverDeliver();
                driverDeliver.setReserveNoList(selectReserveOnList);
                driverDeliver.setSelectYear(year);
                List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverOnTimeList(driverDeliver);
                if(!driverDelivers.isEmpty()){
                    for(int i = 0; i < 12; i++){
                        int finalI = i + 1;
                        List<DriverDeliver> deliverFilterList = driverDelivers.stream().filter(
                                e -> {
                                    LocalDateTime eDate = e.getReserveDate().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
                                    if (eDate.getMonth().getValue() == finalI) {
                                        return true;
                                    }
                                    return false;
                                }
                        ).collect(Collectors.toList());
                        if (!deliverFilterList.isEmpty()) {
                            Long totalReserve = (long)deliverFilterList.size();
                            Long onTimeCount = deliverFilterList.stream().filter(
                                    e -> {
                                        if(e.getSigninDate() == null){
                                            return false;
                                        }
                                        String reserveDateStr = timeFormat.format(e.getReserveDate());
                                        String windowLastTimeStr = e.getTimeWindow().split("-")[1];
                                        LocalDateTime windowLastTime = LocalDateTime.parse(reserveDateStr + "T" + windowLastTimeStr + ":00");
                                        return windowLastTime.isAfter(e.getSigninDate().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime());
                                    }
                            ).count();
                            addList.get(i).setOnTimeRatio(String.format("%.2f", ((onTimeCount.doubleValue() / totalReserve.doubleValue()) * 100)));
                            addList.get(i).setTotalCount(deliverFilterList.size());
                            addList.get(i).setLateCount((int)(totalReserve - onTimeCount));

                        }
                    }
                }
            }
            result.addAll(addList);
        });
        return result;
    }

    @Override
    public boolean deleteDriverDeliverById(Long deliverId) {
        DriverDeliver driverDeliver = super.getById(deliverId);
        if (driverDeliver == null) {
            throw new ServiceException("预约单不存在！");
        }
        if (driverDeliver.getStatus() != SignStatusEnum.NOT_SIGN.getCode()) {
            throw new ServiceException("司机已签到，不允许删除！");
        }
        boolean res = super.removeById(deliverId);
        if (res) {
            QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
            wrapper.eq("reserve_no", driverDeliver.getReserveNo());
            Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapper).stream().findFirst();
            if (supplierReserve.isPresent() && supplierReserve.get().getStatus() == ReserveStatusEnum.ON_ORDER.getCode()) {
                supplierReserve.get().setStatus(ReserveStatusEnum.RESERVED.getCode());
                supplierReserveMapper.updateById(supplierReserve.get());
            }
        }
      /*  QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve_no", driverDeliver.getReserveNo());
        Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapper).stream().findFirst();
        if (supplierReserve.isPresent() && supplierReserve.get().getStatus() == ReserveStatusEnum.COMPLETE.getCode()) {
            throw new ServiceException("订单已完成，不允许删除！");
        }
        boolean res = super.removeById(deliverId);
        if (res) {
            if (supplierReserve.isPresent()) {
                //已签到删除排队信息
                if (supplierReserve.get().getStatus() == ReserveStatusEnum.ARRIVAL.getCode()) {
                    QueryWrapper<DriverDispatch> wrapper1 = new QueryWrapper<>();
                    wrapper1.eq("driver_id", driverDeliver.getDeliverId());
                    Optional<DriverDispatch> driverDispatch = driverDispatchMapper.selectList(wrapper1).stream().findFirst();
                    if (driverDispatch.isPresent()) {
                        int i = driverDispatchMapper.deleteById(driverDispatch.get().getDispatchId());
                    }
                }
                supplierReserve.get().setStatus(ReserveStatusEnum.RESERVED.getCode());
                supplierReserveMapper.updateById(supplierReserve.get());
            }
        }*/
        return res;
    }

    @Override
    public boolean insertDriverDeliver(DriverDeliverDTO driverDeliverDTO) {
        R<List<BlackDriverVO>> result = remoteMasterDataService.getBlackDriverByWechatId(driverDeliverDTO.getWechatId(), true);
        List<BlackDriverVO> data = result.getData();
        if (data.size() > 0) {
            throw new ServiceException("您已进入黑名单，请联系客户确认！");
        }
        QueryWrapper<DriverDeliver> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve_no", driverDeliverDTO.getReserveNo());
        Optional<DriverDeliver> first = driverDeliverMapper.selectList(wrapper).stream().findFirst();
        if (first.isPresent()) {
            if (!first.get().getWechatId().equals(driverDeliverDTO.getWechatId())) {
                throw new ServiceException("该预约单已有司机预约，请联系客户！");
            } else {
                throw new ServiceException("该预约单您已预约过！");
            }
        }
        BlackDriverDTO blackDriverDTO = new BlackDriverDTO();
        blackDriverDTO.setWechatId(driverDeliverDTO.getWechatId());
        blackDriverDTO.setDriverName(driverDeliverDTO.getDriverName());
        blackDriverDTO.setDriverPhone(driverDeliverDTO.getDriverPhone());
        blackDriverDTO.setCarNum(driverDeliverDTO.getCarNum());
        R<Boolean> booleanR = remoteMasterDataService.saveBlackDriver(blackDriverDTO);

        String[] timeWindow = driverDeliverDTO.getTimeWindow().split("-");
        R<List<TimeWindowVO>> resultWindow = remoteTimeWindowService.getListByWareId(driverDeliverDTO.getWareId());
        List<TimeWindowVO> timeWindowVOList = resultWindow.getData();
        List<TimeWindowVO> dataWindow = timeWindowVOList.stream().filter(c -> c.getStatus().intValue() == WinTimeStatusEnum.ENABL.getCode()
                && c.getStartTime().equals(timeWindow[0]) && c.getEndTime().equals(timeWindow[1])).collect(Collectors.toList());
        if (dataWindow == null) {
            throw new ServiceException(driverDeliverDTO.getTimeWindow() + "该时段已约满");
        } else {
            List<SupplierReserve> supplierReserves = supplierReserveMapper.selectReserveDateList(driverDeliverDTO.getWareId(), driverDeliverDTO.getReserveDate());
            long count = Long.parseLong(dataWindow.get(0).getDockNum()) - supplierReserves.stream().filter(c -> c.getTimeWindow().equals(driverDeliverDTO.getTimeWindow())).count();
            if (count <= 0) {
                throw new ServiceException(driverDeliverDTO.getTimeWindow() + "该时段已约满");
            }
        }
        DriverDeliver driverDeliver = BeanConverUtil.conver(driverDeliverDTO, DriverDeliver.class);
        driverDeliver.setStatus(SignStatusEnum.NOT_SIGN.getCode());
        driverDeliver.setReserveType(ReserveTypeEnum.RESERVED.getCode());
        driverDeliver.setCreateTime(DateUtils.getNowDate());
        driverDeliver.setCreateBy(SecurityUtils.getUsername());
        boolean res = super.save(driverDeliver);
        if (res) {
            QueryWrapper<SupplierReserve> wrapperReserve = new QueryWrapper<>();
            wrapperReserve.eq("reserve_no", driverDeliver.getReserveNo());
            Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapperReserve).stream().findFirst();
            if (supplierReserve.isPresent() && supplierReserve.get().getStatus() == ReserveStatusEnum.RESERVED.getCode()) {
                supplierReserve.get().setStatus(ReserveStatusEnum.ON_ORDER.getCode());
                supplierReserveMapper.updateById(supplierReserve.get());
            }
        }
        return res;
    }

    @Override
    public List<DriverDeliverVO> selectDriverDeliverInfo(String wechatId) {
        DriverDeliver driverDeliver = new DriverDeliver();
        driverDeliver.setWechatId(wechatId);
        driverDeliver.setReserveType(ReserveTypeEnum.RESERVED.getCode());
        driverDeliver.setStatus(SignStatusEnum.NOT_SIGN.getCode());
        driverDeliver.setSelectType(0);
        List<DriverDeliver> driverDelivers = driverDeliverMapper.selectDriverDeliverList(driverDeliver);
        List<DriverDeliverVO> driverDeliverVOS = BeanConverUtil.converList(driverDelivers, DriverDeliverVO.class);
        Map<String, String> supplierMap = new HashMap<>();
        for (DriverDeliverVO driverDeliverVO : driverDeliverVOS) {
            if (!supplierMap.keySet().contains(driverDeliverVO.getSupplierCode())) {
                SupplierInfoVO supplierInfoVO = remoteMasterDataService.getSupplierInfoByCode(driverDeliverVO.getSupplierCode()).getData();
                if (supplierInfoVO != null) {
                    supplierMap.put(driverDeliverVO.getSupplierCode(), supplierInfoVO.getName());
                }
                driverDeliverVO.setSupplierName(supplierMap.get(driverDeliverVO.getSupplierCode()));
            }
        }
        return driverDeliverVOS;
    }

    @Override
    public boolean signIn(Long id) {
        DriverDeliver driverDeliver = driverDeliverMapper.selectById(id);
        if (driverDeliver == null) {
            throw new ServiceException("预约信息不存在！");
        }
        if (driverDeliver.getStatus() == SignStatusEnum.SIGNED.getCode()) {
            throw new ServiceException("您已签到！");
        }
        driverDeliver.setStatus(SignStatusEnum.SIGNED.getCode());
        driverDeliver.setSigninDate(DateUtils.getNowDate());
        QueryWrapper<SupplierReserve> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve_no", driverDeliver.getReserveNo());
        Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapper).stream().findFirst();
        if (supplierReserve.isPresent()) {
            // 查询供应商时间窗口余量
            int min = 0;
            String supplierCode = supplierReserve.get().getSupplierCode();
            R<SupplierInfoVO> result = remoteMasterDataService.getSupplierInfoByCode(supplierCode);
            if (result.getData() != null && result.getData().getTimeWindow() != null) {
                min = result.getData().getTimeWindow().intValue();
            }
            String[] split = driverDeliver.getTimeWindow().split("-");
            String dateTime = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, supplierReserve.get().getReserveDate()) + " " + split[1];
            Date date = DateUtils.addMinutes(DateUtils.parseDate(dateTime), min);
            if (DateUtils.getNowDate().after(date)) {
                driverDeliver.setLate(LateEnum.LATE.getCode());
            } else {
                driverDeliver.setLate(LateEnum.NOT_LATE.getCode());
            }
        }
        int i = driverDeliverMapper.updateById(driverDeliver);
        if (i > 0) {
            if (supplierReserve.isPresent() && supplierReserve.get().getStatus() == ReserveStatusEnum.ON_ORDER.getCode()) {
                supplierReserve.get().setStatus(ReserveStatusEnum.ARRIVAL.getCode());
                supplierReserveMapper.updateById(supplierReserve.get());
            }
            Long wareId = null;
            if (supplierReserve.isPresent() && supplierReserve.get().getWareId() != null) {
                wareId = supplierReserve.get().getWareId();
            }
            saveDriverDispatch(id, wareId);
        }
        return i > 0;
    }

    @Override
    public boolean signInDriverDeliver(DriverDeliverDTO driverDeliverDTO) {
        DriverDeliver driverDeliver = BeanConverUtil.conver(driverDeliverDTO, DriverDeliver.class);
        driverDeliver.setStatus(SignStatusEnum.SIGNED.getCode());
        driverDeliver.setReserveType(ReserveTypeEnum.NOT_RESERVE.getCode());
        driverDeliver.setSigninDate(DateUtils.getNowDate());
        driverDeliver.setCreateTime(DateUtils.getNowDate());
        driverDeliver.setCreateBy(SecurityUtils.getUsername());
        boolean res = super.save(driverDeliver);
        if (res) {
            saveDriverDispatch(driverDeliver.getDeliverId(), null);
        }
        return res;
    }

    private void saveDriverDispatch(Long id, Long wareId) {
        Integer maxSortNo = driverDispatchMapper.getMaxSortNo();
        DriverDispatch driverDispatch = new DriverDispatch();
        driverDispatch.setDriverId(id);
        driverDispatch.setWareId(wareId);
        driverDispatch.setSortNo(maxSortNo + 1);
        driverDispatch.setDriverType(DispatchTypeEnum.DELIVER.getCode());
        driverDispatch.setStatus(DispatchStatusEnum.WAITE.getCode());
        driverDispatch.setCreateTime(DateUtils.getNowDate());
        driverDispatch.setCreateBy(SecurityUtils.getUsername());
        driverDispatchMapper.insert(driverDispatch);
    }
}
