package com.bosch.masterdata.controller;
import java.util.*;

import com.bosch.masterdata.api.domain.MissionToDo;
import com.bosch.masterdata.api.domain.ReportMaterial;
import com.bosch.masterdata.api.domain.ReportWareShift;
import com.bosch.masterdata.api.domain.dto.*;
import com.bosch.masterdata.api.domain.vo.*;
import com.bosch.masterdata.service.IReportService;
import com.bosch.masterdata.utils.BeanConverUtil;
import com.bosch.system.api.domain.SysUser;
import com.bosch.system.api.domain.UserOperationLog;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

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
    @ApiOperation("原材料待办任务")
    public R<List<MissionToDo>> getMissionToDo(@RequestBody MissionToDo mission)
    {

        if (mission!=null && StringUtils.isEmpty(mission.getCell())&& StringUtils.isEmpty(mission.getWareCode())){
            throw new RuntimeException("请选择cell或仓库");
        }
        List<MissionToDo> list = reportService.selectReportList(mission);
        return R.ok(list);
    }
    @PostMapping("/getMissionToDoPro")
    @ApiOperation("成品待办任务")
    public R<List<MissionToDo>> getMissionToDoPro(@RequestBody MissionToDo mission)
    {

        if (mission!=null && StringUtils.isEmpty(mission.getCell())&& StringUtils.isEmpty(mission.getWareCode())){
            throw new RuntimeException("请选择cell或仓库");
        }
        List<MissionToDo> list = reportService.selectProReportList(mission);
        return R.ok(list);
    }
    @PostMapping("/oldMaterial")
    @ApiOperation("在库时间最长的物料")
    public R<PageVO<ReportMaterial>> oldMaterial(@RequestBody ReportMaterialDTO reportMaterialDTO)
    {
        PageDomain pageDomain= BeanConverUtil.conver(reportMaterialDTO,PageDomain.class);
        startPage(pageDomain);
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
        PageDomain pageDomain= BeanConverUtil.conver(reportMaterialDTO,PageDomain.class);
        startPage(pageDomain);
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

        PageDomain pageDomain= BeanConverUtil.conver(reportWareShiftDTO,PageDomain.class);
        startPage(pageDomain);

        List<ReportWareShift> reportWareShifts = reportService.reportWareShift(reportWareShiftDTO);

        return R.ok(new PageVO<>(reportWareShifts, new PageInfo<>(reportWareShifts).getTotal()));

    }
    /**
     * 员工实际工作量（原材料）-标准单位托盘
     */
    @PostMapping("/workload")
    @ApiOperation("员工实际工作量（原材料）-标准单位托盘")
    public  R<PageVO<WorkloadVO>> workload(@RequestBody WorkloadDTO workloadDTO) {

        PageDomain pageDomain= BeanConverUtil.conver(workloadDTO,PageDomain.class);
        startPage(pageDomain);
        List<WorkloadVO> list = reportService.workload(workloadDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }
    /**
     * 员工实际工作量（成品）-标准单位托盘
     */
    @PostMapping("/workloadPro")
    @ApiOperation("员工实际工作量（成品）-标准单位托盘")
    public  R<PageVO<WorkloadVO>> workloadPro(@RequestBody WorkloadDTO workloadDTO) {

        PageDomain pageDomain= BeanConverUtil.conver(workloadDTO,PageDomain.class);
        startPage(pageDomain);
        List<WorkloadVO> list = reportService.workloadPro(workloadDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }
    /**
     * 员工实际工作量（原材料+成品）-标准单位托盘导出
     */
    @PostMapping("/workloadExport")
    @ApiOperation("员工实际工作量（原材料）-标准单位托盘导出")
    public  void workloadExport(HttpServletResponse response ) {
        WorkloadDTO workloadDTO=new WorkloadDTO();
        List<WorkloadVO> list = reportService.workload(workloadDTO);
        ExcelUtil<WorkloadVO> util = new ExcelUtil<>(WorkloadVO.class);
        util.exportExcel(response, list, "员工实际工作量（原材料）-标准单位托盘导出");

    }
    /**
     * 员工实际工作量（原材料+成品）-标准单位托盘导出
     */
    @PostMapping("/workloadExportPro")
    @ApiOperation("员工实际工作量（成品）-标准单位托盘导出")
    public  void workloadProExport(HttpServletResponse response ) {
        WorkloadDTO workloadDTO=new WorkloadDTO();
        List<WorkloadVO> list = reportService.workloadPro(workloadDTO);
        ExcelUtil<WorkloadVO> util = new ExcelUtil<>(WorkloadVO.class);
        util.exportExcel(response, list, "员工实际工作量（成品）-标准单位托盘导出");

    }
    /**
     * 流程效率-按作业类型计算流程平均PT
     */
    @PostMapping("/processEfficiency")
    @ApiOperation("流程效率-按作业类型计算流程平均PT")
    public  R<List<ProcessEfficiencyVO>> processEfficiency(@RequestBody EfficiencyDTO processEfficiencyDTO) {

//            startPage();
            List<ProcessEfficiencyVO> list = reportService.processEfficiency(processEfficiencyDTO);
        List<ProcessEfficiencyVO> sortedList = list.stream()
                .sorted(Comparator.comparing(ProcessEfficiencyVO::getCreateTime))
                .collect(Collectors.toList());
            return R.ok(sortedList);

    }
    /**
     * 成品报表-进销存量
     */
    @PostMapping("/proInOutStock")
    @ApiOperation("成品报表-进销存量")
    public  R<PageVO<ProInOutStockVO>> proInOutStock(@RequestBody ProInOutStockDTO proInOutStockDTO) {
        PageDomain pageDomain= BeanConverUtil.conver(proInOutStockDTO,PageDomain.class);
        startPage(pageDomain);
        //设置前一天
        Date createTimeStart = proInOutStockDTO.getCreateTimeStart();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createTimeStart);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date newTime = calendar.getTime();
        proInOutStockDTO.setCreateTimeStart(newTime);

        List<ProInOutStockVO> list = reportService.proInOutStock(proInOutStockDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }
    /**
     * 成品报表-进销存量
     */
    @PostMapping("/proInOutStockExport")
    @ApiOperation("成品报表-进销存量导出")
    public  void proInOutStockExport(HttpServletResponse response ,@RequestBody ProInOutStockDTO proInOutStockDTO) {

        List<ProInOutStockVO> list = reportService.proInOutStock(proInOutStockDTO);
        ExcelUtil<ProInOutStockVO> util = new ExcelUtil<>(ProInOutStockVO.class);
        util.exportExcel(response, list, "成品报表-进销存量导出");
    }
}
