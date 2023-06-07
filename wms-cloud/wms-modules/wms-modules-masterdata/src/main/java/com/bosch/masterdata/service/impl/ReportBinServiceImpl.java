package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.ReportBin;
import com.bosch.masterdata.service.IReportBinService;
import com.bosch.masterdata.mapper.ReportBinMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【report_bin(库位占用报表)】的数据库操作Service实现
* @createDate 2023-06-06 15:09:12
*/
@Service
@Slf4j
public class ReportBinServiceImpl extends ServiceImpl<ReportBinMapper, ReportBin>
    implements IReportBinService {

    @Autowired
    private ReportBinMapper reportBinMapper;


    @Override
    public List<ReportBin>  genBinOccupy() {
        List<ReportBin> reportBins = reportBinMapper.selectBinReport();
        try {
            if(!CollectionUtils.isEmpty(reportBins)){
                for (ReportBin reportBin : reportBins) {
                    int mb = reportBin.getMaterialOccupyBin() != null ? reportBin.getMaterialOccupyBin() : 0;
                    int pb = reportBin.getProductOccupyBin() != null ? reportBin.getProductOccupyBin() : 0;
                    BigDecimal used = new BigDecimal(mb+pb);
                    BigDecimal all = new BigDecimal(reportBin.getTotalBin()!= null ? reportBin.getTotalBin() : 0);
                    BigDecimal divide = used.divide(all, 2, BigDecimal.ROUND_HALF_UP);
                    reportBin.setPercent(divide);
                    reportBin.setCreateTime(LocalDateTime.now());
                }
            }
        }catch (Exception e){
            log.error("定时任务获取库位异常："+e.getMessage()+" 时间："+LocalDateTime.now());
        }
        return reportBins;
    }
}




