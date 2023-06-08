package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.service.IReportService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
@Api(tags = "报表接口")
public class ReportController {

    @Autowired
    private IReportService reportService;
    @GetMapping("/getMissionToDo")
    @ApiOperation("待办任务")
    public R<List<MissionToDo>> getMissionToDo(MissionToDo mission)
    {
        List<MissionToDo> missionToDoList = null;
        List<MissionToDo> list = reportService.selectReportList(mission);
        return R.ok(missionToDoList);
    }
}
