package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.SSCCLogVO;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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

    @Autowired
    private IUserOperationLogService userOperationLogService;


    @PostMapping(value = "/add")
    @ApiOperation("新增移库任务")
    @Log(title = "新增移库任务", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> add(@RequestBody AddShiftTaskDTO dto) {
        return R.ok(shiftService.add(dto));
    }

    @PostMapping(value = "/addShiftTask")
    @ApiOperation("外库新增需求接口")
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addShiftTask(@RequestBody AddShiftTaskDTO dto) {
        return R.ok(shiftService.addShiftRequirement(dto));
    }


    @PutMapping(value = "/binDown/{mesBarCode}")
    @ApiOperation("移库任务下架")
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    @Log(title = "移库任务下架", businessType = BusinessType.UPDATE)
    public R binDown(@PathVariable String mesBarCode) {
        WareShift wareShift = shiftService.binDown(mesBarCode);
        userOperationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), null, SecurityUtils.getUsername(), UserOperationType.BINOUTOTHER.getCode(), MesBarCodeUtil.getSSCC(mesBarCode), MesBarCodeUtil.getMaterialNb(mesBarCode));

        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(wareShift.getSsccNb());
        ssccLogVO.setQuantity(wareShift.getQuantity());
        return R.ok(ssccLogVO, mesBarCode + "下架成功");
    }

    @PostMapping(value = "splitPallet")
    @ApiOperation("移库任务拆托")
    @Log(title = "移库任务拆托", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R splitPallet(@RequestBody SplitPalletDTO splitPallet) {
        shiftService.splitPallet(splitPallet);

        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(splitPallet.getSourceSsccNb());
        ssccLogVO.setQuantity(splitPallet.getSplitQuantity());
        ssccLogVO.setNewSSCCNumber(MesBarCodeUtil.getSSCC(splitPallet.getNewMesBarCode()));

        return R.ok(ssccLogVO);
    }

    @GetMapping(value = "/getOne/{mesBarCode}")
    @ApiOperation("获取移库任务详情")
    public R<WareShiftVO> info(@PathVariable String mesBarCode) {
        WareShiftQueryDTO queryDTO = new WareShiftQueryDTO();
        queryDTO.setSsccNb(MesBarCodeUtil.getSSCC(mesBarCode));
        List<WareShiftVO> wareShiftList = shiftService.getWareShiftList(queryDTO);
        if (CollectionUtils.isEmpty(wareShiftList)){
            throw new ServiceException("该SSCC"+MesBarCodeUtil.getSSCC(mesBarCode)+"下没有移库任务");
        }
        List<WareShiftVO> collect = wareShiftList.stream().filter(item -> item.getStatus() != KanbanStatusEnum.CANCEL.value()
                && item.getStatus() != KanbanStatusEnum.FINISH.value()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            throw new ServiceException("该SSCC"+MesBarCodeUtil.getSSCC(mesBarCode)+"下没有进行中的移库任务");
        }
        return R.ok(collect.get(0));
    }

    @PostMapping(value = "generateWareShiftByCall")
    @ApiOperation("根据call生成移库任务")
    @Log(title = "根据叫料需求生成移库任务", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public R generateWareShiftByCall(@RequestBody List<CallWareShiftDTO> dtos){
        shiftService.generateWareShiftByCall(dtos);
        return R.ok();
    }


    @GetMapping(value = "/getOneBinDown/{sscc}")
    @ApiOperation("获取单个待下架")
    public R<WareShift> getOne(@PathVariable("sscc") String sscc){
        WareShiftQueryDTO queryDTO = new WareShiftQueryDTO();
        queryDTO.setStatus(KanbanStatusEnum.WAITING_BIN_DOWN.value());
        queryDTO.setSsccNb(sscc);
        List<WareShiftVO> list = shiftService.getWareShiftList(queryDTO);
        if (CollectionUtils.isEmpty(list)){

        }
        LambdaQueryWrapper<WareShift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WareShift::getSsccNb,sscc)
                .eq(WareShift::getDeleteFlag,DeleteFlagStatus.FALSE)
                .eq(WareShift::getStatus,KanbanStatusEnum.WAITING_BIN_DOWN.value());
        queryWrapper.last("limit 1");
        WareShift one = shiftService.getOne(queryWrapper);
        if (one==null){
            throw new ServiceException("无该sscc对应的待下架信息");
        }
        return R.ok(shiftService.getOne(queryWrapper));
    }




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
        if (!StringUtils.isEmpty(SecurityUtils.getWareCode())) {
            queryDTO.setSourceWareCode(SecurityUtils.getWareCode());
        }
        startPage();
        List<WareShiftVO> list = shiftService.getWareShiftList(queryDTO);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/allocateBin/{mesBarCode}")
    @ApiOperation("移库任务上架分配库位")
    @Log(title = "移库任务分配库位", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R<BinInVO> allocateBin(@PathVariable("mesBarCode") String mesBarCode) {

        return R.ok(shiftService.allocateBin(mesBarCode, SecurityUtils.getWareCode()));
    }

    @GetMapping(value = "/getWaitingBinIn")
    @ApiOperation("查询移库任务待上架列表")
    public R<PageVO<WareShiftVO>> getWaitingBinIn(PageDomain pageDomain) {
        startPage();
        List<WareShiftVO> list = shiftService.getWaitingBinIn();
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    /**
     * 取消移库
     */
    @ApiOperation("取消移库任务")
    @PutMapping(value = "/cancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "移库取消", businessType = BusinessType.INSERT)
    public R cancelWareShift(@PathVariable("id") Long id) {
        WareShift wareShift = shiftService.cancelWareShift(id);
        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(wareShift.getSsccNb());
        ssccLogVO.setQuantity(wareShift.getQuantity());


        return R.ok(ssccLogVO);
    }



    @PostMapping(value = "/binIn")
    @ApiOperation("移库上架接口")
    @Log(title = "移库上架", businessType = BusinessType.INSERT)
    @Synchronized
    public R performBinIn(@RequestBody BinInDTO binInDTO) {
        BinInVO binInVO = shiftService.performBinIn(binInDTO);
        userOperationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), null,SecurityUtils.getUsername(), UserOperationType.SHIFT_BININ.getCode(), MesBarCodeUtil.getSSCC(binInDTO.getMesBarCode()),MesBarCodeUtil.getMaterialNb(binInDTO.getMesBarCode()));

        SSCCLogVO ssccLogVO = new SSCCLogVO();
        ssccLogVO.setSsccNumber(binInVO.getSsccNumber());
        ssccLogVO.setQuantity(binInVO.getQuantity());

        return R.ok();
    }

    @PostMapping(value = "/batchBinIn")
    @ApiOperation("批量上架到区域")
    @Synchronized
    @Log(title = "移库批量上架到区域", businessType = BusinessType.INSERT)
    public R batchPerformBinIn(@RequestBody WareShiftBatchBinInDTO dto) {
        List<BinIn> binIns = shiftService.batchPerformBinIn(dto);
        ArrayList<SSCCLogVO> logVOS = new ArrayList<>();

        binIns.stream().forEach(binIn -> {
            SSCCLogVO ssccLogVO = new SSCCLogVO();
            ssccLogVO.setSsccNumber(binIn.getSsccNumber());
            ssccLogVO.setQuantity(binIn.getQuantity());
            logVOS.add(ssccLogVO);
        });
        return R.ok(logVOS);
    }



    /**
     * 导出列表
     */
    @PostMapping("/exportExcel")
    @ApiOperation("移库列表导出")
    @Log(title = "移库列表导出", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, @RequestBody WareShiftQueryDTO queryDTO) {
        List<WareShiftVO> wareShiftList = shiftService.getWareShiftList(queryDTO);
        ExcelUtil<WareShiftVO> util = new ExcelUtil<>(WareShiftVO.class);
        util.exportExcel(response, wareShiftList, "移库任务列表");
    }

}
