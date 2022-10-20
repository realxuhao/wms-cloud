package com.bosch.binin.controller;


import com.bosch.binin.api.domain.dto.BinAllocationDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.BinInQueryDTO;
import com.bosch.binin.api.domain.vo.BinAllocationVO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.service.IBinInService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
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


    @PostMapping(value = "/allocate")
    @ApiOperation("分配库位编码")
    public R<BinAllocationVO> allocate(@RequestBody BinAllocationDTO binAllocationDTO) {
        binInService.allocateBinCode(binAllocationDTO);
        return R.ok(null);
    }

    @PostMapping(value = "/in")
    @ApiOperation("实际上架")
    public R<BinInVO> in(@RequestBody BinInDTO binInDTO) {
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
        return R.ok(null);
    }

    @GetMapping(value = "/getByMesBarCode/{mesBarCOde}")
    @ApiOperation("扫码查询上架信息")
    public R<BinInVO> getByMesBarCode(@PathVariable("mesBarCOde") String mesBarCOde) {
        return R.ok(null);
    }


    @GetMapping(value = "/virtualPalletCode/{palletType}")
    @ApiOperation("获取虚拟编码")
    public R<Map> virtualPalletCode(@PathVariable("palletType") String palletType) {
        Map hashMap = new HashMap();
        hashMap.put("virtualPalletCode", binInService.virtualPalletCode(palletType));
        return R.ok(hashMap);
    }

}
