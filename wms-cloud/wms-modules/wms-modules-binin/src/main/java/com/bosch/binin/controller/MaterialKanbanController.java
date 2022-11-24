package com.bosch.binin.controller;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.service.IMaterialCallService;
import com.bosch.masterdata.api.domain.dto.PalletDTO;
import com.google.common.collect.Maps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialKanbanDTO;
import com.bosch.binin.api.domain.vo.MaterialKanbanVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
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

        IPage<MaterialKanbanVO> materialKanbanIPage = materialKanbanService.pageList(materialKanbanDTO);

        return R.ok(new PageVO<>(materialKanbanIPage.getRecords(), materialKanbanIPage.getTotal()));
    }


    @GetMapping(value = "/getStockInfo")
    @ApiOperation("根据仓库code和物料code查询kanban任务中对应的库存信息")
    public R<List<StockVO>> getStockInfo(@RequestParam("materialNb") String materialNb,
                                         @RequestParam("wareCode") String wareCode) {
        List<StockVO> stockVOS = materialKanbanService.getStockInfo(materialNb, wareCode);
        return R.ok(stockVOS);
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
            //更新kanban
            boolean updateKanban = materialKanbanService.updateBatchById(materialKanbans);
            //更新叫料需求
            int i = materialCallService.updateCallStatus(materialCall);


        } catch (Exception ex) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//contoller中增加事务
            return R.fail(ex.getMessage());
        }
        return R.ok();
    }

    /**
     * 修改kanban
     */
    @Log(title = "kanban", businessType = BusinessType.CLEAN)
    @ApiOperation("取消kanban")
    @GetMapping(value = "/cancelKanban/{id}")
    @Transactional(rollbackFor = Exception.class)
    public R cancelKanban( @PathVariable("id") Long id) {

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

}
