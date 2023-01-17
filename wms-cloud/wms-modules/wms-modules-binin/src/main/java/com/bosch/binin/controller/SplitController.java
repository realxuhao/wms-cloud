package com.bosch.binin.controller;

import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.dto.SplitQuertDTO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.api.domain.vo.SplitRecordVO;
import com.bosch.binin.service.ISplitService;
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
 * @description:
 * @author: xuhao
 * @create: 2023-01-17 09:50
 **/
@RestController
@Api(tags = "拆托接口")
@RequestMapping("/split")
public class SplitController extends BaseController {

    @Autowired
    private ISplitService splitService;


    @PostMapping(value = "add")
    @ApiOperation("普通拆托")
    @Transactional(rollbackFor = Exception.class)
    public R splitPallet(@RequestBody SplitPalletDTO splitPallet) {
        splitService.add(splitPallet);
        return R.ok();
    }

    @GetMapping(value = "/list")
    @ApiOperation("转储单列表")
    public R<PageVO<SplitRecordVO>> list(SplitQuertDTO queryDTO) {
//        if (queryDTO == null) {
//            queryDTO = new SplitQuertDTO();
//        }
//        if (SecurityUtils.getWareCode() != null) {
//            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
//        }
        startPage();
        List<SplitRecordVO> list = splitService.getList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }



}
