package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;

import java.util.List;

public interface IReportService {

    /**
     * 查询待办任务列表
     */
    public List<MissionToDo> selectReportList(MissionToDo mission);


    /**
     * bin报表
     * @param reportBinDTO
     * @return
     */
    List<ReportBinVO> selectCellReportByType (ReportBinDTO reportBinDTO);
}
