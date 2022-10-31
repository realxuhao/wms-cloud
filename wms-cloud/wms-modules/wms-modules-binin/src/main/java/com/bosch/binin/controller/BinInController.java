package com.bosch.binin.controller;


import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.dto.BinInTaskDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.service.IBinAssignmentService;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @PostMapping(value = "/allocate")
    @ApiOperation("分配库位编码")
    public R<BinAllocationVO> allocate(@RequestBody BinAllocationDTO binAllocationDTO) {
        binAssignmentService.getBinAllocationVO(binAllocationDTO);
        return R.ok(null);
    }

    @PostMapping(value = "/generateInTask")
    @ApiOperation("生成上架任务")
    public R<BinInVO> generateInTask(@RequestBody BinInTaskDTO binInTaskDTO) {
        BinInVO binInVO = binInService.generateInTask(binInTaskDTO);
        return R.ok(binInVO);
    }

    @PostMapping(value = "/in")
    @ApiOperation("实际上架")
    public R<BinInVO> in(@RequestBody BinInDTO binInDTO) {
        binInService.performBinIn(binInDTO);
        return R.ok(null);
    }

    @GetMapping(value = "/list")
    @ApiOperation("查询上架列表")
    public R<PageVO<BinInVO>> list(BinInQueryDTO binInQueryDTO) {
        startPage();
        List<BinInVO> list = binInService.selectBinVOList(binInQueryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/currentUserData")
    @ApiOperation("查询当前用户的上架列表")
    public R<PageVO<BinInVO>> currentUserData(BinInQueryDTO binInQueryDTO) {
        if(binInQueryDTO==null){
            binInQueryDTO=new BinInQueryDTO();
        }
        binInQueryDTO.setCreateBy(SecurityUtils.getUsername());
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

}