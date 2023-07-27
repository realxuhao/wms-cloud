package com.bosch.binin.controller;

import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.service.IManualTransferOrderService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
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
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<ManualTransferOrderVO> list = manualTransferOrderService.list(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @PostMapping(value = "/add")
    @ApiOperation("新增手工创建转储单")
    @Log(title = "新增手工创建转储单", businessType = BusinessType.INSERT)

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

    @PutMapping(value = "/issue/{ids}")
    @ApiOperation("下发")
    @Log(title = "转储下发", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public R issueJob(@PathVariable Long[] ids) {
        manualTransferOrderService.issueJob(ids);
        return R.ok("下发成功");
    }

    @PutMapping(value = "/cancel/{ssccNumbers}")
    @ApiOperation("取消")
    @Log(title = "转储取消", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public R cancel(@PathVariable String[] ssccNumbers) {
        manualTransferOrderService.cancel(ssccNumbers);
        return R.ok("取消成功");
    }


    @PutMapping(value = "/binDown/{mesBarCode}")
    @ApiOperation("转储单任务下架")
    @Log(title = "转储单任务下架", businessType = BusinessType.DELETE)
    @Transactional(rollbackFor = Exception.class)
    public R binDown(@PathVariable String mesBarCode) {
        manualTransferOrderService.binDown(mesBarCode);
        return R.ok(mesBarCode + "下架成功");
    }


    @PostMapping(value = "/in")
    @ApiOperation("转储单任务实际上架")
    @Log(title = "转储单任务实际上架", businessType = BusinessType.INSERT)
    public R<BinInVO> in(@RequestBody ManualBinInDTO binInDTO) {

        return R.ok(manualTransferOrderService.performBinIn(binInDTO));
    }


    @PostMapping(value = "/trans")
    @Log(title = "PDA实际转储", businessType = BusinessType.INSERT)
    @ApiOperation("转储")
    public R trans(@RequestBody ManualBinInDTO binInDTO) {
        manualTransferOrderService.trans(binInDTO);
        return R.ok("转储成功");
    }

}
