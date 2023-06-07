package com.bosch.masterdata.utils;

import com.bosch.masterdata.api.domain.ReportBin;
import com.bosch.masterdata.service.IReportBinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class BinScheduleTask {

    @Autowired
    private IReportBinService binService;

    @Scheduled(cron = "0 0 1 * * ?")
    public boolean genBinOccupy(){
        List<ReportBin> reportBins = binService.genBinOccupy();
        boolean b = binService.saveBatch(reportBins);
        return b;
    }
}
