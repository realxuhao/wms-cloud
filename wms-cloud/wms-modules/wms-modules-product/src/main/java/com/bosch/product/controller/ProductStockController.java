package com.bosch.product.controller;

import com.bosch.binin.api.domain.dto.AddManualTransDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.StockEditDTO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.EditStockDTO;
import com.bosch.product.api.domain.dto.ProductStockEditDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.service.IMaterialStockService;
import com.bosch.product.service.IProductStockService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    private IUserOperationLogService userOperationLogService;

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


//    @PostMapping(value = "/editStock")
//    @ApiOperation("修改库存")
//    public R editStock(@RequestBody EditStockDTO dto) {
//        String barCode = dto.getBarCode();
//        if (barCode.length() == 50) {
//            materialStockService.editStock(dto);
//        } else if (barCode.length() == 71) {
//            productStockService.editStock(dto);
//        }
//
//        return R.ok();
//    }


    @PostMapping(value = "/editStock")
    @ApiOperation("修改库存")
    @Log(title = "成品库存调整修改库存", businessType = BusinessType.UPDATE)
    public R editStock(@RequestBody ProductStockEditDTO stockEditDTO) {

        productStockService.adjustStock(stockEditDTO);
        return R.ok();
    }


    @PostMapping(value = "/trans")
    @ApiOperation("转储")
    @Log(title = "成品库存转储", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R trans(@RequestBody ManualBinInDTO binInDTO) {
        productStockService.trans(binInDTO);
        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCT_TRANS.getCode(), ProductQRCodeUtil.getSSCC(binInDTO.getMesBarCode()));

        return R.ok("转储成功");
    }


    @GetMapping(value = "/getByBarCode/{barCode}")
    public R<ProductStockVO> getByBarCode(@PathVariable("barCode") String barCode) {
        String sscc = ProductQRCodeUtil.getSSCC(barCode);
        ProductStockQueryDTO queryDTO = new ProductStockQueryDTO();
        queryDTO.setSsccNumber(sscc);
        List<ProductStockVO> list = productStockService.allList(queryDTO);
        if (!CollectionUtils.isEmpty(list)) {
            return R.ok(list.get(0));
        }
        return R.fail("没有该SSCC" + sscc + "对应的库存");
    }


    @PostMapping(value = "addSplit")
    @ApiOperation("拆托")
    @Log(title = "成品库存拆托", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R splitPallet(@RequestBody SplitPalletDTO splitPallet) {
        productStockService.addSplit(splitPallet);
        userOperationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PALLETSPLIT.getCode(),splitPallet.getSourceSsccNb());

        return R.ok();
    }


}
