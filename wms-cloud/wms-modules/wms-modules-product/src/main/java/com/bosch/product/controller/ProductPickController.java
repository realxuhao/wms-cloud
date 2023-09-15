package com.bosch.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductSPDNPick;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.enumeration.ProductPickEnum;
import com.bosch.product.api.domain.vo.ProductPickExportVO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.service.IProductPickService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Calendar;
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
        if (queryDTO != null && queryDTO.getStartCreateTime() != null) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(queryDTO.getStartCreateTime());
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            queryDTO.setStartCreateTime(instance.getTime());

        }
        startPage();
        List<ProductPickVO> list = pickService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/binDownlist")
    @ApiOperation("汇总的下架列表")
    public R<PageVO<ProductPickVO>> binDownlist(ProductPickDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ProductPickDTO();
        }
        if (StringUtils.isEmpty(queryDTO.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ProductPickVO> list = pickService.binDownlist(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getOneBinDown/{qrCode}")
    @ApiOperation("获取单个汇总后的下架数据")
    public R<ProductPickVO> getOneBinDown(@PathVariable("qrCode") String qrCode) {
        ProductPickDTO pickDTO = new ProductPickDTO();
        pickDTO.setSscc(ProductQRCodeUtil.getSSCC(qrCode));
        pickDTO.setStatus(ProductPickEnum.WAITTING_DOWN.code());
        List<ProductPickVO> list = pickService.binDownlist(pickDTO);
        if (!CollectionUtils.isEmpty(list)) {
            return R.ok(list.get(0));
        }
        return R.ok();
    }

    @GetMapping(value = "/getOneBinDownBySSCC/{sscc}")
    @ApiOperation("根据SSCC获取单个汇总后的下架数据")
    public R<ProductPickVO> getOneBinDownBySSCC(@PathVariable("sscc") String sscc) {
        ProductPickDTO pickDTO = new ProductPickDTO();
        pickDTO.setSscc(sscc);
        List<ProductPickVO> list = pickService.binDownlist(pickDTO);
        if (!CollectionUtils.isEmpty(list)) {
            return R.ok(list.get(0));
        }
        return R.ok();
    }

    @PutMapping(value = "/sumBinDown/{qrCode}")
    @ApiOperation("SUDN捡配任务汇总下架")
    @Log(title = "SUDN捡配任务汇总下架", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R binDown(@PathVariable String qrCode) {
        pickService.sumBinDown(qrCode);

        return R.ok(qrCode + "下架成功");
    }

    @PutMapping(value = "/sumBatchBinDown/{ssccs}")
    @ApiOperation("SUDN捡配任务汇总下架")
    @Log(title = "SUDN捡配任务汇总下架", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R batchBinDown(@PathVariable List<String> ssccs) {
        ssccs.stream().forEach(sscc -> pickService.sumBinDownBySSCC(sscc));
        return R.ok("下架成功");
    }


    @PostMapping(value = "/batchDelete/{ids}")
    @ApiOperation("删除SUDN捡配任务")
    @Log(title = "批量删除捡配", businessType = BusinessType.DELETE)
    public R batchDelete(@PathVariable Long[] ids) {
        pickService.batchCancel(Arrays.asList(ids));
        return R.ok();
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除SUDN捡配任务")
    @Log(title = "单个删除捡配", businessType = BusinessType.DELETE)
    public R delete(@PathVariable Long id) {
        pickService.cancel(id);
        return R.ok();
    }

    @PutMapping(value = "/binDown/{qrCode}")
    @ApiOperation("SUDN捡配任务下架")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "SUDN捡配任务下架", businessType = BusinessType.UPDATE)
    @Synchronized
    public R binDown(@PathVariable String qrCode, @RequestParam("sudnId") Long sudnId) {
        pickService.binDown(qrCode, sudnId);

        return R.ok(qrCode + "下架成功");
    }

    @GetMapping(value = "/getByQrCode/{qrCode}")
    @ApiOperation("获取单个")
    public R batchDelete(@PathVariable String qrCode, @RequestParam Long sudnId) {
        LambdaQueryWrapper<ProductPick> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductPick::getSscc, ProductQRCodeUtil.getSSCC(qrCode));
        queryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.eq(ProductPick::getSudnId, sudnId);
        return R.ok(pickService.getOne(queryWrapper));
    }


    @PostMapping(value = "/editBinDownQuantity")
    @ApiOperation("修改下架量")
    @Log(title = "SUDN捡配修改下架量", businessType = BusinessType.UPDATE)
    public R ship(@RequestBody EditBinDownQuantityDTO dto) {

        pickService.editBinDownQuantity(dto);
        return R.ok();
    }

    @PostMapping(value = "/batchIssue/{ids}")
    @Log(title = "SUDN捡配批量下发", businessType = BusinessType.UPDATE)
    @ApiOperation("批量下发")
    public R batchIssue(@PathVariable Long[] ids) {
        pickService.batchIssue(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 导出列表
     */
    @PostMapping("/exportExcel")
    @ApiOperation("SUDN捡配列表")
    public void export(HttpServletResponse response, @RequestBody ProductPickDTO queryDTO) {
        if (queryDTO != null && queryDTO.getStartCreateTime() != null) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(queryDTO.getStartCreateTime());
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            queryDTO.setStartCreateTime(instance.getTime());

        }
        List<ProductPickExportVO> list = pickService.getSUDNPickExportVO(queryDTO);
        list.stream().forEach(item -> {
            item.setItem("0000" + item.getItem());
            item.setDeliveryQuantityString(String.valueOf(item.getDeliveryQuantity().intValue()));
        });
        ExcelUtil<ProductPickExportVO> util = new ExcelUtil<>(ProductPickExportVO.class);
        util.exportExcel(response, list, "SUDN捡配列表");
    }

}
