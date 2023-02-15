package com.bosch.vehiclereservation.controller;

import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
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

@RestController
@RequestMapping("/supplierReserve")
@Api(tags = "供应商预约接口")
public class SupplierReserveController extends BaseController {

    @Autowired
    private ISupplierReserveService supplierReserveService;


    /**
     * 根据仓库id获取时间窗口列表
     */
    @GetMapping(value = "/{wareId}")
    @ApiOperation("根据仓库id获取时间窗口列表")
    public R<List<TimeWindowVO>> getListByName(@PathVariable("wareId") Long wareId) {
        List<TimeWindowVO> list = supplierReserveService.selectTimeWindowList(wareId);
        return R.ok(list);
    }


    /**
     * 新增供应商预约单
     */
    @RequiresPermissions("vehiclereservation:supplier:add")
    @Log(title = "供应商预约单", businessType = BusinessType.INSERT)
    @ApiOperation("新增供应商预约单")
    @PostMapping
    public AjaxResult add(@RequestBody SupplierDTO supplierDTO) {
        return toAjax(supplierReserveService.insertSupplierReserve(supplierDTO));
    }

}
