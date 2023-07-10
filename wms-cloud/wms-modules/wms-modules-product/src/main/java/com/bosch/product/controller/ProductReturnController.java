package com.bosch.product.controller;

import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnDTO;
import com.bosch.binin.api.domain.dto.MaterialReturnQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialReturnVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductReturnDTO;
import com.bosch.product.api.domain.dto.ProductStockEditDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductReturnVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.service.IMaterialStockService;
import com.bosch.product.service.IProductReturnService;
import com.bosch.product.service.IProductStockService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 15:58
 **/
@RestController
@Api(tags = "成品退料接口")
@RequestMapping("/product-return")
public class ProductReturnController extends BaseController {

    @Autowired
    private IProductStockService productStockService;



    @PostMapping(value = "/addProductReturn")
    @ApiOperation("新增退库")
    public R save(@RequestBody ProductReturnDTO productReturnDTO) {

      productStockService.stockReturn(productReturnDTO);
        return R.ok();

    }

    @GetMapping(value = "/list")
    @ApiOperation("获取退料列表")
    public R<PageVO<ProductReturnVO>> list(ProductReturnDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductReturnDTO();
        }
//        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
//            queryDTO.setWareCode(SecurityUtils.getWareCode());
//        }
        startPage();

        List<ProductReturnVO> list = productStockService.returnList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));

    }

    @PostMapping("/export")
    @ApiOperation("退库记录导出")
    public void export(HttpServletResponse response, @RequestBody ProductReturnDTO queryDTO) {
        List<ProductReturnVO> materialReturnVOS = productStockService.returnList(queryDTO);
        ExcelUtil<ProductReturnVO> util = new ExcelUtil<>(ProductReturnVO.class);
        util.exportExcel(response, materialReturnVOS, "成品退库记录");
    }


}
