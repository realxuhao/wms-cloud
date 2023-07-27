package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.vo.StockTakeDetailVO;
import com.bosch.product.api.domain.vo.StockTakeTaskVO;
import com.bosch.product.service.IStockTakeDetailService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-17 13:55
 **/
@RestController
@Api(tags = "库存盘点详情")
@RequestMapping("/stock-take-detail")
public class StockTakePlanDetailController extends BaseController {

    @Autowired
    private IStockTakeDetailService detailService;

    @PostMapping(value = "/issue")
    @ApiOperation("下发")
    @Log(title = "下发盘点明细", businessType = BusinessType.UPDATE)
    public R issue(@RequestBody StockTakeDetailQueryDTO dto) {
        detailService.issue(dto);
        return R.ok();
    }


    @GetMapping(value = "/taskList")
    @ApiOperation("任务列表")
    public R<PageVO<StockTakeTaskVO>> taskList(StockTakeDetailQueryDTO dto) {
        if (dto == null) {
            dto = new StockTakeDetailQueryDTO();
        }
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            dto.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<StockTakeTaskVO> takeTaskVOS = detailService.getTaskList(dto);
        return R.ok(new PageVO<>(takeTaskVOS, new PageInfo<>(takeTaskVOS).getTotal()));
    }


    @GetMapping(value = "/list")
    @ApiOperation("detail列表")
    public R<PageVO<StockTakeDetailVO>> list(StockTakeDetailQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new StockTakeDetailQueryDTO();
        }
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<StockTakeDetailVO> detailVOS = detailService.getDetailList(queryDTO);
        return R.ok(new PageVO<>(detailVOS, new PageInfo<>(detailVOS).getTotal()));
    }


    @GetMapping(value = "/getByBarCode/{barCode}")
    @ApiOperation("扫码获取单个")
    public R<StockTakeDetailVO> getByBarCode(@PathVariable("barCode") String barCode) {
        String sscc = "";
        if (barCode.length() <=55) {
            sscc = MesBarCodeUtil.getSSCC(barCode);
        } else if (barCode.length() == 71) {
            sscc = ProductQRCodeUtil.getSSCC(barCode);
        }
        return R.ok(detailService.getByBarCode(sscc));
    }

    @PostMapping(value = "/operate")
    @ApiOperation("PDA盘点操作接口")
    @Log(title = "PDA盘点操作接口", businessType = BusinessType.UPDATE)
    public R operate(@RequestBody PdaTakeOperateDTO pdaTakeOperateDTO){
        detailService.operate(pdaTakeOperateDTO);
        return R.ok();
    }


    @PostMapping(value = "/confirm")
    @ApiOperation("库存确认接口")
    @Log(title = "盘点库存确认", businessType = BusinessType.UPDATE)
    public R confirm(@RequestBody StockTakeDetailQueryDTO queryDTO){
        detailService.confirm(queryDTO);
        return R.ok();
    }


    @PostMapping(value = "/editTakeQuantity")
    @ApiModelProperty("修改盘点数量")
    @Log(title = "修改盘点数量", businessType = BusinessType.UPDATE)
    public R editTakeQuantity(@RequestBody PdaTakeOperateDTO operateDTO){
        detailService.editTakeQuantity(operateDTO);
        return R.ok();
    }

    @PostMapping("/export")
    @ApiOperation("导出盘点明细")
    @Log(title = "导出盘点明细", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, StockTakeDetailQueryDTO dto) {
        List<StockTakeDetailVO> detailVOS = detailService.getDetailList(dto);
        ExcelUtil<StockTakeDetailVO> util = new ExcelUtil<>(StockTakeDetailVO.class);
        util.exportExcel(response, detailVOS, "盘点明细列表");
    }
}
