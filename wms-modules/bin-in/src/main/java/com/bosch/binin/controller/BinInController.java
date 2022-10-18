package com.bosch.binin.controller;


import com.bosch.binin.domain.dto.BinAllocationDTO;
import com.bosch.binin.domain.dto.BinInDTO;
import com.bosch.binin.domain.dto.BinInQueryDTO;
import com.bosch.binin.domain.vo.BinAllocationVO;
import com.bosch.binin.domain.vo.BinInVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


/**
 * @author: UWH4SZH
 * @since: 10/18/2022 13:42
 * @descriptio: 原材料上架接口
 */
@RestController
@Api(tags = "原材料上架接口")
@RequestMapping("/bin-in")
public class BinInController {


    @PostMapping(value = "/allocate")
    @ApiOperation("分配库位编码")
    public R<BinAllocationVO> allocate(@RequestBody BinAllocationDTO binAllocationDTO) {
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
        return R.ok(null);
    }

    @GetMapping(value = "/currentUserData")
    @ApiOperation("查询当前用户的上架列表")
    public R<PageVO<BinInVO>> currentUserData(BinInQueryDTO binInQueryDTO) {
        return R.ok(null);
    }


}
