package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.mapper.ManualTransferOrderMapper;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-07 16:15
 **/
@Service
public class IQCSamplePlanServiceImpl extends ServiceImpl<IQCSamplePlanMapper, IQCSamplePlan> implements IIQCSamplePlanService {

    @Autowired
    private IQCSamplePlanMapper samplePlanMapper;

    @Autowired
    private IStockService stockService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Override
    public List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto) {
        return samplePlanMapper.getSamplePlan(dto);
    }



    @Override
    public void manualAdd(List<IQCSamplePlanDTO> dto) {
        if (Objects.isNull(dto)) {
            throw new ServiceException("请选中数据后重试");
        }

        List<String> ssccList = dto.stream().map(IQCSamplePlanDTO::getSsccNb).collect(Collectors.toList());
        Map<String, IQCSamplePlanDTO> ssccMaps = dto.stream().collect(Collectors.toMap(IQCSamplePlanDTO::getSsccNb, Function.identity()));


        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.in(Stock::getSsccNumber, ssccList)
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode())
                .eq(Stock::getFreezeStock, (double) 0);
        List<Stock> stockList = stockService.list(stockQueryWrapper);
        if (Objects.isNull(stockList) || stockList.size() != ssccList.size()) {
            throw new ServiceException("库存数据过期，请刷新后重试");
        }

        List<String> materialNbList = stockList.stream().map(Stock::getMaterialNb).collect(Collectors.toList());
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialNbList(materialNbList);
        materialDTO.setPageSize(0);
        R<PageVO<MaterialVO>> R = remoteMaterialService.list(materialDTO);
        Map<String, MaterialVO> materialVOMap = new HashMap<>();
        if (R == null || !R.isSuccess()) {
            List<MaterialVO> materialVOS = R.getData().getRows();
            materialVOMap = materialVOS.stream().collect(Collectors.toMap(MaterialVO::getCode, Function.identity()));
        }


        List<IQCSamplePlan> samplePlanList = new ArrayList();
        Map<String, MaterialVO> finalMaterialVOMap = materialVOMap;
        stockList.stream().forEach(stock -> {
            if (stock.getTotalStock() < ssccMaps.get(stock.getSsccNumber()).getSampleQuantity()) {
                throw new ServiceException("抽样数量不能大于库存数量，请重试");
            }
            IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
            iqcSamplePlan.setSsccNb(stock.getSsccNumber());
            iqcSamplePlan.setCell(finalMaterialVOMap.get(stock.getMaterialNb()).getCell());
            iqcSamplePlan.setMaterielNb(stock.getMaterialNb());
            iqcSamplePlan.setBinDownCode(stock.getBinCode());
            iqcSamplePlan.setBinDownUser(SecurityUtils.getUsername());
            iqcSamplePlan.setBinDownTime(new Date());
            iqcSamplePlan.setRecommendSampleQuantity(ssccMaps.get(stock.getSsccNumber()).getSampleQuantity());
            samplePlanList.add(iqcSamplePlan);
            stock.setFreezeStock(stock.getAvailableStock());
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        });

        saveBatch(samplePlanList);

        stockService.updateBatchById(stockList);


    }
}
