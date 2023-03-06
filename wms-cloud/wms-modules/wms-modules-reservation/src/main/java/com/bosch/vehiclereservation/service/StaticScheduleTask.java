package com.bosch.vehiclereservation.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.SyncDataLog;
import com.bosch.vehiclereservation.api.domain.dto.PoReqDTO;
import com.bosch.vehiclereservation.api.domain.dto.RecordDTO;
import com.bosch.vehiclereservation.api.domain.dto.WareHouseOrderDTO;
import com.bosch.vehiclereservation.utils.BeanConverUtil;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class StaticScheduleTask {

    @Autowired
    private ISyncDataService syncDataService;

    @Scheduled(cron = "0 1 0 * * ?")
    private void syncPurchaseOrderTasks() {
        System.out.println("执行定时任务时间: " + LocalDateTime.now());
        syncDataService.syncData();
    }


}
