package com.bosch.product.controller;

import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IProductWareShiftService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
            if(queryDTO.getStatus().equals(ProductWareShiftEnum.WAITTING_BIN_IN.code())) {
                queryDTO.setTargetWareCode(SecurityUtils.getWareCode());
            }else if (queryDTO.getStatus().equals(ProductWareShiftEnum.FINISH.code())){
                queryDTO.setTargetWareCode(SecurityUtils.getWareCode());
            }else {
                queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
            }
        }

        startPage();
        List<ProductWareShiftVO> list = productWareShiftService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/receivinglist")
    @ApiOperation("成品移库任务列表")
    public R<PageVO<ProductWareShiftVO>> receivinglist(ProductWareShiftQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductWareShiftQueryDTO();
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

    @PostMapping(value = "/batchGenerateShift")
    @ApiOperation("根据库存ids批量生成移库任务")
    public R add(@RequestBody List<Long> stockIds) {
        productWareShiftService.addBatchByStockIds(stockIds);
        return R.ok();
    }

    @PutMapping(value = "/cancel/{id}")
    @ApiOperation("取消移库任务")
    public R cancel(@PathVariable("id") Long id){
        productWareShiftService.cancel(id);
        return R.ok();
    }


    @PostMapping(value = "/ship")
    @ApiOperation("发运")
    public R ship(@RequestBody ProductWareShiftQueryDTO dto) {
        Assert.notNull(dto,"参数不可以为空");
        Assert.noNullElements(dto.getSsccList(),"ssccList不可以为空");
        Assert.notNull(dto.getCarNb(),"车牌号不可以为空");

        List<String> list = dto.getSsccList().stream().map(item -> ProductQRCodeUtil.getSSCC(item)).collect(Collectors.toList());

        productWareShiftService.ship(list, dto.getCarNb());
        return R.ok();
    }

    @GetMapping(value = "/getOne/{qrCode}")
    public R<ProductWareShiftVO> getOne(@PathVariable("qrCode") String qrCode){
        ProductWareShiftQueryDTO queryDTO = new ProductWareShiftQueryDTO();
        queryDTO.setSsccNb(ProductQRCodeUtil.getSSCC(qrCode));
        List<ProductWareShiftVO> list = productWareShiftService.list(queryDTO);
        list = list.stream().filter(item -> !item.getStatus().equals(ProductWareShiftEnum.CANCEL.code()) || !item.getStatus().equals(ProductWareShiftEnum.FINISH.code())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)){
            return R.fail("没有该SSCC"+ProductQRCodeUtil.getSSCC(qrCode)+"对应的进行中移库信息");
        }
        return R.ok(list.get(0));
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


    @PostMapping(value = "/mainReceiveConfirm")
    @ApiOperation("确认并入库")
    @Transactional(rollbackFor = Exception.class)
    public R confirmOrder(@RequestBody List<String> ssccList){
        productWareShiftService.mainReceiveConfirm(ssccList);
        return R.ok();
    }


}
