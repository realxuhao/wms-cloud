package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.BinInDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanDTO;
import com.bosch.binin.api.domain.dto.IQCSamplePlanQueryDTO;
import com.bosch.binin.api.domain.vo.BinInVO;
import com.bosch.binin.api.domain.vo.IQCSamplePlanVO;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.mapper.IQCSamplePlanMapper;
import com.bosch.binin.service.IBinInService;
import com.bosch.binin.service.IIQCSamplePlanService;
import com.bosch.binin.service.IStockService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private IBinInService binInService;

    @Autowired
    private RemoteMaterialService remoteMaterialService;

    @Override
    public List<IQCSamplePlanVO> getSamplePlan(IQCSamplePlanQueryDTO dto) {
        return samplePlanMapper.getSamplePlanList(dto);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
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
            throw new ServiceException("不存在该物料");
        }
        List<MaterialVO> materialVOS = R.getData().getRows();
        materialVOMap = materialVOS.stream().collect(Collectors.toMap(MaterialVO::getCode, Function.identity()));


        List<IQCSamplePlan> samplePlanList = new ArrayList();
        Map<String, MaterialVO> finalMaterialVOMap = materialVOMap;
        stockList.stream().forEach(stock -> {
            if (stock.getTotalStock() < ssccMaps.get(stock.getSsccNumber()).getSampleQuantity()) {
                throw new ServiceException("抽样数量不能大于库存数量，请重试");
            }
            IQCSamplePlan iqcSamplePlan = new IQCSamplePlan();
            iqcSamplePlan.setSsccNb(stock.getSsccNumber());
            iqcSamplePlan.setCell(finalMaterialVOMap.get(stock.getMaterialNb()).getCell());
            iqcSamplePlan.setMaterialNb(stock.getMaterialNb());
            iqcSamplePlan.setBinDownCode(stock.getBinCode());
            iqcSamplePlan.setBinDownTime(new Date());
            iqcSamplePlan.setRecommendSampleQuantity(ssccMaps.get(stock.getSsccNumber()).getSampleQuantity());
            iqcSamplePlan.setStatus(IQCStatusEnum.WAITING_BIN_DOWN.code());
            iqcSamplePlan.setBatchNb(stock.getBatchNb());
            iqcSamplePlan.setWareCode(stock.getWareCode());
            iqcSamplePlan.setExpireDate(stock.getExpireDate());
            samplePlanList.add(iqcSamplePlan);
            stock.setFreezeStock(stock.getAvailableStock());
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        });

        saveBatch(samplePlanList);

        stockService.updateBatchById(stockList);


    }

    @Override
    public void modifySscc(IQCSamplePlanDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isNull(dto.getSourceSsccNb())) {
            throw new ServiceException("请选中数据后重试");
        }
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, dto.getSourceSsccNb());
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("sscc:" + dto.getSourceSsccNb() + "对应的抽样信息不存在");
        }

        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_DOWN.code()) {
            throw new ServiceException("状态为：" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可以修改");
        }


        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, dto.getSsccNb()).eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (!iqcSamplePlan.getMaterialNb().equals(stock.getMaterialNb())) {
            throw new ServiceException("只能修改为同一物料号的库存sscc");
        }
        if (Objects.isNull(stock) || stock.getFreezeStock() > 0) {
            throw new ServiceException("库存状态过期，请重新选择");
        }
        if (dto.getSampleQuantity() > stock.getTotalStock()) {
            throw new ServiceException("抽样数量不能大于库存数量");
        }
        //老任务取消
        iqcSamplePlan.setStatus(IQCStatusEnum.CANCEL.code());
        updateById(iqcSamplePlan);

        //新建任务
        IQCSamplePlan samplePlan = new IQCSamplePlan();
        samplePlan.setSsccNb(dto.getSsccNb());
        samplePlan.setCell(iqcSamplePlan.getCell());
        samplePlan.setMaterialNb(iqcSamplePlan.getMaterialNb());
        samplePlan.setBinDownCode(stock.getBinCode());
        samplePlan.setBatchNb(stock.getBatchNb());
        samplePlan.setWareCode(stock.getWareCode());
        samplePlan.setExpireDate(stock.getExpireDate());
        samplePlan.setBinDownTime(new Date());
        samplePlan.setRecommendSampleQuantity(dto.getSampleQuantity());
        samplePlan.setStatus(IQCStatusEnum.WAITING_BIN_DOWN.code());
        save(samplePlan);

        stock.setFreezeStock(stock.getAvailableStock());
        stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
        stockService.updateById(stock);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void binDown(String ssccNb) {
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, ssccNb);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + ssccNb + "对应的IQC抽样下架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_DOWN.code()) {
            throw new ServiceException("sscc:" + ssccNb + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可下架");
        }
        //删除库存
        LambdaQueryWrapper<Stock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(Stock::getSsccNumber, ssccNb).eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        Stock stock = stockService.getOne(stockQueryWrapper);
        if (stock == null) {
            throw new ServiceException("没有该sscc:" + ssccNb + "对应的库存数据");
        }
        stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        stockService.updateById(stock);

        //更新IQC任务状态
        iqcSamplePlan.setStatus(IQCStatusEnum.WAITING_SAMPLE.code());
        //更新下架人下架时间
        iqcSamplePlan.setBinDownUser(SecurityUtils.getUsername());
        iqcSamplePlan.setBinDownTime(new Date());
        samplePlanMapper.updateById(iqcSamplePlan);
    }

    @Override
    public BinInVO getBinInInfo(String sscc) {

        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, sscc);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的IQC抽样待上架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_IN.code()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可上架");
        }


        BinInVO binInVO = binInService.generateInTaskByOldStock(sscc, iqcSamplePlan.getSampleQuantity(), SecurityUtils.getWareCode());

        return binInVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void performBinIn(BinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = MesBarCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<IQCSamplePlan> iqcQueryWrapper = new LambdaQueryWrapper<>();
        iqcQueryWrapper.eq(IQCSamplePlan::getSsccNb, sscc);
        iqcQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        IQCSamplePlan iqcSamplePlan = samplePlanMapper.selectOne(iqcQueryWrapper);
        if (iqcSamplePlan == null) {
            throw new ServiceException("没有该sscc:" + sscc + "对应的IQC抽样待上架任务");
        }
        if (iqcSamplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_IN.code()) {
            throw new ServiceException("sscc:" + sscc + "对应任务状态为:" + IQCStatusEnum.getDesc(iqcSamplePlan.getStatus()) + ",不可上架");
        }
        BinInVO binInVO = binInService.performBinIn(binInDTO);

        iqcSamplePlan.setBinInCode(binInVO.getActualBinCode());
        iqcSamplePlan.setBinInTime(new Date());
        iqcSamplePlan.setBinInUser(SecurityUtils.getUsername());
        samplePlanMapper.updateById(iqcSamplePlan);

    }

    @Override
    public void cancel(Long id) {
        IQCSamplePlan samplePlan = getById(id);
        if (samplePlan == null || DeleteFlagStatus.TRUE.getCode().equals(samplePlan.getDeleteFlag())) {
            throw new ServiceException("无该IQC抽样待上架任务");
        }
        if (samplePlan.getStatus() == IQCStatusEnum.CANCEL.code()) {
            throw new ServiceException("该任务已经取消，不能再次取消");
        }
        if (samplePlan.getStatus() != IQCStatusEnum.WAITING_BIN_DOWN.code()) {
            throw new ServiceException("该任务状态为:" + IQCStatusEnum.getDesc(samplePlan.getStatus()) + ",不可上架");
        }
        samplePlan.setStatus(IQCStatusEnum.CANCEL.code());
        updateById(samplePlan);
    }

    @Override
    public IQCSamplePlanVO info(String mesBarCode) {
        IQCSamplePlanQueryDTO dto = new IQCSamplePlanQueryDTO();
        dto.setSsccNb(MesBarCodeUtil.getSSCC(mesBarCode));
        List<IQCSamplePlanVO> samplePlan = getSamplePlan(dto);
        if (CollectionUtils.isEmpty(samplePlan)) {
            throw new ServiceException("没有该sscc" + MesBarCodeUtil.getSSCC(mesBarCode) + "对应的IQC抽样任务");
        }
        return samplePlan.get(0);
    }


}
