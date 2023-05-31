package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.dto.PurchaseOrderDTO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.api.domain.vo.PurchaseOrderVO;
import com.bosch.vehiclereservation.api.domain.vo.SupplierReserveDetailVO;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
import com.bosch.vehiclereservation.service.ISyncDataService;
import com.bosch.vehiclereservation.service.StaticScheduleTask;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 采购订单Controller
 *
 * @author taojd
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/purchase")
@Api(tags = "采购订单接口")
public class PurchaseOrderController extends BaseController {

    @Autowired
    private IPurchaseOrderService purchaseOrderService;


    @Autowired
    private ISyncDataService syncDataService;

    /**
     * 查询采购订单列表
     */
    //@RequiresPermissions("purchase:order:list")
    @GetMapping("/list")
    @ApiOperation("查询采购订单列表")
    public R<PageVO<PurchaseOrderVO>> list(PurchaseOrderDTO purchaseOrderDTO) {
        startPage();
        List<PurchaseOrder> list = purchaseOrderService.selectPurchaseOrderList(BeanConverUtil.conver(purchaseOrderDTO, PurchaseOrder.class));
        List<PurchaseOrderVO> blackDriverVOS = BeanConverUtil.converList(list, PurchaseOrderVO.class);
        return R.ok(new PageVO<>(blackDriverVOS, new PageInfo<>(list).getTotal()));
    }

    /**
     * 根据供应商姓名获取该供应商可预约的采购单列表
     */
    @GetMapping(value = "/list/{name}")
    @ApiOperation("根据供应商姓名获取该供应商可预约的采购单列表")
    public R<PageVO<PurchaseOrderVO>> getListToSupplier(@PathVariable("name") String name, PurchaseOrderDTO purchaseOrderDTO) {
        startPage();
        List<PurchaseOrder> list = purchaseOrderService.selectSupplierPurchaseOrder(name, BeanConverUtil.conver(purchaseOrderDTO, PurchaseOrder.class));
        List<PurchaseOrderVO> blackDriverVOS = BeanConverUtil.converList(list, PurchaseOrderVO.class);
        return R.ok(new PageVO<>(blackDriverVOS, new PageInfo<>(blackDriverVOS).getTotal()));
    }

    /**
     * 关闭采购单
     *
     * @param id 采购单id
     * @return
     */
    @RequiresPermissions("purchase:order:complete")
    @GetMapping("/close/{id}")
    @ApiOperation("关闭采购单")
    public AjaxResult close(@PathVariable("id") Long id) {
        return toAjax(purchaseOrderService.closePurchaseOrder(id));
    }

    /**
     * 根据采购单id查看该采购单被预约的明细信息
     */
    @RequiresPermissions("purchase:order:details")
    @GetMapping(value = "/detail/{purchaseId}")
    @ApiOperation("根据采购单id查看该采购单被预约的明细信息")
    public R<List<SupplierReserveDetailVO>> getSupplierReserveList(@PathVariable("purchaseId") Long purchaseId) {
        List<SupplierReserveDetailVO> list = purchaseOrderService.getSupplierReserveList(purchaseId);
        return R.ok(list);
    }


    /**
     * 同步采购单
     */
    @RequiresPermissions("purchase:order:sync")
    @GetMapping("/syncdata")
    @ApiOperation("同步采购单")
    public AjaxResult syncData() {
        return toAjax(syncDataService.syncData());
    }

    /**
     * 获取错误状态的采购单PO号
     *
     * @return
     */
    @GetMapping(value = "/getErrorPoCode")
    @ApiOperation("获取错误状态的采购单PO号")
    public R<List<String>> getAllErrorPoCode() {
        return R.ok(purchaseOrderService.getAllErrorPoCode());
    }

}
