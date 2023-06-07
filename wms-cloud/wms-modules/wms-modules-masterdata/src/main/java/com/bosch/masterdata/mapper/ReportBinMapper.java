package com.bosch.masterdata.mapper;

import com.bosch.masterdata.api.domain.ReportBin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【report_bin(库位占用报表)】的数据库操作Mapper
* @createDate 2023-06-06 15:09:12
* @Entity com.bosch.masterdata.api.domain.ReportBin
*/
public interface ReportBinMapper extends BaseMapper<ReportBin> {

   List<ReportBin> selectBinReport();
}




