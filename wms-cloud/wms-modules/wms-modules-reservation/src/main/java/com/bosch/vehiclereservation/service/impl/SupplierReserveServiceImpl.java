package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteTimeWindowService;
import com.bosch.masterdata.api.domain.Ware;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.api.enumeration.WinTimeStatusEnum;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierPorderDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierReserveDTO;
import com.bosch.vehiclereservation.api.domain.vo.PurchaseOrderVO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveVO;
import com.bosch.vehiclereservation.api.enumeration.OrderStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.mapper.DriverDeliverMapper;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.ISupplierPorderService;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierReserveServiceImpl extends ServiceImpl<SupplierReserveMapper, SupplierReserve> implements ISupplierReserveService {

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private DriverDeliverMapper driverDeliverMapper;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private RemoteTimeWindowService remoteTimeWindowService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Autowired
    private ISupplierPorderService supplierPorderService;

    @Override
    public List<TimeWindowVO> selectTimeWindowList(Long wareId, Date reserveDate) {
        R<List<TimeWindowVO>> result = remoteTimeWindowService.getListByWareId(wareId);
        List<TimeWindowVO> timeWindowVOList = result.getData();
        List<TimeWindowVO> data = timeWindowVOList.stream().filter(c -> c.getStatus().intValue() == WinTimeStatusEnum.ENABL.getCode()).collect(Collectors.toList());
        List<SupplierReserve> supplierReserves = supplierReserveMapper.selectReserveDateList(wareId, reserveDate);
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

    @Override
    public boolean insertSupplierReserve(SupplierDTO supplierDTO) {
        SupplierReserveDTO supplierReserveDTO = supplierDTO.getSupplierReserveDTO();
        /*String[] timeWindow = supplierReserveDTO.getTimeWindow().split("-");
        R<List<TimeWindowVO>> result = remoteTimeWindowService.getListByWareId(supplierReserveDTO.getWareId());
        List<TimeWindowVO> timeWindowVOList = result.getData();
        List<TimeWindowVO> data = timeWindowVOList.stream().filter(c -> c.getStatus().intValue() == WinTimeStatusEnum.ENABL.getCode()
                && c.getStartTime().equals(timeWindow[0]) && c.getEndTime().equals(timeWindow[1])).collect(Collectors.toList());
        if (data == null) {
            throw new ServiceException(supplierReserveDTO.getTimeWindow() + "该时段已约满");
        } else {
            List<SupplierReserve> supplierReserves = supplierReserveMapper.selectReserveDateList(supplierReserveDTO.getWareId(), supplierReserveDTO.getReserveDate());
            long count = Long.parseLong(data.get(0).getDockNum()) - supplierReserves.stream().filter(c -> c.getTimeWindow().equals(supplierReserveDTO.getTimeWindow())).count();
            if (count <= 0) {
                throw new ServiceException(supplierReserveDTO.getTimeWindow() + "该时段已约满");
            }
        }*/
        //获取当前日期
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String format = simpleDateFormat.format(date);
        SupplierReserve lastData = supplierReserveMapper.getLastData();
        int i = 1;
        if (!Objects.isNull(lastData)) {
            String datePrefix = lastData.getReserveNo().substring(0, 8);
            if (datePrefix.equals(format)) {
                String number = lastData.getReserveNo().substring(8);
                i = Integer.parseInt(number) + 1;
                if (i >= 1000) {
                    throw new ServiceException("今日订单已约满");
                }
            }
        }
        /**
         *左边补充空位,用0去补充
         * */
        NumberFormat nf = NumberFormat.getInstance();
        //设置是否使用分组
        nf.setGroupingUsed(false);
        //设置最大整数位数
        nf.setMaximumIntegerDigits(3);
        //设置最小整数位数
        nf.setMinimumIntegerDigits(3);
        String reserveNo = format + nf.format(i);
        supplierReserveDTO.setReserveNo(reserveNo);
        supplierReserveDTO.setSupplierCode(supplierDTO.getSupplierName());
        supplierReserveDTO.setStatus(ReserveStatusEnum.RESERVED.getCode());
        SupplierReserve supplierReserve = BeanConverUtil.conver(supplierReserveDTO, SupplierReserve.class);
        supplierReserve.setCreateTime(DateUtils.getNowDate());
        supplierReserve.setCreateBy(SecurityUtils.getUsername());

        List<SupplierPorderDTO> supplierPorderDTOList = supplierDTO.getSupplierPorderDTOS();
        supplierPorderDTOList.forEach(c -> {
            c.setReserveNo(supplierReserveDTO.getReserveNo());
            BigDecimal arriveQuantity = supplierPorderService.getArriveQuantityByPurchaseId(c.getPurchaseId());
            BigDecimal sum = arriveQuantity.add(c.getArriveQuantity());
            PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(c.getPurchaseId());
            if (!Objects.isNull(purchaseOrder)) {
                BigDecimal subtract = purchaseOrder.getQuantity().subtract(sum);
                if (subtract.compareTo(BigDecimal.ZERO) < 0) {
                    throw new ServiceException("实际送货数量超过剩余需求量");
                }
                c.setSurplusQuantity(purchaseOrder.getQuantity().subtract(arriveQuantity));
            }
        });

        boolean res = super.save(supplierReserve);
        if (res) {
            List<SupplierPorder> supplierPorderList = BeanConverUtil.converList(supplierPorderDTOList, SupplierPorder.class);
            supplierPorderList.forEach(c -> {
                c.setCreateTime(DateUtils.getNowDate());
                c.setCreateBy(SecurityUtils.getUsername());
            });
            boolean b = supplierPorderService.saveBatch(supplierPorderList);
            if (b) {
                changePurchaseOrderStatus(supplierPorderList);
            }
        }
        return res;
    }

    @Override
    public List<SupplierReserveVO> selectSupplierReserveVO(SupplierReserveDTO supplierReserveDTO) {
        if (StringUtils.isEmpty(supplierReserveDTO.getSupplierCode())) {
            //用户信息中获取供应商code
            //supplierReserveDTO.setSupplierCode("code");
        }
        SupplierReserve supplierReserve = BeanConverUtil.conver(supplierReserveDTO, SupplierReserve.class);
        List<SupplierReserve> selectSupplierReserveList = supplierReserveMapper.selectSupplierReserveList(supplierReserve);
        List<SupplierReserveVO> supplierReserveVOS = BeanConverUtil.converList(selectSupplierReserveList, SupplierReserveVO.class);
        Map<Long, Ware> wareMap = new HashMap<>();
        supplierReserveVOS.forEach(c -> {
            if (!wareMap.keySet().contains(c.getWareId())) {
                Ware wareInfo = remoteMasterDataService.getWareInfo(c.getWareId().toString()).getData();
                if (wareInfo != null) {
                    wareMap.put(c.getWareId(), wareInfo);
                }
            }
            if (wareMap.get(c.getWareId()) != null) {
                c.setWareName(wareMap.get(c.getWareId()).getName());
                c.setWareLocation(wareMap.get(c.getWareId()).getLocation());
                c.setWareUser(wareMap.get(c.getWareId()).getWareUser());
                c.setWareUserPhone(wareMap.get(c.getWareId()).getWareUserPhone());
            }
        });
        return supplierReserveVOS;
    }

    @Override
    public boolean deleteSupplierReserveById(Long reserveId) {
        SupplierReserve supplierReserve = super.getById(reserveId);
        if (supplierReserve == null) {
            throw new ServiceException("预约单不存在！");
        }
        if (supplierReserve.getStatus() != ReserveStatusEnum.RESERVED.getCode()) {
            throw new ServiceException("订单流程进行中，不允许删除！");
        }
        boolean res = super.removeById(reserveId);
        if (res) {
            QueryWrapper<SupplierPorder> wrapper = new QueryWrapper<>();
            wrapper.eq("reserve_no", supplierReserve.getReserveNo());
            List<SupplierPorder> supplierPorderList = supplierPorderService.list(wrapper);
            supplierPorderList.forEach(c -> {
                boolean b = supplierPorderService.removeById(c.getId());
                if (b) {
                    //更新同采购单id的剩余送货数量
                    Integer i = supplierPorderService.updateSurplusQuantityByPurchaseId(c.getPurchaseId(), c.getSurplusQuantity(), c.getArriveQuantity());
                    PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(c.getPurchaseId());
                    if (!Objects.isNull(purchaseOrder) && purchaseOrder.getStatus() == OrderStatusEnum.CLOSE.getCode()) {
                        purchaseOrder.setStatus(OrderStatusEnum.NORMAL.getCode());
                        purchaseOrderMapper.updateById(purchaseOrder);
                    }
                }
            });
        }
        return res;
    }

    @Override
    public List<PurchaseOrderVO> selectPurchaseOrderList(String reserveNo) {
        QueryWrapper<SupplierPorder> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve_no", reserveNo);
        List<SupplierPorder> supplierPorderList = supplierPorderService.list(wrapper);
        List<PurchaseOrder> purchaseOrders = purchaseOrderMapper.selectBatchIds(supplierPorderList.stream().map(c -> c.getPurchaseId()).collect(Collectors.toList()));
        List<PurchaseOrderVO> purchaseOrderVOS = BeanConverUtil.converList(purchaseOrders, PurchaseOrderVO.class);
        purchaseOrderVOS.forEach(c -> {
            List<SupplierPorder> supplierPorders = supplierPorderList.stream().filter(s -> s.getPurchaseId().equals(c.getPurchaseId())).collect(Collectors.toList());
            c.setArriveQuantity(supplierPorders.get(0).getArriveQuantity());
            c.setSurplusQuantity(supplierPorders.get(0).getSurplusQuantity());
        });
        return purchaseOrderVOS;
    }

    @Override
    public SupplierReserveVO selectDataByReserveNo(String reserveNo) {
        QueryWrapper<SupplierReserve> wrapperReserve = new QueryWrapper<>();
        wrapperReserve.eq("reserve_no", reserveNo);
        Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapperReserve).stream().findFirst();
        if (supplierReserve.isPresent()) {
            SupplierReserve reserve = supplierReserve.get();
            if (reserve.getStatus() > 0) {
                throw new ServiceException("该预约单已有司机预约，请联系客户！");
            }
            SupplierReserveVO voData = BeanConverUtil.conver(reserve, SupplierReserveVO.class);
            R<Ware> wareInfo = remoteMasterDataService.getWareInfo(reserve.getWareId().toString());
            Ware ware = wareInfo.getData();
            if (ware != null) {
                voData.setWareName(ware.getName());
                voData.setWareLocation(ware.getLocation());
                voData.setWareUser(ware.getWareUser());
                voData.setWareUserPhone(ware.getWareUserPhone());
            }
            return voData;
        } else {
            throw new ServiceException("该预约单号不存在！");
        }
    }


    public SupplierReserveVO selectDataByReserveNoForWx(String reserveNo) {
        QueryWrapper<SupplierReserve> wrapperReserve = new QueryWrapper<>();
        wrapperReserve.eq("reserve_no", reserveNo);
        Optional<SupplierReserve> supplierReserve = supplierReserveMapper.selectList(wrapperReserve).stream().findFirst();
        if (supplierReserve.isPresent()) {
            SupplierReserve reserve = supplierReserve.get();
            SupplierReserveVO voData = BeanConverUtil.conver(reserve, SupplierReserveVO.class);
            R<Ware> wareInfo = remoteMasterDataService.getWareInfo(reserve.getWareId().toString());
            Ware ware = wareInfo.getData();
            if (ware != null) {
                voData.setWareName(ware.getName());
                voData.setWareLocation(ware.getLocation());
                voData.setWareUser(ware.getWareUser());
                voData.setWareUserPhone(ware.getWareUserPhone());
            }
            return voData;
        } else {
            throw new ServiceException("该预约单号不存在！");
        }
    }

    private void changePurchaseOrderStatus(List<SupplierPorder> supplierPorderList) {
        supplierPorderList.forEach(c -> {
            BigDecimal sum = supplierPorderService.getArriveQuantityByPurchaseId(c.getPurchaseId());
            PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(c.getPurchaseId());
            if (!Objects.isNull(purchaseOrder)) {
                BigDecimal subtract = purchaseOrder.getQuantity().subtract(sum);
                if (subtract.compareTo(BigDecimal.ZERO) <= 0) {
                    purchaseOrder.setStatus(OrderStatusEnum.CLOSE.getCode());
                    purchaseOrderMapper.updateById(purchaseOrder);
                }
            }
        });
    }


}
