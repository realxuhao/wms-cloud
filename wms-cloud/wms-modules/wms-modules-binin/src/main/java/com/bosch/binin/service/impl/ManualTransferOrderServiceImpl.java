package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.AddManualTransDTO;
import com.bosch.binin.api.domain.dto.ManualTransQueryDTO;
import com.bosch.binin.api.domain.vo.ManualTransferOrderVO;
import com.bosch.binin.api.enumeration.ManuTransStatusEnum;
import com.bosch.binin.api.enumeration.MaterialTransTypeEnum;
import com.bosch.binin.mapper.BinInMapper;
import com.bosch.binin.mapper.ManualTransferOrderMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IManualTransferOrderService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        });

        return saveBatch(list);
    }
}
