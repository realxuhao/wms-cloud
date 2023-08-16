package com.bosch.masterdata.utils;

import com.bosch.masterdata.api.domain.ReportBin;
import com.bosch.masterdata.service.IReportBinService;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.service.IProductStockDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class BinScheduleTask  {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IReportBinService binService;

    @Autowired
    private IProductStockDetailService productStockDetailService;
    @Scheduled(cron = "0 0 1 * * ?")
    //五分钟执行一次
    //@Scheduled(cron = "0 0/5 * * * ?")
    public boolean genBinOccupy(){
        List<ReportBin> reportBins = binService.genBinOccupy();
        boolean b = binService.saveBatch(reportBins);
        return b;
    }
    //@Scheduled(cron = "0 0 1 * * ?")
    //每一分执行一次
    //@Scheduled(cron = "0 0 1 * * ?")
    //每天23：59：00执行
    @Scheduled(cron = "0 59 23 * * ?")
    public void genProStock(){
        try {
            productStockDetailService.genProStock();
        } catch (Exception e) {
            logger.error("库存期初期末Scheduled:"+e.getMessage());
        }

    }
}
