package com.bosch.vehiclereservation.controller;

import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierDTO;
import com.bosch.vehiclereservation.api.domain.dto.SupplierReserveDTO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.api.domain.vo.PurchaseOrderVO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveVO;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @GetMapping(value = "/timewindow")
    @ApiOperation("根据仓库id获取时间窗口列表")
    public R<List<TimeWindowVO>> getTimeWindowList(Long wareId, String reserveDate) {
        Date date = DateUtils.parseDate(reserveDate);
        List<TimeWindowVO> list = supplierReserveService.selectTimeWindowList(wareId, date);
        return R.ok(list);
    }


    /**
     * 新增供应商预约单
     */
    @RequiresPermissions("vehiclereservation:supplier:add")
    @Log(title = "供应商预约单", businessType = BusinessType.INSERT)
    @ApiOperation("新增供应商预约单")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SupplierDTO supplierDTO) {
        return toAjax(supplierReserveService.insertSupplierReserve(supplierDTO));
    }


    /**
     * 查询供应商预约单列表(已预约记录)
     */
    @RequiresPermissions("vehiclereservation:supplier:list")
    @GetMapping("/list")
    @ApiOperation("查询供应商预约单列表")
    public R<PageVO<SupplierReserveVO>> list(SupplierReserveDTO supplierReserveDTO) {
        startPage();
        List<SupplierReserveVO> list = supplierReserveService.selectSupplierReserveVO(supplierReserveDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 删除供应商的预约信息（只能删状态是0的数据）
     */
    @RequiresPermissions("vehiclereservation:supplier:remove")
    @Log(title = "删除供应商的预约信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(supplierReserveService.deleteSupplierReserveById(id));
    }

    /**
     * 获取某个预约单下的采购单列表信息
     */
    @GetMapping(value = "/purchaseorder/{reserveNo}")
    @ApiOperation("获取某个预约单下的采购单列表信息")
    public R<List<PurchaseOrderVO>> getPurchaseOrderList(@PathVariable String reserveNo) {
        List<PurchaseOrderVO> list = supplierReserveService.selectPurchaseOrderList(reserveNo);
        return R.ok(list);
    }

}
