package com.bosch.binin.controller;

import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
        if (SecurityUtils.getWareCode() != null) {
            queryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<IQCSamplePlanVO> list = samplePlanService.getSamplePlan(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/sample/manualAdd")
    public R manualAdd(@RequestBody @Validated List<IQCSamplePlanDTO> dtos) {
        samplePlanService.manualAdd(dtos);
        return R.ok();
    }

    @PostMapping(value = "/sample/modifySscc")
    public R modifySscc(@RequestBody @Validated IQCSamplePlanDTO dto) {
        samplePlanService.modifySscc(dto);
        return R.ok();
    }


    @GetMapping(value = "/binDown/list")
    @ApiOperation("IQC抽样计划待下架列表")
    public R<List<IQCSamplePlanVO>> binDownList() {
        IQCSamplePlanQueryDTO queryDTO = new IQCSamplePlanQueryDTO();
        queryDTO.setStatusList(Arrays.asList(IQCStatusEnum.WAITING_BIN_DOWN.code()));
        return R.ok(samplePlanService.getSamplePlan(queryDTO));
    }




}
