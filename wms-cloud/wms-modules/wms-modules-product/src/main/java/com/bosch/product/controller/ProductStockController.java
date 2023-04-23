package com.bosch.product.controller;

import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.EditStockDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.service.IMaterialStockService;
import com.bosch.product.service.IProductStockService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
    private IProductStockService productStockService;

    @Autowired
    private IMaterialStockService materialStockService;

    @GetMapping(value = "/list")
    @ApiOperation("库存列表")
    public R<PageVO<ProductStockVO>> list(ProductStockQueryDTO stockQueryDTO) {
        startPage();
        List<ProductStockVO> list = productStockService.list(stockQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/listByBinCode/{binCode}")
    @ApiOperation("库存列表-根据库位")
    public R<List<ProductStockVO>> listByBinCode(@PathVariable("binCode") String binCode) {
        ProductStockQueryDTO queryDTO = new ProductStockQueryDTO();
        queryDTO.setBinCode(binCode);
        List<ProductStockVO> list = productStockService.list(queryDTO);
        return R.ok(list);
    }

//    @GetMapping(value = "/allocateBin/{qrCode}")
//    @ApiOperation("获取分配库位")
//    public R<ProductStockVO> allocateBin(@PathVariable("qrCode")String qrCode){
//        return stockService.allocateBin(qrCode)
//    }


    @PostMapping(value = "/editStock")
    @ApiOperation("修改库存")
    public R editStock(@RequestBody EditStockDTO dto) {
        String barCode = dto.getBarCode();
        if (barCode.length() == 50) {
            materialStockService.editStock(dto);
        } else if (barCode.length() == 71) {
            productStockService.editStock(dto);
        }

        return R.ok();
    }

    @GetMapping(value = "/getByBarCode/{barCode}")
    public R<ProductStockVO> getByBarCode(@PathVariable("barCode") String barCode) {
        String sscc = ProductQRCodeUtil.getSSCC(barCode);
        ProductStockQueryDTO queryDTO = new ProductStockQueryDTO();
        queryDTO.setSsccNumber(sscc);
        List<ProductStockVO> list = productStockService.list(queryDTO);
        if (!CollectionUtils.isEmpty(list)) {
            return R.ok(list.get(0));
        }
        return R.fail("没有该SSCC" + sscc + "对应的库存");
    }


}
