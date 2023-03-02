package com.bosch.vehiclereservation.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;
import com.bosch.vehiclereservation.api.domain.dto.PoReqDTO;
import com.bosch.vehiclereservation.api.domain.dto.RecordDTO;
import com.bosch.vehiclereservation.mapper.PurchaseOrderMapper;
import com.bosch.vehiclereservation.mapper.SyncDataLogMapper;
import com.bosch.vehiclereservation.service.ISyncDataService;
import com.ruoyi.common.core.exception.ServiceException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static javax.xml.transform.OutputKeys.MEDIA_TYPE;

@Service
public class SyncDataServiceImpl extends ServiceImpl<SyncDataLogMapper, SyncDataLog> implements ISyncDataService {

    @Autowired
    private SyncDataLogMapper syncDataLogMapper;

    @Override
    public SyncDataLog getLastSyncData() {
        return syncDataLogMapper.getLastSyncData();
    }
}
