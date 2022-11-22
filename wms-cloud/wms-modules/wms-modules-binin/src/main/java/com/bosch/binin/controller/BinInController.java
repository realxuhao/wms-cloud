package com.bosch.binin.controller;


import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.dto.BinInTaskDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.service.IBinAssignmentService;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:42
 * @descriptio: 原材料上架接口
 */
@RestController
@Api(tags = "原材料上架接口")
@RequestMapping("/bin-in")
public class BinInController extends BaseController {

    @Autowired
    private IBinInService binInService;

    @Autowired
    private IBinAssignmentService binAssignmentService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @PostMapping(value = "/allocate")
    @ApiOperation("分配库位编码")
    public R<BinAllocationVO> allocate(@RequestBody BinAllocationDTO binAllocationDTO) {

        try {

            return R.ok(binAssignmentService.getBinAllocationVO(binAllocationDTO));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return R.fail(e.getMessage());
        }
        //Object listR = remoteMasterDataService.selectBinVOByFrameType("");

    }

    @PostMapping(value = "/generateInTask")
    @ApiOperation("生成上架任务")
    public R<BinInVO> generateInTask(@RequestBody BinInTaskDTO binInTaskDTO) {
        BinInVO binInVOByMesBarCode = binInService.getByMesBarCode(binInTaskDTO.getMesBarCode());
        if (binInVOByMesBarCode != null && StringUtils.isNotEmpty(binInVOByMesBarCode.getRecommendBinCode())) {
            throw new ServiceException("该物料已有上架任务或者已上架");
        }
        return R.ok(binInService.generateInTask(binInTaskDTO));
    }

    @PostMapping(value = "/in")
    @ApiOperation("实际上架")
    public R<BinInVO> in(@RequestBody BinInDTO binInDTO) {

        return R.ok(binInService.performBinIn(binInDTO));
    }


    @GetMapping(value = "/processingList")
    @ApiOperation("查询待上架列表")
    public R<PageVO<BinInVO>> processingList(BinInQueryDTO binInQueryDTO) {
        if (binInQueryDTO == null) {
            binInQueryDTO = new BinInQueryDTO();
        }
        if (StringUtils.isEmpty(binInQueryDTO.getWareCode())) {
            binInQueryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<BinInVO> list = binInService.selectProcessingBinVOList(binInQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/list")
    @ApiOperation("查询上架列表")
    public R<PageVO<BinInVO>> list(BinInQueryDTO binInQueryDTO) {
        if (binInQueryDTO == null) {
            binInQueryDTO = new BinInQueryDTO();
        }
        if (StringUtils.isEmpty(binInQueryDTO.getWareCode())) {
            binInQueryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<BinInVO> list = binInService.selectBinVOList(binInQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/currentUserData")
    @ApiOperation("查询当前用户的上架列表")
    public R<PageVO<BinInVO>> currentUserData(BinInQueryDTO binInQueryDTO) {
        if (binInQueryDTO == null) {
            binInQueryDTO = new BinInQueryDTO();

        }
        binInQueryDTO.setCreateBy(SecurityUtils.getUsername());
        if (StringUtils.isEmpty(binInQueryDTO.getWareCode())) {
            binInQueryDTO.setWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<BinInVO> list = binInService.selectBinVOList(binInQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/getByMesBarCode/{mesBarCode}")
    @ApiOperation("扫码查询上架信息")
    public R<BinInVO> getByMesBarCode(@PathVariable("mesBarCode") String mesBarCode) {
        BinInVO binInVO = binInService.getByMesBarCode(mesBarCode);
        return R.ok(binInVO);
    }


    @GetMapping(value = "/virtualPalletCode/{palletType}")
    @ApiOperation("获取虚拟编码")
    public R<Map> virtualPalletCode(@PathVariable("palletType") String palletType) {
        Map hashMap = new HashMap();
        hashMap.put("virtualPalletCode", binInService.virtualPalletCode(palletType));
        return R.ok(hashMap);
    }


    @ApiOperation("逻辑删除上架任务")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(binInService.deleteBinInById(id));
    }


}
