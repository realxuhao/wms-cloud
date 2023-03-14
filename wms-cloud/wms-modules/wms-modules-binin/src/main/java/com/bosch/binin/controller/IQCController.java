package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static com.ruoyi.common.core.utils.PageUtils.startPage;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:17
 **/
@RestController
@Api(tags = "IQC")
@RequestMapping("/iqc")
public class IQCController extends BaseController {

    @Autowired
    private IIQCSamplePlanService samplePlanService;


    @GetMapping(value = "/sample/list")
    @ApiOperation("IQC抽样计划列表")
    public R<PageVO<IQCSamplePlanVO>> list(IQCSamplePlanQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new IQCSamplePlanQueryDTO();
        }

        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<IQCSamplePlanVO> list = samplePlanService.getSamplePlan(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/sample/manualAdd")
    @ApiOperation("手工添加IQC抽样计划")
    public R manualAdd(@RequestBody @Validated List<IQCSamplePlanDTO> dtos) {
        samplePlanService.manualAdd(dtos);
        return R.ok();
    }

    @GetMapping(value = "/sample/{mesBarCode}")
    @ApiOperation("获取单个IQC抽样计划")
    public R<IQCSamplePlanVO> info(@PathVariable("mesBarCode") String mesBarCode) {
        return R.ok(samplePlanService.info(mesBarCode));
    }

    @PostMapping(value = "/sample/modifySscc")
    @ApiOperation("修改抽样SSCC")
    public R modifySscc(@RequestBody @Validated IQCSamplePlanDTO dto) {
        samplePlanService.modifySscc(dto);
        return R.ok();
    }


//    @GetMapping(value = "/binDown/list")
//    @ApiOperation("IQC抽样计划待下架列表")
//    public R<List<IQCSamplePlanVO>> binDownList() {
//        IQCSamplePlanQueryDTO queryDTO = new IQCSamplePlanQueryDTO();
//        queryDTO.setStatusList(Arrays.asList(IQCStatusEnum.WAITING_BIN_DOWN.code()));
//        return R.ok(samplePlanService.getSamplePlan(queryDTO));
//    }

    @PutMapping(value = "/binDown/{mesBarCode}")
    @ApiOperation("IQC抽样计划执行下架接口")
    public R binDown(@PathVariable String mesBarCode) {
        samplePlanService.binDown(MesBarCodeUtil.getSSCC(mesBarCode));
        return R.ok();
    }

    @PutMapping(value = "/sample/cancel/{id}")
    @ApiOperation("IQC抽样计划执行取消接口")
    public R binDown(@PathVariable("id") Long id) {
        samplePlanService.cancel(id);
        return R.ok();
    }

    @GetMapping(value = "/binIn/{mesBarCode}")
    @ApiOperation("IQC抽样计划分配库位接口")
    public R<BinInVO> getBinInInfo(@PathVariable String mesBarCode) {
        BinInVO binInVO = samplePlanService.getBinInInfo(MesBarCodeUtil.getSSCC(mesBarCode));
        return R.ok(binInVO);
    }


    @PostMapping(value = "/binIn")
    @ApiOperation("IQC抽样计划执行上架接口")
    public R performBinIn(@RequestBody BinInDTO binInDTO) {
        samplePlanService.performBinIn(binInDTO);
        return R.ok();
    }

    @PostMapping(value = "/sample/confirm")
    @ApiOperation("IQC抽样确认")
    public R confirm(@RequestBody IQCSamplePlanDTO dto) {
        samplePlanService.confirm(dto);
        return R.ok();
    }


    /**
     * 导出列表
     */
    @PostMapping("/sample/export")
    @ApiOperation("列表导出")
    public void export(HttpServletResponse response, IQCSamplePlanQueryDTO queryDTO) {
        List<IQCSamplePlanVO> iqcSamplePlanVOS = samplePlanService.getSamplePlan(queryDTO);
//        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(list, MaterialCallVO.class);

        ExcelUtil<IQCSamplePlanVO> util = new ExcelUtil<>(IQCSamplePlanVO.class);
        util.exportExcel(response, iqcSamplePlanVOS, "IQC列表");
    }

    /**
     * IQC 外库的可以增加移库
     */
    @PostMapping(value = "/sample/addShift")
    @ApiOperation("IQC外库增加移库接口")
    public R addShift(@Valid  @RequestBody IQCWareShiftDTO dto) {
        samplePlanService.addShift(dto);
        return R.ok();
    }

    /**
     * IQC此库抽样，取消移库
     */
    @ApiOperation("IQC此库抽样，取消移库")
    @PutMapping(value = "/sample/cancelWareShift/{ssccNb}")
    @Transactional(rollbackFor = Exception.class)
    public R cancelWareShift(@PathVariable("ssccNb") String ssccNb) {

        samplePlanService.cancelWareShift(ssccNb);

        return R.ok();
    }


}
