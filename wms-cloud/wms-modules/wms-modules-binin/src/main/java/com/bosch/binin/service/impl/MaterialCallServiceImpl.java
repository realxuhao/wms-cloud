package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.StockLog;
import com.bosch.binin.api.domain.MaterialCall;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.MaterialCallDTO;
import com.bosch.binin.api.domain.dto.MaterialCallQueryDTO;
import com.bosch.binin.api.domain.vo.MaterialCallVO;
import com.bosch.binin.api.domain.vo.RequirementResultVO;
import com.bosch.binin.api.enumeration.RequirementActionTypeEnum;
import com.bosch.binin.api.enumeration.MaterialCallSortTypeEnum;
import com.bosch.binin.api.enumeration.RequirementPerformTypeEnum;
import com.bosch.binin.mapper.MaterialCallMapper;
import com.bosch.binin.service.IMaterialCallService;
import com.bosch.binin.service.IMaterialKanbanService;
import com.bosch.binin.service.IStockLogService;
import com.bosch.binin.service.IStockService;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2022-11-10 14:12
 **/
@Service
public class MaterialCallServiceImpl extends ServiceImpl<MaterialCallMapper, MaterialCall> implements IMaterialCallService {


    @Autowired
    private MaterialCallMapper materialCallMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IStockLogService stockLogService;


    @Autowired
    private IMaterialKanbanService kanbanService;

    @Override
    public List<MaterialCall> getMaterialCallList(MaterialCallQueryDTO queryDTO) {

        LambdaQueryWrapper<MaterialCall> queryWrapper = new LambdaQueryWrapper();
        if (queryDTO != null) {
            queryWrapper.eq(StringUtils.isNotEmpty(queryDTO.getMaterialNb()), MaterialCall::getMaterialNb, queryDTO.getMaterialNb())
                    .eq(StringUtils.isNotEmpty(queryDTO.getCell()), MaterialCall::getCell, queryDTO.getCell())
                    .eq(StringUtils.isNotEmpty(queryDTO.getOrderNb()), MaterialCall::getOrderNb, queryDTO.getOrderNb())
                    .eq(StringUtils.isNotEmpty(queryDTO.getCreateBy()), MaterialCall::getCreateBy, queryDTO.getCreateBy())
                    .eq(ObjectUtils.isNotEmpty(queryDTO.getStatus()), MaterialCall::getStatus, queryDTO.getStatus())
                    .apply(ObjectUtils.allNotNull(queryDTO.getStartCreateTime()), "date_format (create_time,'%Y-%m-%d') >= date_format ({0},'%Y-%m-%d')", queryDTO.getStartCreateTime())
                    .apply(ObjectUtils.allNotNull(queryDTO.getEndCreateTime()), "date_format (create_time,'%Y-%m-%d') <= date_format ({0},'%Y-%m-%d')", queryDTO.getEndCreateTime())
                    .orderByAsc(MaterialCall::getStatus)
                    .orderByDesc(MaterialCall::getCreateTime);
        }
        List<MaterialCall> materialCalls = materialCallMapper.selectList(queryWrapper);
        return materialCalls;
    }


    @Override
    public boolean validList(List<MaterialCallDTO> dtos) {
        List<String> orderNbs = dtos.stream().map(MaterialCallDTO::getOrderNb).collect(Collectors.toList());
        LambdaQueryWrapper<MaterialCall> wrapper = new LambdaQueryWrapper<MaterialCall>();
        wrapper.in(MaterialCall::getOrderNb, orderNbs);
//        dtos.forEach(r->{
//            wrapper.or(wp->wp.eq(MaterialCall::getOrderNb,r.getOrderNb()).eq(MaterialCall::getMaterialNb,r.getMaterialNb()));
//        });
        Integer integer = materialCallMapper.selectCount(wrapper);

        return integer > 0;
    }

    @Override
    public RequirementResultVO converToRequirement(List<MaterialCall> dos, Integer sortType, String cell) {
        if (CollectionUtils.isEmpty(dos)) {
            return null;
        }

        dealMaterialCall(dos);

        updateBatchById(dos);

        Map<String, Double> materialQtyMap = dos.stream().
                collect(Collectors.groupingBy(item -> item.getOrderNb() + "-" + item.getMaterialNb(),
                        Collectors.summingDouble(MaterialCall::getQuantity)));

        //部分满足的物料号
        List<RequirementResultVO.MaterialOrder> unStatisfiedMaterialNbs = new ArrayList<>();
        //完全满足的物料号
        List<RequirementResultVO.MaterialOrder> fullStatisfiedMaterialNbs = new ArrayList<>();
        //没有库存的物料号
        List<RequirementResultVO.MaterialOrder> noStockMaterialNbs = new ArrayList<>();

        materialQtyMap.forEach((materialNbOrderNb, quantity) -> {
            String[] split = materialNbOrderNb.split("-");
            String materialNb = split[1];
            String orderNb = split[0];

            //先查询出来需要用到的结果
            List<Stock> useMaterialStockList = getUseMaterialStock(orderNb, materialNb, sortType, quantity,
                    unStatisfiedMaterialNbs, fullStatisfiedMaterialNbs, noStockMaterialNbs);
            //处理筛选出来的库存列表
            dealUseMaterialStockList(useMaterialStockList, sortType, quantity, orderNb, cell);
        });
        return RequirementResultVO.builder().fullStatisfiedMaterialNbs(fullStatisfiedMaterialNbs).noStockMaterialNbs(noStockMaterialNbs).unStatisfiedMaterialNbs(unStatisfiedMaterialNbs).build();
    }


    private void dealMaterialCall(List<MaterialCall> dos) {

        Map<String, List<MaterialCall>> sameOrderMaterialCallList = dos.stream().
                collect(Collectors.groupingBy(item -> item.getOrderNb() + "-" + item.getMaterialNb()));

        sameOrderMaterialCallList.forEach((materialOrder, materialCallList) -> {
            String[] split = materialOrder.split("-");
            String materialNb = split[1];
            String orderNb = split[0];
            List<Stock> stockList = getUseMaterialByNb(materialNb);
            Double stockSum = stockList.stream().collect(Collectors.summingDouble(Stock::getAvailableStock));
            int temp = 0;
            for (MaterialCall materialCall : materialCallList) {
                temp += materialCall.getQuantity();
                if (stockSum - temp >= 0) {
                    materialCall.setDeliveryQuantity(materialCall.getQuantity());
                    materialCall.setDiffQuantity(Double.valueOf(0));
                } else {
                    Double actual = stockSum - temp + materialCall.getQuantity();
                    materialCall.setDeliveryQuantity(actual > 0 ? actual : 0);
                    materialCall.setDiffQuantity(materialCall.getQuantity() - materialCall.getDeliveryQuantity());
                }
            }
        });


    }


    private void dealUseMaterialStockList(List<Stock> useMaterialStockList, Integer sortType,
                                          Double quantity, String orderNb, String cell) {
        if (CollectionUtils.isEmpty(useMaterialStockList)) {
            return;
        }
        //计算最后一托是整托还是拆托
        Double stockSum = useMaterialStockList.stream().collect(Collectors.summingDouble(Stock::getAvailableStock));
        Double deviation = null;
        boolean splitFlag = false;
        if (stockSum > quantity) {
            splitFlag = true;
            deviation = stockSum - quantity;
        }

        List<MaterialKanban> kanbanList = new ArrayList<>();
        for (int i = 0; i < useMaterialStockList.size(); i++) {
            Stock stock = useMaterialStockList.get(i);
            MaterialKanban kanban = new MaterialKanban();
            kanban.setOrderNumber(orderNb);
            kanban.setFactoryCode(stock.getPlantNb());
            kanban.setWareCode(stock.getWareCode());
            kanban.setAreaCode(stock.getAreaCode());
            kanban.setBinCode(stock.getBinCode());
            kanban.setMaterialCode(stock.getMaterialNb());
            kanban.setSsccNumber(stock.getSsccNumber());
            kanban.setCell(cell);
            kanban.setType(RequirementActionTypeEnum.FULL_BIN_DOWN.value());
            kanban.setQuantity(stock.getAvailableStock());
            kanban.setStatus(stock.getPlantNb().equals("7751") ? RequirementPerformTypeEnum.HAS_ISSUED.value()
                    : RequirementPerformTypeEnum.WAIT_ISSUE.value());
            kanban.setCreateBy(SecurityUtils.getUsername());
            kanban.setCreateTime(new Date());
            kanban.setMoveType(MoveTypeEnums.CALL.getCode());


            if (i == useMaterialStockList.size() - 1 && splitFlag) {
                kanban.setQuantity(stock.getAvailableStock() - deviation);
                kanban.setType(splitFlag ? RequirementActionTypeEnum.PART_BIN_DOWN.value() : RequirementActionTypeEnum.FULL_BIN_DOWN.value());
                stock.setFreezeStock(stock.getFreezeStock() + kanban.getQuantity());
                stock.setAvailableStock(stock.getAvailableStock() - kanban.getQuantity());
            } else {
                stock.setFreezeStock(stock.getFreezeStock() + stock.getAvailableStock());
                stock.setAvailableStock(Double.valueOf(0));

            }
            kanbanList.add(kanban);
        }
        //批量添加kanban
        kanbanService.saveBatch(kanbanList);
        //更新库存
        stockService.updateBatchById(useMaterialStockList);
        //批量添加库存日志表
        List<StockLog> stockLogList = BeanConverUtil.converList(useMaterialStockList, StockLog.class);
        stockLogList.forEach(item -> {
            item.setMoveType(MoveTypeEnums.CALL.getCode());
        });
        stockLogService.saveBatch(stockLogList);

    }


    private List<Stock> getUseMaterialByNb(String materialNb) {
        LambdaQueryWrapper<Stock> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Stock::getMaterialNb, materialNb);
        lambdaQueryWrapper.eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode());
        List<Stock> stockList = stockService.list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            stockList = new ArrayList<>();
        }
        return stockList;
    }

    private List<Stock> getUseMaterialStock(String orderNb, String materialNb, Integer sortType, Double quantity,
                                            List<RequirementResultVO.MaterialOrder> unStatisfiedMaterialNbs,
                                            List<RequirementResultVO.MaterialOrder> fullStatisfiedMaterialNbs,
                                            List<RequirementResultVO.MaterialOrder> noStockMaterialNbs) {
        //根据库存列表查询出来
        List<Stock> stockList = getUseMaterialByNb(materialNb);
        List<Stock> sortedStockList = new ArrayList<>();
        if (MaterialCallSortTypeEnum.BBD_FIRST.value().equals(sortType)) {
            sortedStockList = stockList.stream().filter(item -> item.getAvailableStock() != 0).sorted(Comparator.comparing(Stock::getExpireDate).thenComparing(Stock::getPlantNb)).collect(Collectors.toList());
        } else if (MaterialCallSortTypeEnum.MAIN_WARE_FIRST.value().equals(sortType)) {
            sortedStockList = stockList.stream().filter(item -> item.getAvailableStock() != 0).sorted(Comparator.comparing(Stock::getPlantNb).thenComparing(Stock::getExpireDate)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(sortedStockList)) {
            noStockMaterialNbs.add(RequirementResultVO.MaterialOrder.builder().materialNb(materialNb).orderNb(orderNb).build());
            return new ArrayList<>();
        }
        double count = 0;
        List<Stock> res = new ArrayList<>();
        for (Stock stock : sortedStockList) {
            count += stock.getAvailableStock();
            res.add(stock);
            if (count >= quantity) {
                break;
            }
        }
        if (count == 0) {
            noStockMaterialNbs.add(RequirementResultVO.MaterialOrder.builder().materialNb(materialNb).orderNb(orderNb).build());

        }
        //计算部分满足还是全部满足
        //完全满足
        if (count >= quantity) {
            fullStatisfiedMaterialNbs.add(RequirementResultVO.MaterialOrder.builder().materialNb(materialNb).orderNb(orderNb).build());
        }
        //部分满足
        if (count != 0 && count < quantity) {
            unStatisfiedMaterialNbs.add(RequirementResultVO.MaterialOrder.builder().materialNb(materialNb).orderNb(orderNb).build());
        }

        return res;
    }


}
