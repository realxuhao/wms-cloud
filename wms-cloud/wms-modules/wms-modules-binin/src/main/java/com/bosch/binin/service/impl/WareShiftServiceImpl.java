package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.WareShift;
import com.bosch.binin.api.domain.dto.AddShiftTaskDTO;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.WareShiftVO;
import com.bosch.binin.api.enumeration.KanbanStatusEnum;
import com.bosch.binin.mapper.MaterialKanbanMapper;
import com.bosch.binin.mapper.WareShiftMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IWareShiftService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.plugin2.main.client.PrintBandDescriptor;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-16 20:48
 **/
@Service
public class WareShiftServiceImpl extends ServiceImpl<WareShiftMapper, WareShift> implements IWareShiftService {


    @Autowired
    private IStockService stockService;

    @Autowired
    private WareShiftMapper wareShiftMapper;

    @Autowired
    private IBinInService binInService;

    @Autowired
    private MaterialKanbanMapper kanbanMapper;

    @Autowired
    private IMaterialKanbanService kanbanService;

    @Override
    public Boolean addShiftRequirement(AddShiftTaskDTO dto) {
        List<String> ssccNbList = dto.getSsccNbList();
        if (CollectionUtils.isEmpty(ssccNbList)) {
            throw new ServiceException("请选择库存");
        }

        //先去再次校验一下库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccNbList);
        stockQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if (CollectionUtils.isEmpty(stockList) || stockList.size() != ssccNbList.size()) {
            throw new ServiceException("库存状态发生变更，请刷新页面");
        }

        List<WareShift> wareShiftList = new ArrayList<>();

        stockList.stream().forEach(item -> {
            WareShift wareShift = WareShift.builder().sourcePlantNb(item.getPlantNb()).sourceWareCode(item.getWareCode()).sourceAreaCode(item.getAreaCode())
                    .sourceBinCode(item.getBinCode()).materialNb(item.getMaterialNb()).batchNb(item.getBatchNb()).expireDate(item.getExpireDate())
                    .ssccNb(item.getSsccNumber()).deleteFlag(DeleteFlagStatus.FALSE.getCode()).moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .status(KanbanStatusEnum.WAITING_BIN_DOWN.value())
                    .build();
            wareShiftList.add(wareShift);
            //更新冻结库存
            item.setFreezeStock(item.getFreezeStock() + item.getAvailableStock());

        });
        //冻结库存，更新状态
        stockService.updateBatchById(stockList);
        return saveBatch(wareShiftList);


    }

    @Override
    public void binDown(String ssccNb) {

        LambdaQueryWrapper<WareShift> wareShiftLambdaQueryWrapper = new LambdaQueryWrapper<>();
        wareShiftLambdaQueryWrapper.eq(WareShift::getSsccNb, ssccNb);
        wareShiftLambdaQueryWrapper.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftLambdaQueryWrapper.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        wareShiftLambdaQueryWrapper.last("for update");
        WareShift wareShift = wareShiftMapper.selectOne(wareShiftLambdaQueryWrapper);
        if (Objects.isNull(wareShift) || !KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {
            throw new ServiceException("任务状态过期，请刷新后重试");
        }

        //在kanban任务中查询
        LambdaQueryWrapper<MaterialKanban> kanbanQueryWrapper = new LambdaQueryWrapper<>();
        kanbanQueryWrapper.eq(MaterialKanban::getSsccNumber, ssccNb);
        kanbanQueryWrapper.eq(MaterialKanban::getDeleteFlag, DeleteFlagStatus.FALSE);
        kanbanQueryWrapper.eq(MaterialKanban::getStatus, KanbanStatusEnum.WAITING_BIN_DOWN);
        MaterialKanban materialKanban = kanbanMapper.selectOne(kanbanQueryWrapper);
        //kanban任务修改状态
        if (materialKanban != null) {
            materialKanban.setStatus(KanbanStatusEnum.INNER_RECEIVING.value());
            kanbanMapper.updateById(materialKanban);
        }

        //状态修改为外库已下架
        wareShift.setStatus(KanbanStatusEnum.OUT_DOWN.value());
        wareShiftMapper.updateById(wareShift);

        //执行下架
        binInService.binDown(ssccNb);
    }

    @Override
    public BinInVO allocateBin(String mesBarCode, String wareCode) {
        //分配库位信息
        BinInVO binInVO = binInService.generateInTaskByOldStock(MesBarCodeUtil.getSSCC(mesBarCode), Double.valueOf(0), wareCode);

        return binInVO;
    }

    @Override
    public List<WareShiftVO> getWareShiftList(WareShiftQueryDTO queryDTO) {

        List<WareShiftVO> list = wareShiftMapper.list(queryDTO);
        return list;
    }

    @Override
    public List<WareShiftVO> getWaitingBinIn() {
        List<WareShiftVO> wareShiftVOS = wareShiftMapper.getWaitingBinIn(SecurityUtils.getWareCode());
        return wareShiftVOS;
    }

    @Override
    public void cancelWareShift(Long id) {
        LambdaQueryWrapper<WareShift> wareShiftQueryWrapper = new LambdaQueryWrapper<>();
        WareShift wareShift = wareShiftMapper.selectById(id);
        if (wareShift == null || wareShift.getDeleteFlag().equals(DeleteFlagStatus.TRUE.getCode())) {
            throw new ServiceException("移库任务不存在或者已删除");
        }
        if (wareShift.getStatus().equals(KanbanStatusEnum.CANCEL.value()) || wareShift.getStatus().equals(KanbanStatusEnum.FINISH.value()) || wareShift.getStatus().equals(KanbanStatusEnum.INNER_RECEIVING.value())) {
            throw new ServiceException("当前状态为： " + KanbanStatusEnum.getDesc(wareShift.getStatus().toString()) + " 不可以取消");
        }
        //待下架状态，需要判断是主库待下架还是外库待下架
        if (KanbanStatusEnum.WAITING_BIN_DOWN.value().equals(wareShift.getStatus())) {
            //如果不是空，走主库上架
            if (StringUtils.isNotEmpty(wareShift.getTargetWareCode())) {
                binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getTargetWareCode());

            } else {
                Stock stock = stockService.getAvailablesStockBySscc(wareShift.getSsccNb());
                if (stock != null) {
                    stock.setAvailableStock(stock.getTotalStock());
                    stock.setFreezeStock(Double.valueOf(0));
                    stockService.updateById(stock);
                }
            }
        }
        //外库待发运，取消后直接在外库上架
        if (KanbanStatusEnum.OUT_DOWN.value().equals(wareShift.getStatus())) {
            binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getSourceWareCode());

        }
        //待上架状态，直接在收货仓库上架
        if (KanbanStatusEnum.INNER_BIN_IN.value().equals(wareShift.getStatus())) {
            binInService.generateInTaskByOldStock(wareShift.getSsccNb(), Double.valueOf(0), wareShift.getTargetWareCode());
        }

        //如果kanban有，需要对应更新为取消
        MaterialKanban kanban = kanbanService.getOneBySCAndStatus(wareShift.getSsccNb(), KanbanStatusEnum.WAITING_ISSUE.value());
        if (kanban != null) {
            kanban.setStatus(KanbanStatusEnum.CANCEL.value());
        }
        kanbanService.updateById(kanban);

        wareShift.setStatus(KanbanStatusEnum.CANCEL.value());
        wareShiftMapper.updateById(wareShift);


    }

    @Override
    public WareShift getWareShiftBySsccAndStatus(String sscc) {
        LambdaQueryWrapper<WareShift> uw = new LambdaQueryWrapper<>();
        uw.in(WareShift::getSsccNb, sscc);
        uw.ne(WareShift::getStatus, KanbanStatusEnum.CANCEL.value());
        uw.ne(WareShift::getStatus, KanbanStatusEnum.FINISH.value());

        uw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        uw.last("limit 1");
        return wareShiftMapper.selectOne(uw);
    }

    @Override
    public int updateStatusByStatus(List<String> ssccs, Integer queryStatus, Integer status) {
        WareShift wareShift = new WareShift();
        wareShift.setStatus(status);
        LambdaUpdateWrapper<WareShift> uw = new LambdaUpdateWrapper<>();
        uw.in(WareShift::getSsccNb, ssccs);
        uw.eq(WareShift::getStatus, queryStatus);
        uw.eq(WareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        uw.eq(WareShift::getTargetWareCode, SecurityUtils.getWareCode());

        return wareShiftMapper.update(wareShift, uw);
    }
}
