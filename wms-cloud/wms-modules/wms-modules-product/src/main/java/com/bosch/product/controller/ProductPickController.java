package com.bosch.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductSPDNPick;
import com.bosch.product.api.domain.dto.EditBinDownQuantityDTO;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.service.IProductPickService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-30 15:16
 **/
@RestController
@Api(tags = "SUDN捡配接口")
@RequestMapping("/sudn-pick")
public class ProductPickController extends BaseController {


    @Autowired
    private IProductPickService pickService;

    @GetMapping(value = "/list")
    @ApiOperation("SUDN捡配列表")
    public R<PageVO<ProductPickVO>> list(ProductPickDTO queryDTO) {
        startPage();
        List<ProductPickVO> list = pickService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @DeleteMapping(value = "/{ids}")
    @ApiOperation("删除pick")
    public R batchDelete(@PathVariable Long[] ids) {
        pickService.batchCancel(Arrays.asList(ids));
        return R.ok();
    }

    @PutMapping(value = "/binDown/{qrCode}")
    @ApiOperation("SUDN捡配任务下架")
    @Transactional(rollbackFor = Exception.class)
    public R binDown(@PathVariable String qrCode,@RequestParam("sudnId") Long sudnId) {
        pickService.binDown(qrCode,sudnId);
        return R.ok(qrCode + "下架成功");
    }

    @GetMapping(value = "/getByQrCode/{qrCode}")
    @ApiOperation("获取单个")
    public R batchDelete(@PathVariable String qrCode,@RequestParam Long sudnId){
        LambdaQueryWrapper<ProductPick> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductPick::getSscc, ProductQRCodeUtil.getSSCC(qrCode));
        queryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.eq(ProductPick::getSudnId,sudnId);
        return R.ok(pickService.getOne(queryWrapper));
    }


    @PostMapping(value = "/editBinDownQuantity")
    @ApiOperation("修改下架量")
    public R ship(@RequestBody EditBinDownQuantityDTO dto) {

        pickService.editBinDownQuantity(dto);
        return R.ok();
    }







}
