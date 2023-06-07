package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.ReportBin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【report_bin(库位占用报表)】的数据库操作Service
* @createDate 2023-06-06 15:09:12
*/
public interface IReportBinService extends IService<ReportBin> {

    List<ReportBin> genBinOccupy();

}
