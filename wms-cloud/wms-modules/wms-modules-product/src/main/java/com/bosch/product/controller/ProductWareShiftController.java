package com.bosch.product.controller;

import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IProductWareShiftService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:26
 **/
@RestController
@Api(tags = "成品移库接口")
@RequestMapping("/product-shift")
public class ProductWareShiftController extends BaseController {

    @Autowired
    private IProductWareShiftService productWareShiftService;

    @Autowired
    private IProductStockService productStockService;


    @GetMapping(value = "/list")
    @ApiOperation("成品移库任务列表")
    public R<PageVO<ProductWareShiftVO>> list(ProductWareShiftQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductWareShiftQueryDTO();
        }
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ProductWareShiftVO> list = productWareShiftService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PutMapping(value = "/generateShift/{stockId}")
    @ApiOperation("根据库存id生成移库任务")
    public R add(@PathVariable("stockId") Long stockId) {
        productWareShiftService.addByStockId(stockId);
        return R.ok();
    }

    @PutMapping(value = "/cancel/{id}")
    @ApiOperation("取消移库任务")
    public R cancel(@PathVariable("id") Long id){
        productWareShiftService.cancel(id);
        return R.ok();
    }


    @PutMapping(value = "/ship/{carNb}")
    @ApiOperation("发运")
    public R ship(@RequestBody List<String> ssccList, @PathVariable("carNb") String carNb) {
        productWareShiftService.ship(ssccList, carNb);
        return R.ok();
    }

    @PutMapping(value = "/receive/{qrCode}")
    @ApiOperation("移库收货")
    public R receive(@PathVariable("qrCode") String qrCode) {
        productWareShiftService.receive(qrCode);
        return R.ok();
    }

    @PostMapping(value = "/binIn")
    @ApiOperation("移库上架")
    public R binIn(@RequestBody ProductBinInDTO binInDTO){
        productWareShiftService.wareShiftBinIn(binInDTO);
        return R.ok();
    }

    @GetMapping(value = "/getBinInInfo/{qrCode}")
    @ApiOperation("获取单个上架信息")
    public R<ProductStockVO> getBinInInfo(@PathVariable("qrCode") String qrCode){
        return R.ok(productWareShiftService.getBinInInfo(qrCode));
    }

    /**
     * 导出列表
     */
    @PostMapping("/export")
    @ApiOperation("移库列表导出")
    public void export(HttpServletResponse response, ProductWareShiftQueryDTO queryDTO) {
        List<ProductWareShiftVO> wareShiftList = productWareShiftService.list(queryDTO);
//        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(list, MaterialCallVO.class);

        ExcelUtil<ProductWareShiftVO> util = new ExcelUtil<>(ProductWareShiftVO.class);
        util.exportExcel(response, wareShiftList, "成品移库任务列表");
    }









}
