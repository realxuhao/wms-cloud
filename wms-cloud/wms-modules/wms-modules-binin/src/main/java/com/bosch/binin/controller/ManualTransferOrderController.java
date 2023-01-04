package com.bosch.binin.controller;

import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.service.IManualTransferOrderService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wms-cloud
 * @description: 手工创建转储单
 * @author: xuhao
 * @create: 2022-12-20 10:20
 **/
@RestController
@Api(tags = "手工创建转储单接口")
@RequestMapping("/manual-transfer")
public class ManualTransferOrderController extends BaseController {

    @Autowired
    private IManualTransferOrderService manualTransferOrderService;

    @GetMapping(value = "/list")
    @ApiOperation("转储单列表")
    public R<PageVO<ManualTransferOrderVO>> list(ManualTransQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new ManualTransQueryDTO();
        }
        if (SecurityUtils.getWareCode() != null) {
            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ManualTransferOrderVO> list = manualTransferOrderService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PostMapping(value = "/add")
    @ApiOperation("新增手工创建转储单")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> add(@RequestBody AddManualTransDTO dto) {
        return R.ok(manualTransferOrderService.add(dto));
    }

    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("分配库位")
    @Transactional(rollbackFor = Exception.class)
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(manualTransferOrderService.generateBinInJob(mesBarCode, SecurityUtils.getWareCode()));
    }


    @GetMapping(value = "/info/{mesBarCode}")
    @ApiOperation("mesBarCode")
    @Transactional(rollbackFor = Exception.class)
    public R<ManualTransferOrder> info(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(manualTransferOrderService.info(mesBarCode));
    }

    @PutMapping(value = "/issue/{ssccNumbers}")
    @ApiOperation("下发")
    @Transactional(rollbackFor = Exception.class)
    public R issueJob(@PathVariable String[] ssccNumbers) {
        manualTransferOrderService.issueJob(ssccNumbers);
        return R.ok("下发成功");
    }

    @PutMapping(value = "/cancel/{ssccNumbers}")
    @ApiOperation("取消")
    @Transactional(rollbackFor = Exception.class)
    public R cancel(@PathVariable String[] ssccNumbers) {
        manualTransferOrderService.cancel(ssccNumbers);
        return R.ok("取消成功");
    }


    @PutMapping(value = "/binDown/{mesBarCode}")
    @ApiOperation("转储单任务下架")
    @Transactional(rollbackFor = Exception.class)
    public R binDown(@PathVariable String mesBarCode) {
        manualTransferOrderService.binDown(mesBarCode);
        return R.ok(mesBarCode + "下架成功");
    }


    @PostMapping(value = "/in")
    @ApiOperation("转储单任务实际上架")
    public R<BinInVO> in(@RequestBody ManualBinInDTO binInDTO) {

        return R.ok(manualTransferOrderService.performBinIn(binInDTO));
    }


}
