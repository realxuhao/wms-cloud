package com.bosch.vehiclereservation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;
import com.bosch.vehiclereservation.api.domain.dto.PoReqDTO;
import com.bosch.vehiclereservation.api.domain.dto.RecordDTO;
import com.bosch.vehiclereservation.api.domain.dto.WareHouseOrderDTO;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
import com.bosch.vehiclereservation.service.ISyncDataLogService;
import com.bosch.vehiclereservation.service.ISyncDataService;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SyncDataServiceImpl implements ISyncDataService {

    @Autowired
    private ISyncDataLogService syncDataLogService;

    @Autowired
    private IPurchaseOrderService purchaseOrderService;

    @Value("${apiurl.purchase-order}")
    private String purchaseOrderUrl;


    @Override
    public boolean syncData() {
        SyncDataLog syncDataLog = new SyncDataLog();
        syncDataLog.setSyncStartDate(DateUtils.getNowDate());
        syncDataLog.setSyncLastDate(DateUtils.getNowDate());
        int pageIndex = 1;
        int pageSize = 100;
        SyncDataLog lastSyncData = syncDataLogService.getLastSyncData();
        PoReqDTO reqDTO = new PoReqDTO();
        reqDTO.setSize(pageSize);
        reqDTO.setCurrent(pageIndex);
        if (lastSyncData != null) {
            reqDTO.setUpdateDate(lastSyncData.getSyncLastDate());
        }
        try {
            while (true) {
                String body = JSON.toJSONString(reqDTO);
                RequestBody reqBody = RequestBody.create(MediaType.parse("application/json"), body);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(purchaseOrderUrl).post(reqBody).build();
                Call call = okHttpClient.newCall(request);
                Response resp = call.execute();
                String respBody = resp.body().string();
                WareHouseOrderDTO wareHouseOrderDTO = JSON.parseObject(respBody, WareHouseOrderDTO.class);
                if (wareHouseOrderDTO.getRecords().size() == 0) {
                    break;
                }
                if (lastSyncData == null) {
                    initPurchaseOrders(wareHouseOrderDTO.getRecords());
                } else {
                    comparePurchaseOrders(wareHouseOrderDTO.getRecords());
                }
                pageIndex++;
                reqDTO.setCurrent(pageIndex);
            }
        } catch (IOException e) {
            throw new ServiceException("供应商采购单接口调用失败！");
        }
        syncDataLog.setSyncEndDate(DateUtils.getNowDate());
        return syncDataLogService.save(syncDataLog);
    }

    /**
     * 初始化数据
     *
     * @param recordDTOList
     */
    private void initPurchaseOrders(List<RecordDTO> recordDTOList) {
        List<RecordDTO> lst = recordDTOList.stream().filter(c -> c.getDeleteFlag() == 0).collect(Collectors.toList());
        List<PurchaseOrder> purchaseOrders = BeanConverUtil.converList(lst, PurchaseOrder.class);
        purchaseOrders.forEach(c -> {
            c.setStatus(0);
        });
        purchaseOrderService.saveBatch(purchaseOrders);
    }

    /**
     * 增量比较数据
     *
     * @param recordDTOList
     */
    private void comparePurchaseOrders(List<RecordDTO> recordDTOList) {
        for (RecordDTO recordDTO : recordDTOList) {
            QueryWrapper<PurchaseOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("po_code", recordDTO.getPoNo());
            wrapper.eq("po_item", recordDTO.getPoItem());
            Optional<PurchaseOrder> first = purchaseOrderService.list(wrapper).stream().findFirst();
            if (first.isPresent()) {
                PurchaseOrder purchaseOrder = first.get();
                PurchaseOrder purchaseOrderUpdate = BeanConverUtil.conver(recordDTO, PurchaseOrder.class);
                purchaseOrderUpdate.setPurchaseId(purchaseOrder.getPurchaseId());
                purchaseOrderUpdate.setStatus(purchaseOrder.getStatus());
                purchaseOrderService.saveOrUpdate(purchaseOrderUpdate);
            } else {
                PurchaseOrder purchaseOrder = BeanConverUtil.conver(recordDTO, PurchaseOrder.class);
                purchaseOrder.setStatus(0);
                purchaseOrderService.save(purchaseOrder);
            }
        }
    }
}
