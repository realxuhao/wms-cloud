package com.bosch.product.service;


import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableScheduling
public class SudnScheduleTask {

    @Autowired
    private IProductPickService pickService;

    @Scheduled(cron = "0 15 0 * * ?") //   0/30 * * * * ?
    private void syncSudnTasks() {
        System.out.println("执行定时任务(销售订单)时间: " + LocalDateTime.now());
        LocalDate date = LocalDate.now().minusDays(1); // 获取当前日期前一天
        //LocalDate date = LocalDate.of(2024, 4, 26);
        LocalDateTime startOfDay = date.atStartOfDay(); // 当天的开始时间 00:00:00
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 当天的结束时间 23:59:59
        System.out.println("查询开始时间: " + startOfDay + "结束时间：" + endOfDay);
        pickService.exportSudnPickStockData(startOfDay, endOfDay);
    }


}
