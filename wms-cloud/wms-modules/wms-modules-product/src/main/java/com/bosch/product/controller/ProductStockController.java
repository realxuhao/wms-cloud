package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.service.IProductStockService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 15:58
 **/
@RestController
@Api(tags = "成品库存接口")
@RequestMapping("/product-stock")
public class ProductStockController extends BaseController {

    @Autowired
    private IProductStockService stockService;

    @GetMapping(value = "/list")
    @ApiOperation("库存列表")
    public R<PageVO<ProductStockVO>> list(ProductStockQueryDTO stockQueryDTO) {
        startPage();
        List<ProductStockVO> list = stockService.list(stockQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }





}
