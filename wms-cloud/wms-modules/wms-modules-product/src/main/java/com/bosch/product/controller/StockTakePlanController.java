package com.bosch.product.controller;

import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.service.IStockTakePlanService;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 13:55
 **/
@RestController
@Api(tags = "库存盘点计划")
@RequestMapping("/stock-take")
public class StockTakePlanController {

    @Autowired
    private IStockTakePlanService stockTakePlanService;

    @PostMapping(value = "/add")
    @ApiOperation("新增盘点计划")
    public R add(@RequestBody StockTakeAddDTO dto) {
        stockTakePlanService.addStockTakePlan(dto);
        return R.ok();
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation("删除盘点计划")
    public R delete(@PathVariable("id") Long id) {
        stockTakePlanService.delete(id);
        return R.ok();
    }






}
