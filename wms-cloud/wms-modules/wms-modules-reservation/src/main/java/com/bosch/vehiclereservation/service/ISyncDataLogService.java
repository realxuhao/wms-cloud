package com.bosch.vehiclereservation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;

public interface ISyncDataLogService extends IService<SyncDataLog> {

    public SyncDataLog getLastSyncData();

}