package com.bosch.product.controller;

import com.bosch.product.api.domain.StockTakeDetail;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.api.domain.dto.StockTakeDetailQueryDTO;
import com.bosch.product.service.IStockTakeDetailService;
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
@Api(tags = "库存盘点详情")
@RequestMapping("/stock-take-detail")
public class StockTakePlanDetailController {

    @Autowired
    private IStockTakeDetailService detailService;

    @PostMapping(value = "/issue")
    @ApiOperation("新增盘点计划")
    public R issue(@RequestBody StockTakeDetailQueryDTO dto) {
        detailService.issue(dto);
        return R.ok();
    }

}
