package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.DriverDispatch;
import com.bosch.vehiclereservation.api.domain.dto.*;
import com.bosch.vehiclereservation.api.domain.vo.*;
import com.bosch.vehiclereservation.service.IDriverDispatchService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    ////@RequiresPermissions("vehiclereservation:driverdispatch:pagelist")
    @PostMapping("/pagelist")
    @ApiOperation("获取签到车辆数据")
    public R<PageVO<DriverDispatchVO>> getSignDataPageList(@RequestBody DriverDispatchDTO driverDispatchDTO, PageDomain pageDomain) {
        driverDispatchDTO.setToday(false);
        startPage();
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(driverDispatchDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 导出供应商签到数据
     */
    @PostMapping("/exportSign")
    @ApiOperation("导出签到记录")
    public void exportSign(HttpServletResponse response, @RequestBody DriverDispatchDTO driverDispatchDTO) {
        driverDispatchDTO.setToday(false);
        List<DriverDispatchVO> list = driverDispatchService.selectTodaySignData(driverDispatchDTO);
        ExcelUtil<DriverDispatchVO> util = new ExcelUtil<>(DriverDispatchVO.class);
        util.exportExcel(response, list, "车辆预约已签到数据");
    }

    /**
     * 导出供应商未签到数据
     */
    @PostMapping("/exportNotSign")
    @ApiOperation("导出未签到记录")
    public void exportNotSign(HttpServletResponse response, @RequestBody DriverDispatchDTO driverDispatchDTO) {
        List<DriverDispatchVO> list = driverDispatchService.selectTodayNotSignData(driverDispatchDTO);;
        List<DriverDispatchNotSignVO> notList = BeanConverUtil.converList(list, DriverDispatchNotSignVO.class);
        ExcelUtil<DriverDispatchNotSignVO> util = new ExcelUtil<>(DriverDispatchNotSignVO.class);
        util.exportExcel(response, notList, "车辆预约未签到数据");
    }

    /**
     * 获取今天签到车辆数据
     *
     * @param driverDispatchDTO 查询条件
     * @return 车辆调度信息列表
     */
    ////@RequiresPermissions("vehiclereservation:driverdispatch:signlist")
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
    ////@RequiresPermissions("vehiclereservation:driverdispatch:nosignlist")
    @PostMapping("/nosignlist")
    @ApiOperation("获取今天未签到车辆数据")
    public R<PageVO<DriverDispatchVO>> getTodayNoSignData(@RequestBody DriverDispatchDTO driverDispatchDTO, PageDomain pageDomain) {
        startPage();
        List<DriverDispatchVO> list = driverDispatchService.selectTodayNotSignData(driverDispatchDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 分配道口
     *
     * @return
     */
    //@RequiresPermissions("warehouse:dispatch:setdock")
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
    //@RequiresPermissions("warehouse:dispatch:enter")
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
    //@RequiresPermissions("warehouse:dispatch:complete")
    @GetMapping("/complete/{id}")
    @ApiOperation("完成")
    public AjaxResult dispatchComplete(@PathVariable("id") Long dispatchId) {
        return toAjax(driverDispatchService.dispatchComplete(dispatchId));
    }

    /**
     * 取消
     *
     * @param dispatchId 主键id
     * @return
     */
    //@RequiresPermissions("warehouse:dispatch:complete")
    @GetMapping("/cancel/{id}")
    @ApiOperation("取消")
    public AjaxResult dispatchCancel(@PathVariable("id") Long dispatchId) {
        return toAjax(driverDispatchService.dispatchCancel(dispatchId));
    }

    /**
     * 车辆进厂排序
     *
     * @param driverDispatchDTO
     * @return
     */
    ////@RequiresPermissions("vehiclereservation:driverdispatch:sort")
    @PostMapping("/sort")
    @ApiOperation("车辆进厂排序")
    public AjaxResult dispatchSortNO(@RequestBody DriverSortDTO driverDispatchDTO) {
        return toAjax(driverDispatchService.dispatchSort(driverDispatchDTO));
    }

    /**
     * 异常数据处理（异常 => 已完成）
     *
     * @param dispatchId
     * @return
     */
    //@RequiresPermissions("warehouse:dispatch:complete")
    @GetMapping("/change/{id}")
    @ApiOperation("处理")
    public AjaxResult dispatchChange(@PathVariable("id") Long dispatchId) {
        return toAjax(driverDispatchService.dispatchChange(dispatchId));
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
