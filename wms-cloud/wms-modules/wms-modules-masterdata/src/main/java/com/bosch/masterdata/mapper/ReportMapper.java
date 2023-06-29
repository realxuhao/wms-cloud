package com.bosch.masterdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.*;
import com.bosch.masterdata.api.domain.dto.AreaDTO;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.dto.ReportWareShiftDTO;
import com.bosch.masterdata.api.domain.dto.WorkloadDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.api.domain.vo.WorkloadVO;
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
}
