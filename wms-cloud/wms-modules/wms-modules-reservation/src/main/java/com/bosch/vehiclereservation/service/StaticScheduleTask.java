package com.bosch.vehiclereservation.service;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class StaticScheduleTask {

    /*@Scheduled(cron = "0/5 * * * * ?")
    private void syncPurchaseOrderTasks() {
        System.err.println("执行定时任务时间: " + LocalDateTime.now());
    }*/
}
