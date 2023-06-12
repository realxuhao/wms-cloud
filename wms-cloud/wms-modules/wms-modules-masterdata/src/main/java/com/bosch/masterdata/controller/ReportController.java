package com.bosch.masterdata.controller;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.service.IReportService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
@Api(tags = "报表接口")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @PostMapping("/bin")
    @ApiOperation("bin报表")
    public R<List<ReportBinVO>> selectCellReportByType(@RequestBody ReportBinDTO reportBinDTO)
    {
        List<ReportBinVO> reportBinVOS = reportService.selectCellReportByType(reportBinDTO);
        return R.ok(reportBinVOS);
    }
    @PostMapping("/getMissionToDo")
    @ApiOperation("待办任务")
    public R<List<MissionToDo>> getMissionToDo(MissionToDo mission)
    {
        List<MissionToDo> missionToDoList = null;
        if (mission!=null || StringUtils.isNotEmpty(mission.getCell())||StringUtils.isNotEmpty(mission.getWareCode())){
            throw new RuntimeException("请选择cell或仓库");
        }
        List<MissionToDo> list = reportService.selectReportList(mission);
        return R.ok(missionToDoList);
    }
}
