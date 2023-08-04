package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.*;

import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.domain.vo.MaterialInfoVO;
import com.bosch.binin.api.enumeration.CallStatusEnum;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.*;

import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.system.api.domain.UserOperationLog;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruoyi.common.core.utils.PageUtils.startPage;


@RestController
@Api(tags = "kanban接口")
@RequestMapping("/materialKanban")
public class MaterialKanbanController {

    @Autowired
    private ITranshipmentOrderService transhipmentOrderService;
    @Autowired
    private IMaterialKanbanService materialKanbanService;
    @Autowired
    private IMaterialCallService materialCallService;

    @Autowired
    private IWareShiftService wareShiftService;

    @Autowired
    private IProductWareShiftService productWareShiftService;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IUserOperationLogService userOperationLogService;

    @GetMapping(value = "/list")
    @ApiOperation("查询kanban列表")
    public R<PageVO<MaterialKanbanVO>> list(MaterialKanbanDTO materialKanbanDTO) {
        if (Objects.isNull(materialKanbanDTO)) {
            materialKanbanDTO = new MaterialKanbanDTO();
        }
        if (StringUtils.isNotEmpty(SecurityUtils.getWareCode())) {
            materialKanbanDTO.setWareCode(SecurityUtils.getWareCode());
        }
        //IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pageList(materialKanbanDTO);
        //List<MaterialKanbanVO> records = materialKanbanIPage.getRecords();
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.getKanbanList(materialKanbanDTO);

        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @GetMapping(value = "/waitingBinDownList")
    @ApiOperation("查询kanban待下架列表")
    public R<PageVO<MaterialKanbanVO>> waitingBinDownList(PageDomain pageDomain) {
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.waitingBinDownList(pageDomain, SecurityUtils.getWareCode());
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/binDownList")
    @ApiOperation("查询kanban已下架列表")
    public R<PageVO<MaterialKanbanVO>> binDownList(PageDomain pageDomain) {
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.binDownList(pageDomain, SecurityUtils.getWareCode());
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }


    @GetMapping(value = "/getStockInfo")
    @ApiOperation("根据仓库code和物料code查询kanban任务中对应的库存信息")
    public R<List<StockVO>> getStockInfo(@RequestParam("materialNb") String materialNb,
                                         @RequestParam("wareCode") String wareCode) {
        List<StockVO> stockVOS = materialKanbanService.getStockInfo(materialNb, wareCode);
        return R.ok(stockVOS);
    }


    @PutMapping(value = "/issueJob/{ids}")
    @ApiOperation("批量下发任务接口")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "批量下发捡配任务接口", businessType = BusinessType.UPDATE)
    public R issueJob(@PathVariable Long[] ids) {
        materialKanbanService.issueJob(ids);
        return R.ok("下发成功");
    }

    @PutMapping(value = "/binDown/{ssccNb}")
    @ApiOperation("看板任务下架")
    @Log(title = "捡配任务下架", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R binDown(@PathVariable String ssccNb) {
        materialKanbanService.binDown(ssccNb);

        return R.ok(ssccNb + "下架成功");
    }

    @PostMapping(value = "splitPallet")
    @ApiOperation("看板任务拆托")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "捡配任务拆托", businessType = BusinessType.UPDATE)
    public R splitPallet(@RequestBody SplitPalletDTO splitPallet) {
        materialKanbanService.splitPallet(splitPallet);
        return R.ok();
    }


    @GetMapping(value = "/getWaitingJob/{mesBarCode}")
    @ApiOperation("扫码查询待下架任务信息")
    public R<MaterialKanbanVO> getWaitingJob(@PathVariable String mesBarCode) {
        MaterialKanbanVO materialKanbanVO = materialKanbanService.getWaitingJob(mesBarCode);
        return R.ok(materialKanbanVO);
    }


    @PostMapping(value = "/picking")
    @ApiOperation("人工拣配")
    @Log(title = "新增人工捡配", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    public R pickingByMan(@RequestBody List<MaterialKanbanDTO> materialKanbanDTOS) {

        try {
            if (CollectionUtils.isEmpty(materialKanbanDTOS)) {
                return R.fail("请选择数据！");
            }
            boolean b = materialKanbanService.checkStock(materialKanbanDTOS);
            if (!b) {
                return R.fail("库存已过期，请刷新页面");
            }
            List<Stock> stocks = materialKanbanService.selectStockList(materialKanbanDTOS);
            //kanban数据赋值
            List<MaterialKanban> materialKanbans = materialKanbanService.setValue(stocks, materialKanbanDTOS);

            //更新叫料需求下发量 状态
            MaterialCall materialCall = new MaterialCall();
            materialCall.setOrderNb(materialKanbanDTOS.get(0).getOrderNumber());
            materialCall.setMaterialNb(materialKanbanDTOS.get(0).getMaterialCode());
            Double newQuantity = new Double(0);
            for (MaterialKanbanDTO item : materialKanbanDTOS) {
                if (item.getQuantity() <= 0) {
                    throw new ServiceException(item.getSsccNumber() + "选择的数据不能小于0");
                }
                newQuantity = DoubleMathUtil.doubleMathCalculation(newQuantity, item.getQuantity(), "+");
            }
            materialCall.setIssuedQuantity(newQuantity);
            //kanban数据导入
            boolean b1 = materialKanbanService.saveBatch(materialKanbans);
            //叫料需求更新
            int i = materialCallService.updateCallStatus(materialCall);

            //sscc库存可用 冻结修改
            boolean updateStock = materialKanbanService.updateStocks(materialKanbans);
            if (!updateStock) {
                throw new ServiceException("库存冻结失败，请重试");
            }
            return R.ok();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(e.getMessage());
        }

    }

    /**
     * 删除kanban
     */
    @ApiOperation("删除kanban")
    @Log(title = "取消捡配任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable Long[] ids) {
        int i = 0;
        try {
            i = materialKanbanService.deleteByIds(ids);
        } catch (Exception ex) {
            return R.fail(ex.getMessage());
        }
        return R.ok(i);
    }

    /**
     * 修改kanban
     */
    @Log(title = "修改捡配任务", businessType = BusinessType.UPDATE)
    @ApiOperation("修改kanban")
    @PutMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R update(@PathVariable("id") Long id, @RequestBody @NotNull @Valid MaterialKanbanDTO kanbanDTO) {

        List<MaterialKanbanDTO> materialKanbanDTOS = new ArrayList<>();

        try {
            materialKanbanDTOS.add(kanbanDTO);
            //检查库存
            boolean b = materialKanbanService.checkStock(materialKanbanDTOS);
            if (!b) {
                return R.fail("库存已过期，请刷新页面");
            }
            //获取库存数据
            List<Stock> stocks = materialKanbanService.selectStockList(materialKanbanDTOS);
            //kanban数据赋值
            List<MaterialKanban> materialKanbans = materialKanbanService.setValue(stocks, materialKanbanDTOS);

            //料需求下发量 状态赋值
            MaterialCall materialCall = new MaterialCall();
            materialCall.setOrderNb(materialKanbanDTOS.get(0).getOrderNumber());
            materialCall.setMaterialNb(materialKanbanDTOS.get(0).getMaterialCode());
            Double newQuantity = new Double(0);
            for (MaterialKanbanDTO item : materialKanbanDTOS) {
                newQuantity = DoubleMathUtil.doubleMathCalculation(newQuantity, item.getQuantity(), "+");
            }
            materialCall.setIssuedQuantity(newQuantity);
            //更新叫料需求
            int i = materialCallService.updateCallStatus(materialCall, materialKanbans.get(0));
            //更新kanban
            boolean updateKanban = materialKanbanService.updateBatchById(materialKanbans);

            //sscc库存可用 冻结修改
            boolean updateStock = materialKanbanService.updateStocks(materialKanbans);
            if (!updateStock) {
                throw new ServiceException("库存冻结失败，请重试");
            }

        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(ex.getMessage());
        }
        return R.ok();
    }

    /**
     * 取消kanban
     */
    @ApiOperation("取消kanban")
    @PutMapping(value = "/cancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "取消捡配任务", businessType = BusinessType.DELETE)
    public R cancelKanban(@PathVariable("id") Long id) {

        try {
//            //查询取消任务详情
//            LambdaQueryWrapper<MaterialKanban> qw = new LambdaQueryWrapper<>();
//            qw.eq(MaterialKanban::getId, id);
//            qw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
//            qw.last("for update ");
//            MaterialKanban materialKanban = materialKanbanService.getOne(qw);
//            //kanban 修改取消状态
//            materialKanbanService.updateKanban(id);
//            //叫料需求的下发量修改
//            materialCallService.updateCallQuantity(materialKanban);
//            //sscc库存可用 冻结修改
//            materialKanbanService.updateStockBySSCC(materialKanban.getSsccNumber(),
//                    materialKanban.getQuantity());

            materialKanbanService.cancelKanban(id);


        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(ex.getMessage());
        }
        return R.ok();
    }


    @PostMapping(value = "/receivingMaterial")
    @ApiOperation("待收料列表")
    public R<PageVO<MaterialKanbanVO>> receivingMaterial(@RequestBody MaterialKanbanDTO dto) {
        String wareCode = SecurityUtils.getWareCode();

        dto.setWareCode(wareCode);
        PageDomain pageDomain= BeanConverUtil.conver(dto,PageDomain.class);

        startPage(pageDomain);
        List<MaterialKanbanVO> list = materialKanbanService.receivingMaterialList(dto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/receivedMaterial")
    @ApiOperation("已收料列表")
    public R<PageVO<MaterialKanbanVO>> receivedMaterial(@RequestBody MaterialKanbanDTO dto) {
        String wareCode = SecurityUtils.getWareCode();

        dto.setWareCode(wareCode);
        PageDomain pageDomain=BeanConverUtil.conver(dto,PageDomain.class);

        startPage(pageDomain);
        List<MaterialKanbanVO> list = materialKanbanService.receivedMaterialList(dto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/materialInfo")
    @ApiOperation("根据SSCC码查询收料信息接口")
    public R<PageVO<MaterialInfoVO>> materialInfo(@RequestParam(value = "mesBarCode") String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String wareCode = SecurityUtils.getWareCode();

        //startPage();
        List<MaterialInfoVO> materialInfoVOS = materialKanbanService.materialInfoList(sscc, wareCode);

        return R.ok(new PageVO<>(materialInfoVOS, new PageInfo<>(materialInfoVOS).getTotal()));
    }

    @PostMapping(value = "/genOrderAndSetStatus/{carNb}")
    @ApiOperation("生成转运单号,修改对应任务状态为主库待收货")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "转运发运", businessType = BusinessType.INSERT)
    @Synchronized
    public R genTranshipmentOrder(@RequestBody List<String> mesbarCodes, @PathVariable("carNb") String carNb) {
        try {
            List<TranshipmentOrder> transhipmentOrders = new ArrayList<>();
            List<String> ssccs = new ArrayList<>();
            List<String> validSSSCCs = new ArrayList<>();
//            long l = System.currentTimeMillis();
            String nextOrderNb = transhipmentOrderService.getNextOrderNb();
            if (CollectionUtils.isEmpty(mesbarCodes)) {
                throw new ServiceException("请选择数据");
            }
            //ssccs
            List<String> collect = mesbarCodes.stream().distinct().collect(Collectors.toList());
            collect.forEach(r -> {
                String sscc = MesBarCodeUtil.getSSCC(r);
                validSSSCCs.add(sscc);
            });
            //校验sscc在不在待发运单中
            List<WareShift> listBySSCC = wareShiftService.getListBySSCC(validSSSCCs);

            if (CollectionUtils.isEmpty(listBySSCC)) {
                throw new ServiceException("选择的sscc码不在待转运清单中");
            }
            if (collect.size() != listBySSCC.size()) {
                List<String> wareShiftSSCCs = listBySSCC.stream().map(WareShift::getSsccNb).collect(Collectors.toList());
                boolean b = validSSSCCs.removeAll(wareShiftSSCCs);
                throw new ServiceException("选择的如下sscc码不在待转运清单中: " + String.join(",", validSSSCCs));
            }
            List<TranshipmentOrder> infoBySSCC = transhipmentOrderService.getInfoBySSCC(validSSSCCs);
            if (CollectionUtils.isNotEmpty(infoBySSCC)) {
                throw new ServiceException("选择的sscc码在装运单中已存在");
            }
            listBySSCC.forEach(w -> {
                TranshipmentOrder transhipmentOrder = new TranshipmentOrder();
                transhipmentOrder.setOrderNumber(nextOrderNb);
                transhipmentOrder.setSsccNumber(w.getSsccNb());
                transhipmentOrder.setMaterialCode(w.getMaterialNb());
                transhipmentOrder.setWareShiftId(w.getId());
                transhipmentOrder.setCarNb(carNb);
                transhipmentOrders.add(transhipmentOrder);
                ssccs.add(w.getSsccNb());
            });
//            collect.forEach(r -> {
//                String sscc = MesBarCodeUtil.getSSCC(r);
//                String materialNb = MesBarCodeUtil.getMaterialNb(r);
//                TranshipmentOrder transhipmentOrder = new TranshipmentOrder();
//                transhipmentOrder.setOrderNumber(Long.toString(l));
//                transhipmentOrder.setSsccNumber(sscc);
//                transhipmentOrder.setMaterialCode(materialNb);
//                transhipmentOrders.add(transhipmentOrder);
//                ssccs.add(sscc);
//            });

            //List<TranshipmentOrder> transhipmentOrders = BeanConverUtil.converList(list, TranshipmentOrder.class);
            //生成转运单
            boolean b = transhipmentOrderService.saveBatch(transhipmentOrders);
            //更新kanban状态为主库待收货
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs, KanbanStatusEnum.OUT_DOWN.value(), KanbanStatusEnum.INNER_RECEIVING.value());
            //更新移库表为主库待收货
            int updateWare = wareShiftService.updateStatusByStatus(ssccs, KanbanStatusEnum.OUT_DOWN.value(),
                    KanbanStatusEnum.INNER_RECEIVING.value());
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(ex.getMessage());
        }
    }

    @GetMapping(value = "/getTranshipmentOrder")
    @ApiOperation("转运单查询")
    public R getTranshipmentOrder(@RequestParam(value = "mesbarCode") String mesbarCode) {

        try {
            String sscc = "";
            if (mesbarCode.length() <= 60 || mesbarCode.contains(".")) {
                sscc = MesBarCodeUtil.getSSCC(mesbarCode);
            } else {
                sscc = ProductQRCodeUtil.getSSCC(mesbarCode);
            }
            String order = transhipmentOrderService.getOrderBySSCC(sscc);
            //根据运单号获取相关sscc
            List<TranshipmentOrder> ssccByOrder = transhipmentOrderService.getSSCCByOrder(order);
            if (CollectionUtils.isEmpty(ssccByOrder)) {
                throw new ServiceException("根据运单号未获取相关sscc");
            }
            List<StockVO> vos = new ArrayList<>();
            List<String> collect =
                    ssccByOrder.stream().map(TranshipmentOrder::getSsccNumber).collect(Collectors.toList());
            //*根据sscc获取kanban信息
            //根据sscc获取stock信息
            if (mesbarCode.length() <= 60 || mesbarCode.contains(".")) {
                LambdaQueryWrapper<WareShift> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(WareShift::getSsccNb, collect);
                queryWrapper.eq(WareShift::getStatus, KanbanStatusEnum.INNER_RECEIVING.value());
                queryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                List<WareShift> wareShifts = wareShiftService.list(queryWrapper);
                return R.ok(wareShifts);
            }else {
                LambdaQueryWrapper<ProductWareShift> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.in(ProductWareShift::getSsccNb, collect);
                lambdaQueryWrapper.eq(ProductWareShift::getStatus, ProductWareShiftEnum.WAITTING_RECEIVING.code());
                lambdaQueryWrapper.eq(ProductWareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                List<ProductWareShift> productWareShifts = productWareShiftService.list(lambdaQueryWrapper);
                return R.ok(productWareShifts);
            }
            //            collect.stream().forEach(r -> {
//                StockVO oneBySSCC = stockService.getLastOneBySSCC(r);
//                if (oneBySSCC == null) {
//                    throw new ServiceException("根据sscc" + r + "未获取到库存信息");
//                }
//                vos.add(oneBySSCC);
//            });

//            return R.ok(vos);
        } catch (Exception ex) {
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    @PostMapping(value = "/validNumberInOrder")
    @ApiOperation("校验选择的sscc在不在转运单中")
    public R validNumberInOrder(@RequestParam(value = "sscc") List<String> sscc) {
        try {
            if (CollectionUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            //获取运单信息
            List<TranshipmentOrder> infoBySSCC = transhipmentOrderService.getInfoBySSCC(sscc);
            return R.ok();
        } catch (Exception ex) {
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    @PostMapping(value = "/updateKanbanStatus")
    @ApiOperation("修改kanban状态")
    @Transactional(rollbackFor = Exception.class)
    public R updateKanbanStatus(@RequestBody List<String> ssccs, @RequestParam("status") Integer status) {
        try {

            if (CollectionUtils.isEmpty(ssccs)) {
                throw new ServiceException("列表为空");
            }
            if (KanbanStatusEnum.getDesc(status.toString()) == null) {
                throw new ServiceException("请传入正确状态");
            }
            //更新任务状态
            int i = materialKanbanService.updateKanbanBySSCC(ssccs, status);
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(ex.getMessage());
        }
    }

    @PostMapping(value = "/confirmOrder")
    @ApiOperation("确认并入库")
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R confirmOrder(@RequestBody List<String> ssccs) {
        try {
            if (CollectionUtils.isEmpty(ssccs)) {
                throw new ServiceException("请选择数据");
            }
            //更新kanban状态为待上架
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs, KanbanStatusEnum.INNER_RECEIVING.value(), KanbanStatusEnum.INNER_BIN_IN.value());
            //更新移库表为待上架
            int updateWare = wareShiftService.updateStatusByStatus(ssccs, KanbanStatusEnum.INNER_RECEIVING.value(),
                    KanbanStatusEnum.INNER_BIN_IN.value());


            Integer updateOrder = transhipmentOrderService.updateBySSCCS(ssccs);
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    @GetMapping(value = "/getKanbanBySSCC")
    @ApiOperation("根据barcode的sscc获取kanban数据 注：若返回600为移库任务")
    public R getKanbanBySSCC(@RequestParam(value = "mesBarCode") String mesBarCode) {
        try {
            String sscc = mesBarCode;
            if (sscc.length() > 18) {
                sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            }

            if (StringUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            //根据sscc获取kanban
            MaterialKanbanVO kanbanBySSCC = materialKanbanService.getKanbanBySSCC(sscc);
            if (kanbanBySSCC == null) {
                return R.fail("没有该SSCC"+sscc+"对应的任务");
            }
            return R.ok(kanbanBySSCC);
        } catch (Exception ex) {
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }
    @GetMapping(value = "/getKanbanBySSCCAndStatus")
    @ApiOperation("根据barcode的sscc和状态获取kanban数据")
    public R getKanbanBySSCCAndStatus(@RequestParam(value = "mesBarCode") String mesBarCode) {
        try {
            String sscc = mesBarCode;
            if (sscc.length() > 18) {
                sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            }

            if (StringUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            //根据sscc获取kanban
            MaterialKanbanVO kanbanBySSCC = materialKanbanService.getKanbanBySSCCAndStatus(sscc,KanbanStatusEnum.INNER_DOWN);
            if (kanbanBySSCC == null) {
                return R.fail("没有该SSCC"+sscc+"对应的任务");
            }
            return R.ok(kanbanBySSCC);
        } catch (Exception ex) {
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }
    @GetMapping(value = "/deliver")
    @ApiOperation("整托下架配送接口")
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "捡配任务整托下架", businessType = BusinessType.UPDATE)
    @Synchronized
    public R deliver(@RequestParam(value = "sscc") String sscc) {
        try {
            //String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            if (StringUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            List<String> ssccs = new ArrayList<>();
            ssccs.add(sscc);
            //判断sscc在不在kanban中
            List<MaterialKanban> listBySCAndStatus = materialKanbanService.getListBySCAndStatus(ssccs,
                    KanbanStatusEnum.INNER_BIN_IN.value());
            if (CollectionUtils.isEmpty(listBySCAndStatus)) {
                throw new ServiceException("sscc在不在kanban任务中");
            }


            MaterialKanban kanban = listBySCAndStatus.get(0);
            UserOperationLog userOperationLog = new UserOperationLog();
            userOperationLog.setSsccNumber(kanban.getSsccNumber());
            userOperationLog.setCode(kanban.getMaterialCode());
            userOperationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), kanban.getOrderNumber(),SecurityUtils.getUsername(), UserOperationType.CALLOVER.getCode(),userOperationLog);



            //更新kanban状态从 待上架  到 产线待收货
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs, KanbanStatusEnum.INNER_BIN_IN.value(), KanbanStatusEnum.INNER_DOWN.value());
            //更新移库表从 待上架 到 完成
            int updateWare = wareShiftService.updateStatusByStatus(ssccs, KanbanStatusEnum.INNER_BIN_IN.value(),
                    KanbanStatusEnum.FINISH.value());
            if (updateKanban <= 0) {
                return R.fail("配送失败，请刷新重试");
            }
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    @GetMapping(value = "/confirmMaterial")
    @ApiOperation("收货确认")
    @Log(title = "捡配任务产线收货", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    @Synchronized
    public R confirmMaterial(@RequestParam(value = "mesBarCodes") List<String> mesBarCodes) {
        try {
            //String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            if (CollectionUtils.isEmpty(mesBarCodes)) {
                throw new ServiceException("请选择数据");
            }
            List<String> ssccs = mesBarCodes.stream().map(MesBarCodeUtil::getSSCC).collect(Collectors.toList());
//            List<String> ssccs = new ArrayList<>();
//            ssccs.add(sscc);
            //判断sscc在不在kanban中
            List<MaterialKanban> listBySCAndStatus = materialKanbanService.getListBySCAndStatus(ssccs,
                    KanbanStatusEnum.INNER_DOWN.value());
            if (CollectionUtils.isEmpty(listBySCAndStatus)) {
                throw new ServiceException("包含暂不能收货的数据");
            }
            //更新kanban状态从 产线待收货  到 产线已收货
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs, KanbanStatusEnum.INNER_DOWN.value(), KanbanStatusEnum.LINE_RECEIVED.value());

            if (updateKanban <= 0) {
                return R.fail("确认收货失败，请刷新重试");
            }
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    @GetMapping(value = "/confirmMaterialBySSCCs")
    @ApiOperation("多个sscc收货确认")
    @Log(title = "PC端产线收货确认", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public R confirmMaterialBySSCCs(@RequestParam(value = "ids") List<Long> ids) {
        try {
            //String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            if (CollectionUtils.isEmpty(ids)) {
                throw new ServiceException("请选择数据");
            }

//            List<String> ssccs = new ArrayList<>();
//            ssccs.add(sscc);
            //判断sscc在不在kanban中
            List<MaterialKanban> listBySCAndStatus = materialKanbanService.getListByIdAndStatus(ids,
                    KanbanStatusEnum.INNER_DOWN.value());
            if (CollectionUtils.isEmpty(listBySCAndStatus)) {
                throw new ServiceException("包含暂不能收货的数据");
            }

            //更新kanban状态从 产线待收货  到 产线已收货
            int updateKanban = materialKanbanService.updateKanbanByIdStatus(ids, KanbanStatusEnum.INNER_DOWN.value(), KanbanStatusEnum.LINE_RECEIVED.value());

            if (updateKanban <= 0) {
                return R.fail("确认收货失败，请刷新重试");
            }
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }

    /**
     * 导出叫料需求列表
     */
    @Log(title = "捡配任务列表导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("叫料需求列表导出")
    public void export(HttpServletResponse response, @RequestBody MaterialKanbanDTO queryDTO) {
        List<MaterialKanbanVO> materialKanbanVOS = materialKanbanService.getKanbanList(queryDTO);
//        List<MaterialCallVO> materialCallVOS = BeanConverUtil.converList(list, MaterialCallVO.class);

        ExcelUtil<MaterialKanbanVO> util = new ExcelUtil<>(MaterialKanbanVO.class);
        util.exportExcel(response, materialKanbanVOS, "叫料需求");
    }
}
