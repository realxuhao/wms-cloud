package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.dto.DispatchSendWxDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDeliverDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverDispatchDTO;
import com.bosch.vehiclereservation.api.domain.dto.DriverSortDTO;
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
     * 获取签到车辆数据
     *
     * @param driverDispatchDTO 查询条件
     * @return 车辆调度信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:pagelist")
    @PostMapping("/pagelist")
    @ApiOperation("获取签到车辆数据")
    public R<PageVO<DriverDispatchVO>> getSignDataPageList(@RequestBody DriverDispatchDTO driverDispatchDTO) {
        driverDispatchDTO.setToday(false);
        startPage();
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(driverDispatchDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 获取今天签到车辆数据
     *
     * @param driverDispatchDTO 查询条件
     * @return 车辆调度信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:signlist")
    @PostMapping("/signlist")
    @ApiOperation("获取今天签到车辆数据")
    public R<List<DriverDispatchVO>> getTodaySignData(@RequestBody DriverDispatchDTO driverDispatchDTO) {
        driverDispatchDTO.setToday(true);
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(driverDispatchDTO);
        return R.ok(list);
    }

    /**
     * 获取今天未签到车辆数据
     *
     * @return 车辆预约信息列表
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:nosignlist")
    @PostMapping("/nosignlist")
    @ApiOperation("获取今天未签到车辆数据")
    public R<List<DriverDispatchVO>> getTodayNoSignData(@RequestBody DriverDispatchDTO driverDispatchDTO) {
        List<DriverDispatchVO> list = driverDispatchService.selectTodayNotSignData(driverDispatchDTO);
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

    /**
     * 完成(取货/送货)
     *
     * @param dispatchId 主键id
     * @return
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:complete")
    @GetMapping("/complete/{id}")
    @ApiOperation("完成")
    public AjaxResult dispatchComplete(@PathVariable("id") Long dispatchId) {
        return toAjax(driverDispatchService.dispatchComplete(dispatchId));
    }

    /**
     * 车辆进厂排序
     *
     * @param driverDispatchDTO
     * @return
     */
    @RequiresPermissions("vehiclereservation:driverdispatch:sort")
    @PostMapping("/sort")
    @ApiOperation("车辆进厂排序")
    public AjaxResult dispatchSortNO(@RequestBody DriverSortDTO driverDispatchDTO) {
        return toAjax(driverDispatchService.dispatchSort(driverDispatchDTO));
    }

    /**
     * 根据appid及密钥获取access_token
     *
     * @return
     */
    @GetMapping("/getWxToken")
    @ApiOperation("根据appid及密钥获取access_token")
    public AjaxResult getWxToken() {
        return success(driverDispatchService.getWxToken());
    }

    /**
     * 推送入厂消息到司机微信
     *
     * @return
     */
    @PostMapping("/sendMsgToWx")
    @ApiOperation("推送入厂消息")
    public AjaxResult sendMsgToWx(@RequestBody DispatchSendWxDTO dispatchSendWxDTO) {
        return toAjax(driverDispatchService.sendMsgToWx(dispatchSendWxDTO));
    }

}
