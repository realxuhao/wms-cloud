package com.bosch.binin.service.impl;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.*;
import com.bosch.binin.api.domain.dto.AddManualTransDTO;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.api.enumeration.ManuTransStatusEnum;
import com.bosch.binin.api.enumeration.MaterialTransTypeEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.ManualTransferOrderMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IManualTransferOrderService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-12-20 13:48
 **/
@Service
public class ManualTransferOrderServiceImpl extends ServiceImpl<ManualTransferOrderMapper, ManualTransferOrder> implements IManualTransferOrderService {

    @Autowired
    private ManualTransferOrderMapper manualTransferOrderMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private IStockService stockService;

    @Override
    public List<ManualTransferOrderVO> list(ManualTransQueryDTO queryDTO) {
        return manualTransferOrderMapper.list(queryDTO);
    }

    @Override
    public Boolean add(AddManualTransDTO dto) {
        List<Long> idList = dto.getIdList();
        String targetBinCode = dto.getTargetBinCode();
        String targetAreaCode = dto.getTargetAreaCode();
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选择库存后重试");
        }
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getId, idList);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockLambdaQueryWrapper.ne(Stock::getFreezeStock, Double.valueOf(0));
        List<Stock> stockList = stockMapper.selectList(stockLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList) || stockList.size() != idList.size()) {
            throw new ServiceException("库存状态过期，请刷新后重新选择");
        }
        List<ManualTransferOrder> list = new ArrayList<>();
        stockList.stream().forEach(item -> {
            ManualTransferOrder manualTransferOrder = new ManualTransferOrder();
            manualTransferOrder.setSourcePlantNb(item.getPlantNb());
            manualTransferOrder.setSourceWareCode(item.getWareCode());
            manualTransferOrder.setSourceAreaCode(item.getAreaCode());
            manualTransferOrder.setSourceBinCode(item.getBinCode());
            manualTransferOrder.setTargetAreaCode(targetAreaCode);
            if (dto.getType() == MaterialTransTypeEnum.NORMAL.code()) {
                manualTransferOrder.setTargetBinCode(targetBinCode);
            }
            manualTransferOrder.setType(dto.getType());
            manualTransferOrder.setMoveType(MoveTypeEnums.IN_TRANSFER.getCode());
            manualTransferOrder.setStatus(ManuTransStatusEnum.WAITING_ISSUE.code());
            list.add(manualTransferOrder);

            //stock更新
            item.setFreezeStock(item.getTotalStock());
            item.setAvailableStock(Double.valueOf(0));
        });

        stockService.updateBatchById(stockList);


        return saveBatch(list);
    }

    @Override
    public BinInVO allocateBin(String mesBarCode, String wareCode) {
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<ManualTransferOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(ManualTransferOrder::getSsccNb, sscc);
//        qw.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_BIN_IN.code());
        qw.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        qw.last("for update");
        ManualTransferOrder manualTransferOrder = manualTransferOrderMapper.selectOne(qw);

        if (Objects.isNull(manualTransferOrder)) {
            throw new ServiceException("该SSCC码 " + sscc + " 不存在转储任务或者状态已过期");
        }
        if (!ManuTransStatusEnum.WAITING_BIN_IN.code().equals(manualTransferOrder.getStatus())) {
            throw new ServiceException("该SSCC码 " + sscc + "对应任务状态为: " + ManuTransStatusEnum.getDesc(String.valueOf(manualTransferOrder.getStatus())) + " 不可分配库位 ");

        }
        //分配库位信息
        BinInVO binInVO = binInService.generateInTaskByOldStock(MesBarCodeUtil.getSSCC(mesBarCode), Double.valueOf(0), wareCode);

        return binInVO;
    }

    @Override
    public void issueJob(String[] ssccNumbers) {
        List<String> ssccNumberList = Arrays.asList(ssccNumbers);
        if (CollectionUtils.isEmpty(ssccNumberList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<ManualTransferOrder> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(ManualTransferOrder::getSsccNb, ssccNumberList);
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_ISSUE.code());

        List<ManualTransferOrder> manualTransferOrderList = manualTransferOrderMapper.selectList(kanbanLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(manualTransferOrderList) || manualTransferOrderList.size() != ssccNumberList.size()) {
            throw new ServiceException("数据已过期，请刷新后重新选择");
        }
        manualTransferOrderList.stream().forEach(item -> {
            item.setStatus(ManuTransStatusEnum.WAITING_BIN_DOWN.code());
        });

        updateBatchById(manualTransferOrderList);


    }

    @Override
    public void cancel(String[] ssccNumbers) {
        List<String> ssccNumberList = Arrays.asList(ssccNumbers);
        if (CollectionUtils.isEmpty(ssccNumberList)) {
            throw new ServiceException("任务为空，请选择任务后重试");
        }
        LambdaQueryWrapper<ManualTransferOrder> kanbanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        kanbanLambdaQueryWrapper.in(ManualTransferOrder::getSsccNb, ssccNumberList);
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        kanbanLambdaQueryWrapper.eq(ManualTransferOrder::getStatus, ManuTransStatusEnum.WAITING_ISSUE.code());

        List<ManualTransferOrder> manualTransferOrderList = manualTransferOrderMapper.selectList(kanbanLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(manualTransferOrderList) || manualTransferOrderList.size() != ssccNumberList.size()) {
            throw new ServiceException("数据已过期，请刷新后重新选择");
        }
        manualTransferOrderList.stream().forEach(item -> item.setStatus(ManuTransStatusEnum.CANCEL.code()));
        updateBatchById(manualTransferOrderList);

    }
}
