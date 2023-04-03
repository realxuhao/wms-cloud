package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.service.IProductReceiveService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
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
 * @create: 2023-03-29 10:56
 **/
@RestController
@Api(tags = "成品入库接口")
@RequestMapping("/product-receive")
public class ProductReceiveController extends BaseController {


    @Autowired
    private IProductReceiveService receiveService;


    @GetMapping(value = "/list")
    @ApiOperation("成品收货列表")
    public R<PageVO<ProductReceiveVO>> list(ProductReceiveQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductReceiveQueryDTO();
        }

        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ProductReceiveVO> list = receiveService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getOne/{qrCode}")
    public R<ProductReceiveVO> getOne(@PathVariable("qrCode") String qrCode) {
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        ProductReceiveQueryDTO queryDTO = new ProductReceiveQueryDTO();
        queryDTO.setSsccNumber(sscc);
        List<ProductReceiveVO> list = receiveService.list(queryDTO);
        if (CollectionUtils.isEmpty(list)) {
            return R.fail("没有该sscc:" + sscc + "对应的入库任务");
        }
        return R.ok(list.get(0));
    }

    @GetMapping(value = "/receive/{qrCode}")
    @ApiOperation("PDA成品收货")
    public R receive(@PathVariable("qrCode") String qrCode) {
        receiveService.receive(qrCode);
        return R.ok();
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除")
    public R receive(@PathVariable("id") Long id) {
        receiveService.delete(id);
        return R.ok();
    }


}
