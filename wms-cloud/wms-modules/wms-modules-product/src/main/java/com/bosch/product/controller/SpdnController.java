package com.bosch.product.controller;

import com.alibaba.fastjson2.JSON;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.file.api.FileService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.masterdata.api.enumeration.ClassType;
import com.bosch.product.api.domain.SPDN;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.service.IProductOutService;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.ISPDNService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description: 成品出库
 * @author: xuhao
 * @create: 2023-06-09 13:48
 **/
@RestController
@Api(tags = "SPDN")
@RequestMapping("/spdn")
public class SpdnController extends BaseController {

    @Autowired
    private FileService fileService;

    @Autowired
    private IProductOutService productOutService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private ISPDNService spdnService;

    @PostMapping(value = "/importSPDN")
    @ApiOperation("SPDN导入")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "文件", name = "file", dataType = "File")
    })
    @Transactional(rollbackFor = Exception.class)
    public R importSPDN(@RequestParam(value = "file") MultipartFile file) {

        R result = fileService.SPDNFileImport(file, ClassType.SPDNDTO.getDesc());
        if (result.isSuccess()) {
            Object data = result.getData();
            List<SPDNDTO> dtos = JSON.parseArray(JSON.toJSONString(data), SPDNDTO.class);
            if (!CollectionUtils.isEmpty(dtos)) {
                productOutService.validList(dtos);
                List<SPDN> spdnList = BeanConverUtil.converList(dtos, SPDN.class);
                spdnService.saveBatch(spdnList);

            }
            return R.ok(null);
        } else {
            return R.fail(result.getMsg());
        }

    }


    @GetMapping(value = "/spdnList")
    @ApiOperation("库存列表")
    public R<List<SPDNVO>> getSpdnList(SPDNDTO spdndto){
        return R.ok(spdnService.getList(spdndto));
    }


    @DeleteMapping(value = "/{ids}")
    @ApiOperation("删除spdn")
    public R batchDelete(@PathVariable Long[] ids){
        spdnService.batchDelete(Arrays.asList(ids));
        return R.ok();
    }

    @PostMapping(value = "/approve/{ids}")
    @ApiOperation("审批SPDN")
    public R approve(@PathVariable Long[] ids){
        spdnService.approve(Arrays.asList(ids));
        return R.ok();
    }


    @GetMapping(value = "/spdnStocklist")
    @ApiOperation("库存列表")
    public R<PageVO<ProductStockVO>> spdnStocklist(ProductStockQueryDTO stockQueryDTO) {
        startPage();
        List<ProductStockVO> list = productStockService.spdnStocklist(stockQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }




}
