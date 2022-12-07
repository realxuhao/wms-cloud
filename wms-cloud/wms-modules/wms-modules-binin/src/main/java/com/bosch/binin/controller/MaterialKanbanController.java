package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.MaterialCall;

import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.domain.dto.TranshipmentOrderDTO;
import com.bosch.binin.api.domain.vo.MaterialInfoVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.vo.MaterialTypeVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.PageDomain;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private IStockService stockService;
    @PostMapping(value = "/list")
    @ApiOperation("查询kanban列表")
    public R<PageVO<MaterialKanbanVO>> list(@RequestBody MaterialKanbanDTO materialKanbanDTO) {
        if (Objects.isNull(materialKanbanDTO)) {
            materialKanbanDTO = new MaterialKanbanDTO();
        }
        if (StringUtils.isNotEmpty(SecurityUtils.getWareCode())) {
            materialKanbanDTO.setWareCode(SecurityUtils.getWareCode());
        }
        IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pageList(materialKanbanDTO);
        List<MaterialKanbanVO> records = materialKanbanIPage.getRecords();
        return R.ok(new PageVO<>(records, materialKanbanIPage.getTotal()));
    }

    @PostMapping(value = "/binDownList")
    @ApiOperation("查询kanban已下架列表")
    public R<PageVO<MaterialKanbanVO>> binDownList(@RequestBody PageDomain pageDomain) {
        IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pagebinDownList(pageDomain, SecurityUtils.getWareCode());
        List<MaterialKanbanVO> records = materialKanbanIPage.getRecords();
        return R.ok(new PageVO<>(records, materialKanbanIPage.getTotal()));
    }


    @GetMapping(value = "/getStockInfo")
    @ApiOperation("根据仓库code和物料code查询kanban任务中对应的库存信息")
    public R<List<StockVO>> getStockInfo(@RequestParam("materialNb") String materialNb,
                                         @RequestParam("wareCode") String wareCode) {
        List<StockVO> stockVOS = materialKanbanService.getStockInfo(materialNb, wareCode);
        return R.ok(stockVOS);
    }


    @PutMapping(value = "/issueJob/{ssccNumber}")
    @ApiOperation("批量下发任务接口")
    public R issueJob(@PathVariable String[] ssccNumbers) {
        materialKanbanService.issueJob(ssccNumbers);
        return R.ok("下发成功");
    }

    @PutMapping(value = "/binDown/{ssccNb}")
    @ApiOperation("看板任务下架")
    public R binDown(@PathVariable String ssccNb) {
        materialKanbanService.binDown(ssccNb);
        return R.ok(ssccNb + "下架成功");
    }

    @GetMapping(value = "/getWaitingJob/{mesBarCode}")
    @ApiOperation("扫码查询待下架任务信息")
    public R<List<MaterialKanbanVO>> getWaitingJob(@PathVariable String mesbarCode) {
        List<MaterialKanbanVO> materialKanbanVO = materialKanbanService.getWaitingJob(mesbarCode);
        return R.ok(materialKanbanVO);
    }


    @PostMapping(value = "/picking")
    @ApiOperation("人工拣配")
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
    @Log(title = "kanban", businessType = BusinessType.DELETE)
    @ApiOperation("删除kanban")
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
    @Log(title = "kanban", businessType = BusinessType.UPDATE)
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
    @Log(title = "kanban", businessType = BusinessType.CLEAN)
    @ApiOperation("取消kanban")
    @GetMapping(value = "/cancelKanban/{id}")
    @Transactional(rollbackFor = Exception.class)
    public R cancelKanban(@PathVariable("id") Long id) {

        try {
            //查询取消任务详情
            LambdaQueryWrapper<MaterialKanban> qw = new LambdaQueryWrapper<>();
            qw.eq(MaterialKanban::getId, id);
            qw.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            qw.last("for update ");
            MaterialKanban materialKanban = materialKanbanService.getOne(qw);
            //kanban 修改取消状态
            materialKanbanService.updateKanban(id);
            //叫料需求的下发量修改
            materialCallService.updateCallQuantity(materialKanban);
            //sscc库存可用 冻结修改
            materialKanbanService.updateStockBySSCC(materialKanban.getSsccNumber(),
                    materialKanban.getQuantity());
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
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.receivingMaterialList(dto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/receivedMaterial")
    @ApiOperation("已收料列表")
    public R<PageVO<MaterialKanbanVO>> receivedMaterial(@RequestBody MaterialKanbanDTO dto) {
        String wareCode = SecurityUtils.getWareCode();

        dto.setWareCode(wareCode);
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.receivedMaterialList(dto);
        return R.ok(new PageVO<>(list, new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/materialInfo")
    @ApiOperation("根据SSCC码查询收料信息接口")
    public R<PageVO<MaterialInfoVO>> materialInfo(@RequestParam(value = "mesBarCode") String mesBarCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        String wareCode = SecurityUtils.getWareCode();

        startPage();
        List<MaterialInfoVO> materialInfoVOS = materialKanbanService.materialInfoList(sscc, wareCode);

        return R.ok(new PageVO<>(materialInfoVOS, new PageInfo<>(materialInfoVOS).getTotal()));
    }

    @PostMapping(value = "/genOrderAndSetStatus")
    @ApiOperation("生成转运单号,修改对应任务状态为主库待收货")
    @Transactional(rollbackFor = Exception.class)
    public R genTranshipmentOrder(@RequestBody List<String> mesbarCodes ) {
        try {
            List<TranshipmentOrder> transhipmentOrders =new ArrayList<>();
            List<String> ssccs = new ArrayList<>();
            long l = System.currentTimeMillis();
            if(CollectionUtils.isEmpty(mesbarCodes)){
                throw new ServiceException("请选择数据");
            }
            List<String> collect = mesbarCodes.stream().distinct().collect(Collectors.toList());
            List<TranshipmentOrder> infoBySSCC = transhipmentOrderService.getInfoBySSCC(collect);
            if (CollectionUtils.isNotEmpty(infoBySSCC)){
                throw new ServiceException("选择的sscc码在装运单中已存在");
            }
            collect.forEach(r->{
                String sscc = MesBarCodeUtil.getSSCC(r);
                String materialNb = MesBarCodeUtil.getMaterialNb(r);
                TranshipmentOrder transhipmentOrder = new TranshipmentOrder();
                transhipmentOrder.setOrderNumber(Long.toString(l));
                transhipmentOrder.setSsccNumber(sscc);
                transhipmentOrder.setMaterialCode(materialNb);
                transhipmentOrders.add(transhipmentOrder);
                ssccs.add(sscc);
            });

            //List<TranshipmentOrder> transhipmentOrders = BeanConverUtil.converList(list, TranshipmentOrder.class);
            //生成转运单
            boolean b = transhipmentOrderService.saveBatch(transhipmentOrders);
            //更新kanban状态为主库待收货
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs,KanbanStatusEnum.OUT_DOWN.value(), KanbanStatusEnum.INNER_RECEIVING.value());
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
    public R<List<StockVO>> getTranshipmentOrder(@RequestParam(value = "mesbarCode") String mesbarCode) {

        try {
            String sscc = MesBarCodeUtil.getSSCC(mesbarCode);
            String order = transhipmentOrderService.getOrderBySSCC(sscc);
            //根据运单号获取相关sscc
            List<TranshipmentOrder> ssccByOrder = transhipmentOrderService.getSSCCByOrder(order);
            if (CollectionUtils.isEmpty(ssccByOrder)) {
                throw new ServiceException("根据运单号未获取相关sscc");
            }
            List<StockVO> vos=new ArrayList<>();
            List<String> collect =
                    ssccByOrder.stream().map(TranshipmentOrder::getSsccNumber).collect(Collectors.toList());
            //*根据sscc获取kanban信息
            //根据sscc获取stock信息
            collect.stream().forEach(r->{
                StockVO oneBySSCC = stockService.getOneBySSCC(r);
                if(oneBySSCC==null){
                    throw new ServiceException("根据sscc"+r+"未获取到库存信息");
                }
                vos.add(oneBySSCC);
            });

            return R.ok(vos);
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
    public R confirmOrder(@RequestParam(value = "sscc") List<String> ssccs) {
        try {
            if (CollectionUtils.isEmpty(ssccs)) {
                throw new ServiceException("请选择数据");
            }
            //更新kanban状态为待上架
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs,KanbanStatusEnum.INNER_RECEIVING.value(), KanbanStatusEnum.INNER_BIN_IN.value());
            //更新移库表为待上架
            int updateWare = wareShiftService.updateStatusByStatus(ssccs, KanbanStatusEnum.INNER_RECEIVING.value(),
                    KanbanStatusEnum.INNER_BIN_IN.value());
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }
    @GetMapping(value = "/getKanbanBySSCC")
    @ApiOperation("根据barcode的sscc获取kanban数据 注：若返回600为移库任务")
    public R getKanbanBySSCC(@RequestParam(value = "mesBarCode")String mesBarCode) {
        try {
            String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            if (StringUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            //根据sscc获取kanban
            MaterialKanbanVO kanbanBySSCC = materialKanbanService.getKanbanBySSCC(sscc);
            if (kanbanBySSCC==null){
                return R.ok(600,"移库任务");
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
    public R deliver(@RequestParam(value = "sscc")String sscc) {
        try {
            //String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
            if (StringUtils.isEmpty(sscc)) {
                throw new ServiceException("请选择数据");
            }
            List<String> ssccs=new ArrayList<>();
            ssccs.add(sscc);
            //更新kanban状态从 待上架  到 产线待收货
            int updateKanban = materialKanbanService.updateKanbanByStatus(ssccs,KanbanStatusEnum.INNER_BIN_IN.value(), KanbanStatusEnum.INNER_DOWN.value());
            //更新移库表从 待上架 到 完成
            int updateWare = wareShiftService.updateStatusByStatus(ssccs, KanbanStatusEnum.INNER_BIN_IN.value(),
                    KanbanStatusEnum.FINISH.value());
            if(updateKanban<=0){
                return R.fail("配送失败，请刷新重试");
            }
            return R.ok();
        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            ex.printStackTrace();
            return R.fail(ex.getMessage());
        }
    }
}
