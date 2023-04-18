package com.bosch.product.controller;

import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.StockTakeDetail;
import com.bosch.product.api.domain.dto.PdaTakeOperateDTO;
import com.bosch.product.api.domain.dto.ProductWareShiftQueryDTO;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.api.domain.dto.StockTakeDetailQueryDTO;
import com.bosch.product.api.domain.vo.StockTakeDetailVO;
import com.bosch.product.api.domain.vo.StockTakeTaskVO;
import com.bosch.product.service.IStockTakeDetailService;
import com.bosch.product.service.IStockTakePlanService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.web.bind.annotation.*;

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
    public R issue(@RequestBody StockTakeDetailQueryDTO dto) {
        detailService.issue(dto);
        return R.ok();
    }


    @PostMapping(value = "/taskList")
    @ApiOperation("任务列表")
    public R<List<StockTakeTaskVO>> taskList(@RequestBody StockTakeDetailQueryDTO dto) {
        List<StockTakeTaskVO> takeTaskVOS = detailService.getTaskList(dto);
        return R.ok(takeTaskVOS);
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
        if (barCode.length() == 50) {
            sscc = MesBarCodeUtil.getSSCC(barCode);
        } else if (barCode.length() == 71) {
            sscc = ProductQRCodeUtil.getSSCC(barCode);
        }
        return R.ok(detailService.getByBarCode(sscc));
    }

    @PostMapping(value = "/operate")
    @ApiOperation("PDA盘点操作接口")
    public R operate(@RequestBody PdaTakeOperateDTO pdaTakeOperateDTO){
        detailService.operate(pdaTakeOperateDTO);
        return R.ok();
    }





}
