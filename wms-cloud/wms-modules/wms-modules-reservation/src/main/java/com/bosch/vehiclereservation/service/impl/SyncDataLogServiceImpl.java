package com.bosch.vehiclereservation.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;
import com.bosch.vehiclereservation.mapper.SyncDataLogMapper;
import com.bosch.vehiclereservation.service.ISyncDataLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncDataLogServiceImpl extends ServiceImpl<SyncDataLogMapper, SyncDataLog> implements ISyncDataLogService {

    @Autowired
    private SyncDataLogMapper syncDataLogMapper;

    @Override
    public SyncDataLog getLastSyncData() {
        return syncDataLogMapper.getLastSyncData();
    }
}
