package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.*;
import com.bosch.masterdata.api.domain.dto.*;
import com.bosch.masterdata.api.domain.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
public interface ReportMapper
{

    List<ReportBinVO> selectCellReportByType (ReportBinDTO reportBinDTO);

    List<ReportBinVO> selectCellReportByMonth (ReportBinDTO reportBinDTO);

//    List<ReportBinVO> selectReportBinByMonth (ReportBinDTO reportBinDTO);
//    List<ReportBinVO> selectReportBinByDay (ReportBinDTO reportBinDTO);
    List<String> getCellCode();
    List<String> getWareCode();
    List<MissionMap> toBeReceived(@Param("cell") String cell,@Param("wareCode") String wareCode);
    List<MissionMap> toBeBin(@Param("cell") String cell,@Param("wareCode") String wareCode);
    //叫料
    List<MissionMap> toBeCall(@Param("cell") String cell,@Param("wareCode") String wareCode);
    //移库
    List<MissionMap> toBeMove(@Param("cell") String cell,@Param("wareCode") String wareCode);


    List<ReportMaterial> oldMaterial();

    List<ReportMaterial> expiredMaterial();

    List<ReportWareShift> reportWareShift(ReportWareShiftDTO reportWareShiftDTO);

    List<WorkloadVO> workload(WorkloadDTO workloadDTO);

    List<EfficiencyVO> getEfficiencyByOperationType(EfficiencyDTO efficiencyDTO);

    //无时间筛选
    List<EfficiencyVO> getEfficiencyByOperationTwo(EfficiencyDTO efficiencyDTO);
    List<EfficiencyVO> getCallOver(EfficiencyDTO efficiencyDTO);
}
