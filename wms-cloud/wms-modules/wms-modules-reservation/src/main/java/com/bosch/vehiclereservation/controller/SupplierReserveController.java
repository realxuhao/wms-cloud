package com.bosch.vehiclereservation.controller;

import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import com.bosch.vehiclereservation.api.domain.vo.PageVO;
import com.bosch.vehiclereservation.api.domain.vo.PurchaseOrderVO;
import com.bosch.vehiclereservation.service.ISupplierReserveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
