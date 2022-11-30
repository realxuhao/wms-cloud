package com.bosch.binin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.MaterialCall;

import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.service.IMaterialCallService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.masterdata.api.domain.Bin;
import com.bosch.masterdata.api.domain.vo.MaterialTypeVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.StringUtils;
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

import static com.ruoyi.common.core.utils.PageUtils.startPage;


@RestController
@Api(tags = "kanban接口")
@RequestMapping("/materialKanban")
public class MaterialKanbanController {

    @Autowired
    private IMaterialKanbanService materialKanbanService;
    @Autowired
    private IMaterialCallService materialCallService;

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


    @GetMapping(value = "/getStockInfo")
    @ApiOperation("根据仓库code和物料code查询kanban任务中对应的库存信息")
    public R<List<StockVO>> getStockInfo(@RequestParam("materialNb") String materialNb,
                                         @RequestParam("wareCode") String wareCode) {
        List<StockVO> stockVOS = materialKanbanService.getStockInfo(materialNb, wareCode);
        return R.ok(stockVOS);
    }


    @PutMapping(value = "/issueJob/{ids}")
    @ApiOperation("批量下发任务接口")
    public R issueJob(@PathVariable Long[] ids) {
        materialKanbanService.issueJob(ids);
        return R.ok("下发成功");
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
            if (!updateStock){
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
            int i = materialCallService.updateCallStatus(materialCall,materialKanbans.get(0));
            //更新kanban
            boolean updateKanban = materialKanbanService.updateBatchById(materialKanbans);

            //sscc库存可用 冻结修改
            boolean updateStock = materialKanbanService.updateStocks(materialKanbans);
            if (!updateStock){
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
            qw.last("for update");
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
        //dto.setStatus(KanbanStatusEnum.LINE_RECEIVING.value());
        dto.setWareCode(wareCode);
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.receivingMaterialList(dto);
        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }

    @PostMapping(value = "/receivedMaterial")
    @ApiOperation("已收料列表")
    public R<PageVO<MaterialKanbanVO>> receivedMaterial(@RequestBody MaterialKanbanDTO dto) {
        String wareCode = SecurityUtils.getWareCode();
        //dto.setStatus(KanbanStatusEnum.LINE_RECEIVING.value());
        dto.setWareCode(wareCode);
        startPage();
        List<MaterialKanbanVO> list = materialKanbanService.receivedMaterialList(dto);
        return R.ok(new PageVO<>(list,new PageInfo<>(list).getTotal()));
    }
}
