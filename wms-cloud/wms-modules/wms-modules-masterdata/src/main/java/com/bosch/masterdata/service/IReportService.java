package com.bosch.masterdata.service;

import com.bosch.masterdata.api.domain.MissionToDo;

import java.util.List;

public interface IReportService {

    /**
     * 查询待办任务列表
     */
    public List<MissionToDo> selectReportList(MissionToDo mission);
}
