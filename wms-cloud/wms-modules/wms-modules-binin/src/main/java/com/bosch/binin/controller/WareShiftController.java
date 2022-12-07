package com.bosch.binin.controller;

import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 21:15
 **/
@RestController
@Api(tags = "移库")
@RequestMapping("/ware-shift")
public class WareShiftController extends BaseController {

    @Autowired
    private IWareShiftService shiftService;

    @PostMapping(value = "/addShiftTask")
    @ApiOperation("外库新增需求接口")
    public R<Boolean> list(@RequestBody AddShiftTaskDTO dto) {
        return R.ok(shiftService.addShiftRequirement(dto));
    }


    @PutMapping(value = "/binDown/{ssccNb}")
    @ApiOperation("移库任务下架")
    public R binDown(@PathVariable String ssccNb) {
        shiftService.binDown(ssccNb);
        return R.ok(ssccNb + "下架成功");
    }


    @GetMapping(value = "/innerReceivingList")
    @ApiOperation("查询主库待收货任务")
    public R<PageVO<WareShift>> inneReceiveingList(WareShiftQueryDTO queryDTO) {
        startPage();
        List<WareShift> list = shiftService.getMaterialCallList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("移库任务上架分配库位")
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(shiftService.allocateBin(mesBarCode));
    }


}
