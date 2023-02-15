package com.bosch.vehiclereservation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteTimeWindowService;
import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.masterdata.api.enumeration.WinTimeStatusEnum;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.SupplierReserve;
import com.bosch.vehiclereservation.api.domain.SupplierPorder;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierPorderDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierReserveDTO;
import com.bosch.vehiclereservation.api.enumeration.OrderStatusEnum;
import com.bosch.vehiclereservation.api.enumeration.ReserveStatusEnum;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.mapper.SupplierReserveMapper;
import com.bosch.vehiclereservation.service.ISupplierPorderService;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SupplierReserveServiceImpl extends ServiceImpl<SupplierReserveMapper, SupplierReserve> implements ISupplierReserveService {

    @Autowired
    private SupplierReserveMapper supplierReserveMapper;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private RemoteTimeWindowService remoteTimeWindowService;

    @Autowired
    private ISupplierPorderService supplierPorderService;

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

    @Override
    public boolean insertSupplierReserve(SupplierDTO supplierDTO) {
        SupplierReserveDTO supplierReserveDTO = supplierDTO.getSupplierReserveDTO();
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
