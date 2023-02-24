package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.BlackDriverVO;
import com.bosch.masterdata.api.domain.vo.SupplierInfoVO;
import com.bosch.vehiclereservation.api.domain.DriverDeliver;
import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
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
    public boolean deleteDriverDeliverById(Long deliverId) {
        DriverDeliver driverDeliver = super.getById(deliverId);
        if (driverDeliver == null) {
            throw new ServiceException("预约单不存在！");
        }
        if (driverDeliver.getStatus() != SignStatusEnum.NOT_SIGN.getCode()) {
            throw new ServiceException("订单已到货，不允许删除！");
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
        return res;
    }

    @Override
    public boolean insertDriverDeliver(DriverDeliverDTO driverDeliverDTO) {
        R<List<BlackDriverVO>> result = remoteMasterDataService.getBlackDriverByName(driverDeliverDTO.getDriverName());
        List<BlackDriverVO> data = result.getData();
        if (data.size() > 0) {
            throw new ServiceException("您已进入黑名单，请联系客户确认！");
        }
        QueryWrapper<DriverDeliver> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve_no", driverDeliverDTO.getReserveNo());
        Optional<DriverDeliver> first = driverDeliverMapper.selectList(wrapper).stream().findFirst();
        if (first.isPresent()) {
            if (!first.get().getDriverName().equals(driverDeliverDTO.getDriverName())) {
                throw new ServiceException("该预约单已有司机预约，请联系客户！");
            } else {
                throw new ServiceException("该预约单您已预约过！");
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
            String[] split = supplierReserve.get().getTimeWindow().split("-");
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
        DriverDispatch driverDispatch = new DriverDispatch();
        driverDispatch.setDriverId(id);
        driverDispatch.setWareId(wareId);
        driverDispatch.setDriverType(DispatchTypeEnum.DELIVER.getCode());
        driverDispatch.setStatus(DispatchStatusEnum.WAITE.getCode());
        driverDispatch.setCreateTime(DateUtils.getNowDate());
        driverDispatch.setCreateBy(SecurityUtils.getUsername());
        driverDispatchMapper.insert(driverDispatch);
    }
}
