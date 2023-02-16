package com.bosch.vehiclereservation.controller;

import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.dto.PurchaseOrderDTO;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.api.domain.vo.PurchaseOrderVO;
import com.bosch.vehiclereservation.service.IPurchaseOrderService;
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


    /**
     * 查询采购订单列表
     */
    @RequiresPermissions("vehiclereservation:purchase:list")
    @GetMapping("/list")
    @ApiOperation("查询采购订单列表")
    public R<PageVO<PurchaseOrderVO>> list(PurchaseOrderDTO purchaseOrderDTO) {
        startPage();
        List<PurchaseOrder> list = purchaseOrderService.selectPurchaseOrderList(BeanConverUtil.conver(purchaseOrderDTO, PurchaseOrder.class));
        List<PurchaseOrderVO> blackDriverVOS = BeanConverUtil.converList(list, PurchaseOrderVO.class);
        return R.ok(new PageVO<>(blackDriverVOS, new PageInfo<>(blackDriverVOS).getTotal()));
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


}
