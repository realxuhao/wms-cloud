package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDeliverVO;
import com.bosch.vehiclereservation.api.domain.vo.DriverDispatchVO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆调度Controller
 *
 * @author taojd
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/driverDispatch")
@Api(tags = "车辆调度接口")
public class DriverDispatchController extends BaseController {

    @Autowired
    private IDriverDispatchService driverDispatchService;

    /**
     * 获取今天签到车辆数据
     *
     * @param wareId 仓库id
     * @return 车辆调度信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:signlist")
    @GetMapping("/signlist")
    @ApiOperation("获取今天签到车辆数据")
    public R<List<DriverDispatchVO>> getTodaySignData(Long wareId) {
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(wareId);
        return R.ok(list);
    }

    /**
     * 获取今天未签到车辆数据
     *
     * @return 车辆预约信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:nosignlist")
    @GetMapping("/nosignlist")
    @ApiOperation("获取今天未签到车辆数据")
    public R<List<DriverDispatchVO>> getTodayNoSignData() {
        List<DriverDispatchVO> list = driverDispatchService.selectTodayNotSignData();
        return R.ok(list);
    }

    /**
     * 分配道口
     *
     * @return
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:dock")
    @PostMapping("/dock")
    @ApiOperation("分配道口")
    public AjaxResult dispatchDock(@RequestBody DriverDispatchDTO driverDispatchDTO) {
        return toAjax(driverDispatchService.dispatchDock(driverDispatchDTO));
    }

    /**
     * 进厂
     *
     * @param dispatchId 主键id
     * @return
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:enter")
    @GetMapping("/enter/{id}")
    @ApiOperation("进厂")
    public AjaxResult dispatchEnter(@PathVariable("id") Long dispatchId) {
        return toAjax(driverDispatchService.dispatchEnter(dispatchId));
    }

}
