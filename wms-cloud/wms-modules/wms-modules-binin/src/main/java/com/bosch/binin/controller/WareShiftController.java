package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> list(@RequestBody AddShiftTaskDTO dto) {
        return R.ok(shiftService.addShiftRequirement(dto));
    }


    @PutMapping(value = "/binDown/{mesBarCode}")
    @ApiOperation("移库任务下架")
    @Transactional(rollbackFor = Exception.class)
    public R binDown(@PathVariable String mesBarCode) {
        shiftService.binDown(mesBarCode);
        return R.ok(mesBarCode + "下架成功");
    }

//    @GetMapping(value = "/{mesBarCode}")
//    @ApiOperation("获取移库任务详情")
//    public R<WareShiftVO> info(@PathVariable String mesBarCode) {
//        WareShiftVO wareShiftVO = shiftService.info(mesBarCode);
//        return R.ok(mesBarCode + "下架成功");
//    }



    @GetMapping(value = "/innerReceivingList")
    @ApiOperation("查询主库待收货任务")
    public R<PageVO<WareShiftVO>> inneReceiveingList(WareShiftQueryDTO queryDTO) {
        startPage();
        if (queryDTO == null) {
            queryDTO = new WareShiftQueryDTO();
        }
        queryDTO.setStatus(KanbanStatusEnum.INNER_RECEIVING.value());


        List<WareShiftVO> list = shiftService.getWareShiftList(queryDTO);

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/list")
    @ApiOperation("移库任务列表")
    public R<PageVO<WareShiftVO>> list(WareShiftQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new WareShiftQueryDTO();
        }
        if (SecurityUtils.getWareCode() != null) {
            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<WareShiftVO> list = shiftService.getWareShiftList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("移库任务上架分配库位")
    @Transactional(rollbackFor = Exception.class)
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(shiftService.allocateBin(mesBarCode, SecurityUtils.getWareCode()));
    }

    @GetMapping(value = "/getWaitingBinIn")
    @ApiOperation("查询移库任务待上架列表")
    public R<PageVO<WareShiftVO>> getWaitingBinIn() {
        startPage();
        List<WareShiftVO> list = shiftService.getWaitingBinIn();
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 取消移库
     */
    @ApiOperation("取消移库任务")
    @GetMapping(value = "/cancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    public R cancelWareShift(@PathVariable("id") Long id) {

        shiftService.cancelWareShift(id);

        return R.ok();
    }


}
