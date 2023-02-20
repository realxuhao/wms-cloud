package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.dto.DriverPickupDTO;
import com.bosch.vehiclereservation.api.domain.vo.DriverPickupVO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.service.IDriverPickupService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 司机取货预约Controller
 *
 * @author taojd
 * @date 2023-02-18
 */
@RestController
@RequestMapping("/driverPickup")
@Api(tags = "司机取货预约接口")
public class DriverPickupController extends BaseController {

    @Autowired
    private IDriverPickupService driverPickupService;


    /**
     * 查询司机取货信息列表
     *
     * @param driverPickupDTO 查询条件
     * @return 司机取货信息列表
     */
    @RequiresPermissions("vehiclereservation:driverpickup:list")
    @GetMapping("/list")
    @ApiOperation("查询司机取货信息列表")
    public R<PageVO<DriverPickupVO>> list(DriverPickupDTO driverPickupDTO) {
        startPage();
        List<DriverPickupVO> list = driverPickupService.selectDriverPickupVO(driverPickupDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    /**
     * 删除司机的取货预约信息（只能删状态是0的数据）
     */
    @RequiresPermissions("vehiclereservation:driverpickup:remove")
    @Log(title = "删除司机的取货预约信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(driverPickupService.deleteDriverPickupById(id));
    }

    /**
     * 新增司机的取货预约信息
     */
    @RequiresPermissions("vehiclereservation:driverpickup:add")
    @Log(title = "新增司机的取货预约信息", businessType = BusinessType.INSERT)
    @ApiOperation("新增司机的取货预约信息")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody List<DriverPickupDTO> driverPickupDTOList) {
        return toAjax(driverPickupService.insertDriverPickup(driverPickupDTOList));
    }

    /**
     * 获取司机的取货预约信息
     *
     * @param wechatId 微信id
     * @return
     */
    @ApiOperation("获取司机的取货预约信息")
    @GetMapping("/info/{wechatId}")
    public R<List<DriverPickupVO>> getDriverDeliverInfo(@PathVariable String wechatId) {
        return R.ok(driverPickupService.selectDriverPickupInfo(wechatId));
    }

    @ApiOperation("司机取货签到")
    @GetMapping("/signin/{id}")
    public AjaxResult signIn(@PathVariable Long id) {
        return toAjax(driverPickupService.signIn(id));
    }

}
