package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.dto.StockQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wms-cloud
 * @description: 库存Controller
 * @author: xuhao
 * @create: 2022-11-02 14:43
 **/
@RestController
@Api(tags = "库存接口")
@RequestMapping("/stock")
public class StockController extends BaseController {

    @Autowired
    private IStockService stockService;

    @GetMapping(value = "/list")
    @ApiOperation("库存列表")
    public R<PageVO<StockVO>> list(StockQueryDTO stockQuerySTO) {
        startPage();
        List<StockVO> list = stockService.selectStockVOList(stockQuerySTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/listByRule")
    @ApiOperation("根据规则查询某个物料的库存")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "物料号", name = "materialNb", dataType = "String"),
            @ApiImplicitParam(value = "排序类型,0基于有效期，1、基于先主库后外库", name = "sortType", dataType = "Integer")
    })
    public R<PageVO<StockVO>> listByRule(StockQueryDTO stockQuerySTO) {
        startPage();
        List<StockVO> list = stockService.selectStockVOBySortType(stockQuerySTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

}
