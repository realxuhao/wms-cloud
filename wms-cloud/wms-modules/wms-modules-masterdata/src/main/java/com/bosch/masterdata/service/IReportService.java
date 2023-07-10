package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.ReportMaterial;
import com.bosch.masterdata.api.domain.ReportWareShift;
import com.bosch.masterdata.api.domain.dto.*;
import com.bosch.masterdata.api.domain.vo.ProInOutStockVO;
import com.bosch.masterdata.api.domain.vo.ProcessEfficiencyVO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.api.domain.vo.WorkloadVO;

import java.util.List;

public interface IReportService {

    /**
     * 查询待办任务列表
     */
    public List<MissionToDo> selectReportList(MissionToDo mission);

    /**
     * 查询成品待办任务列表
     */
    public List<MissionToDo> selectProReportList(MissionToDo mission);

    /**
     * bin报表
     * @param reportBinDTO
     * @return
     */
    List<ReportBinVO> selectCellReportByType (ReportBinDTO reportBinDTO);

    List<ReportMaterial> oldMaterial();

    List<ReportMaterial> expiredMaterial();

    List<ReportWareShift> reportWareShift(ReportWareShiftDTO reportWareShiftDTO);

    List<WorkloadVO> workload(WorkloadDTO workloadDTO);

    List<WorkloadVO> workloadPro(WorkloadDTO workloadDTO);

    List<ProcessEfficiencyVO> processEfficiency(EfficiencyDTO efficiencyDTO);

    List<ProInOutStockVO> proInOutStock(ProInOutStockDTO proInOutStockDTO);
}
