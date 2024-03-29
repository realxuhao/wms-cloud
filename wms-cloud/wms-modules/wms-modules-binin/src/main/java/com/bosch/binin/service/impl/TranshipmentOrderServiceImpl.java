package com.bosch.binin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.mapper.TranshipmentOrderMapper;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.ITranshipmentOrderService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.StatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranshipmentOrderServiceImpl extends ServiceImpl<TranshipmentOrderMapper, TranshipmentOrder> implements ITranshipmentOrderService {

    @Autowired
    private MaterialKanbanMapper materialKanbanMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private TranshipmentOrderMapper transhipmentOrderMapper;


    @Override
    public List<TranshipmentOrder> getSSCCByOrder(String transhipmentOrder) {
        LambdaQueryWrapper<TranshipmentOrder> qw =new LambdaQueryWrapper<>();
        qw.eq(TranshipmentOrder::getOrderNumber,transhipmentOrder);
        qw.eq(TranshipmentOrder::getStatus, StatusEnums.FALSE.getCode());
        qw.eq(TranshipmentOrder::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        List<TranshipmentOrder> list = transhipmentOrderMapper.selectList(qw);
        return list;
    }

    @Override
    public String getOrderBySSCC(String sscc) {

        LambdaQueryWrapper<TranshipmentOrder> qw =new LambdaQueryWrapper<>();
        qw.eq(TranshipmentOrder::getSsccNumber,sscc);
        qw.eq(TranshipmentOrder::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        TranshipmentOrder transhipmentOrder = transhipmentOrderMapper.selectOne(qw);
        if (transhipmentOrder==null){
            throw  new ServiceException("根据ssccnumber未查询到运单号");
        }
        return transhipmentOrder.getOrderNumber();
    }

    @Override
    public List<TranshipmentOrder> getInfoBySSCC(List<String> sscc) {
        LambdaQueryWrapper<TranshipmentOrder> qw =new LambdaQueryWrapper<>();
        qw.in(TranshipmentOrder::getSsccNumber,sscc);
        qw.eq(TranshipmentOrder::getStatus, StatusEnums.FALSE.getCode());
        qw.eq(TranshipmentOrder::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        List<TranshipmentOrder> list = transhipmentOrderMapper.selectList(qw);
        return list;
    }

    @Override
    public TranshipmentOrder getOneBySSCC(String sscc) {
        LambdaQueryWrapper<TranshipmentOrder> qw =new LambdaQueryWrapper<>();
        qw.eq(TranshipmentOrder::getSsccNumber,sscc);
        qw.eq(TranshipmentOrder::getStatus, StatusEnums.FALSE.getCode());
        qw.eq(TranshipmentOrder::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        qw.last("limit 1");
        TranshipmentOrder transhipmentOrder = transhipmentOrderMapper.selectOne(qw);
        return transhipmentOrder;
    }
}
