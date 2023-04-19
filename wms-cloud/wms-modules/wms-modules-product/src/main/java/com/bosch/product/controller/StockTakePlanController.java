package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.api.domain.dto.StockTakePlanDTO;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;
import com.bosch.product.api.domain.vo.StockTakePlanVO;
import com.bosch.product.service.IStockTakePlanService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 13:55
 **/
@RestController
@Api(tags = "库存盘点计划")
@RequestMapping("/stock-take")
public class StockTakePlanController extends BaseController {

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

    @GetMapping(value = "/list")
    @ApiOperation("盘点计划列表")
    public R<PageVO<StockTakePlanVO>> list(StockTakePlanDTO dto) {

        startPage();
        List<StockTakePlan> list = stockTakePlanService.list(dto);
        List<StockTakePlanVO> stockTakePlanVOList = BeanConverUtil.converList(list, StockTakePlanVO.class);
        return R.ok(new PageVO<>(stockTakePlanVOList, new PageInfo<>(list).getTotal()));
    }






}
