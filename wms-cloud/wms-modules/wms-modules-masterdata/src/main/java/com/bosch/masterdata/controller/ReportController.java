package com.bosch.masterdata.controller;
import java.util.ArrayList;
import java.util.Date;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.ReportMaterial;
import com.bosch.masterdata.api.domain.ReportWareShift;
import com.bosch.masterdata.api.domain.dto.ReportBinDTO;
import com.bosch.masterdata.api.domain.dto.ReportMaterialDTO;
import com.bosch.masterdata.api.domain.dto.ReportWareShiftDTO;
import com.bosch.masterdata.api.domain.dto.WorkloadDTO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.domain.vo.ReportBinVO;
import com.bosch.masterdata.api.domain.vo.WorkloadVO;
import com.bosch.masterdata.service.IReportService;
import com.bosch.system.api.domain.SysUser;
import com.bosch.system.api.domain.UserOperationLog;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/report")
@Api(tags = "报表接口")
public class ReportController extends BaseController {

    @Autowired
    private IReportService reportService;

    @Autowired
    private IUserOperationLogService iUserOperationLogService;
    @PostMapping("/bin")
    @ApiOperation("bin报表")
    public R<List<ReportBinVO>> selectCellReportByType(@RequestBody ReportBinDTO reportBinDTO)
    {
        List<ReportBinVO> reportBinVOS = reportService.selectCellReportByType(reportBinDTO);
        return R.ok(reportBinVOS);
    }
    @PostMapping("/getMissionToDo")
    @ApiOperation("待办任务")
    public R<List<MissionToDo>> getMissionToDo(@RequestBody MissionToDo mission)
    {

        if (mission!=null && StringUtils.isEmpty(mission.getCell())&& StringUtils.isEmpty(mission.getWareCode())){
            throw new RuntimeException("请选择cell或仓库");
        }
        List<MissionToDo> list = reportService.selectReportList(mission);
        return R.ok(list);
    }

    @PostMapping("/oldMaterial")
    @ApiOperation("在库时间最长的物料")
    public R<PageVO<ReportMaterial>> oldMaterial(@RequestBody ReportMaterialDTO reportMaterialDTO)
    {
        startPage();
        List<ReportMaterial> reportMaterials = reportService.oldMaterial();
        return R.ok(new PageVO<>(reportMaterials, new PageInfo<>(reportMaterials).getTotal()));
    }
    /**
     * 导出列表
     */
    @PostMapping("/oldMaterialExport")
    @ApiOperation("在库时间最长的物料列表导出")
    public void oldMaterialExport(HttpServletResponse response) {

        List<ReportMaterial> reportMaterials = reportService.oldMaterial();
        ExcelUtil<ReportMaterial> util = new ExcelUtil<>(ReportMaterial.class);
        util.exportExcel(response, reportMaterials, "在库时间最长的物料列表");

    }

    @PostMapping("/expiredMaterial")
    @ApiOperation("有效期30天")
    public R<PageVO<ReportMaterial>> expiredMaterial(@RequestBody ReportMaterialDTO reportMaterialDTO)
    {
        startPage();
        List<ReportMaterial> reportMaterials = reportService.expiredMaterial();
        return R.ok(new PageVO<>(reportMaterials, new PageInfo<>(reportMaterials).getTotal()));
    }
    /**
     * 导出列表
     */
    @PostMapping("/expiredMaterialExport")
    @ApiOperation("有效期30天列表导出")
    public void expiredMaterialExport(HttpServletResponse response) {

        List<ReportMaterial> reportMaterials = reportService.expiredMaterial();
        ExcelUtil<ReportMaterial> util = new ExcelUtil<>(ReportMaterial.class);
        util.exportExcel(response, reportMaterials, "有效期30天列表");

    }
    /**
     * 移库列表
     */
    @PostMapping("/wareShift")
    @ApiOperation("移库列表")
    public  R<PageVO<ReportWareShift>> wareShift(@RequestBody ReportWareShiftDTO reportWareShiftDTO) {

        startPage();

        List<ReportWareShift> reportWareShifts = reportService.reportWareShift(reportWareShiftDTO);

        return R.ok(new PageVO<>(reportWareShifts, new PageInfo<>(reportWareShifts).getTotal()));

    }
    /**
     * 员工实际工作量（原材料+成品）-标准单位托盘
     */
    @PostMapping("/workload")
    @ApiOperation("员工实际工作量（原材料+成品）-标准单位托盘")
    public  R<PageVO<WorkloadVO>> workload(@RequestBody WorkloadDTO workloadDTO) {

        startPage();
        List<WorkloadVO> list = reportService.workload(workloadDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }
    /**
     * 员工实际工作量（原材料+成品）-标准单位托盘导出
     */
    @PostMapping("/workloadExport")
    @ApiOperation("员工实际工作量（原材料+成品）-标准单位托盘导出")
    public  void workloadExport(HttpServletResponse response ) {
        WorkloadDTO workloadDTO=new WorkloadDTO();
        List<WorkloadVO> list = reportService.workload(workloadDTO);
        ExcelUtil<WorkloadVO> util = new ExcelUtil<>(WorkloadVO.class);
        util.exportExcel(response, list, "员工实际工作量");

    }
}
