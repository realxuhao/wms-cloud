package com.bosch.binin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.BinIn;
import com.bosch.binin.api.domain.IQCSamplePlan;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.StockAdjust;
import com.bosch.binin.api.domain.dto.*;
import com.bosch.binin.api.domain.vo.JobVO;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.enumeration.BinInStatusEnum;
import com.bosch.binin.api.enumeration.IQCStatusEnum;
import com.bosch.binin.mapper.StockMapper;
import com.bosch.binin.service.*;
import com.bosch.binin.utils.BeanConverUtil;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.dto.IQCDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.IQCVO;
import com.bosch.product.api.domain.ProComparison;
import com.bosch.system.api.domain.UserOperationLog;
import com.ruoyi.common.core.constant.AreaListConstants;
import com.ruoyi.common.core.constant.DataTranslateAspect;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.BaseEntity;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: UWH4SZH
 * @since: 10/19/2022 15:59
 * @description:
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    private static Map<String, AreaVO> areaMap = new HashMap();

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;

    @Autowired
    @Lazy
    private IBinInService binInService;


    @Autowired
    private IStockAdjustService stockAdjustService;

    @Autowired
    @Lazy
    private IIQCSamplePlanService iiqcSamplePlanService;


    @Autowired
    @Lazy
    private IJobService jobService;

    @Override
    public List<StockVO> selectStockVOList(StockQueryDTO stockQueryDTO) {

        return stockMapper.selectStockVOList(stockQueryDTO);
    }

    @Override
    public List<StockVO> selectStockVOListBySSCC(List<String> ssccs) {
        return stockMapper.selectStockVOListBySSCC(ssccs);
    }

    @Override
    public List<StockVO> selectIQCManagementList(IQCManagementQueryDTO iqcManagementQueryDTO) {

        return stockMapper.selectIQCManagementList(iqcManagementQueryDTO);
    }

    @Override
    public boolean validateStatus(Long id) {
        return stockMapper.validateStatus(id) == 1;
    }

    @Override
    public Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO) {
//        Integer i = new Integer(0);
//        if (iqcChangeStatusDTO.getType().equals(0)) {
//            i = stockMapper.changeStatus(iqcChangeStatusDTO);
//        } else {
//            LambdaQueryWrapper<Stock> queryWrapper=new LambdaQueryWrapper<>();
//            queryWrapper.eq(Stock::getId,iqcChangeStatusDTO.getId());
//
//            stockMapper.selectOne(queryWrapper)
//            i = stockMapper.changeStatus(iqcChangeStatusDTO);
//        }
        Integer i = stockMapper.changeStatus(iqcChangeStatusDTO, SecurityUtils.getUsername(), DateUtils.getNowDate());
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<IQCVO> excelChangeStatus(List<IQCDTO> list) {
        List<IQCVO> result = new ArrayList<>();
        List<UserOperationLog> userOperationLogList = new ArrayList<>();

        List<String> ssccList = list.stream().map(IQCDTO::getSsccnumber).collect(Collectors.toList());
        LambdaQueryWrapper<IQCSamplePlan> iqcSampleQueryWrapper = new LambdaQueryWrapper<>();
        iqcSampleQueryWrapper.in(IQCSamplePlan::getSsccNb, ssccList);
        iqcSampleQueryWrapper.eq(IQCSamplePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<IQCSamplePlan> iqcSamplePlans = iiqcSamplePlanService.list(iqcSampleQueryWrapper);
        List<String> issueSSCCList = iqcSamplePlans.stream().filter(iqcSamplePlan -> iqcSamplePlan.getStatus().equals(IQCStatusEnum.WAAITTING_ISSUE.code())).map(IQCSamplePlan::getSsccNb).collect(Collectors.toList());


        // 定义每个线程处理的数据量
        int batchSize = 100;
        // 计算需要启动的线程数
        int threadCount = (int) Math.ceil((double) list.size() / batchSize);
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 使用循环分配数据给每个线程处理
        for (int i = 0; i < threadCount; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, list.size());
            // 创建一个线程处理当前批次的数据
            executorService.execute(() -> {
                // 获取当前线程处理的数据
                List<IQCDTO> batchList = list.subList(start, end);
                // 执行批量更新操作
                for (IQCDTO iqcdto : batchList) {
                    LambdaUpdateWrapper<Stock> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Stock::getSsccNumber, iqcdto.getSsccnumber());
                    wrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
                    wrapper.notIn(!CollectionUtils.isEmpty(issueSSCCList), Stock::getSsccNumber, issueSSCCList);
                    wrapper.set(Stock::getQualityStatus, iqcdto.getFinalSAPStatus());
                    wrapper.set(Stock::getChangeStatus, 1);
                    wrapper.set(Stock::getUpdateBy, SecurityUtils.getUsername());
                    wrapper.set(Stock::getUpdateTime, DateUtils.getNowDate());
                    wrapper.set(Stock::getIqcUpdateBy, SecurityUtils.getUsername());
                    wrapper.set(Stock::getIqcUpdateTime, DateUtils.getNowDate());
                    boolean update = this.update(wrapper);

                    IQCVO conver = BeanConverUtil.conver(iqcdto, IQCVO.class);
                    conver.setStatus(update ? 0 : 1);
                    //result.add(conver);
                    synchronized (result) { // 确保线程安全
                        result.add(conver); // 添加到结果列表
                    }
                }
            });
        }


        // 关闭线程池
        executorService.shutdown();
        try {
            // 等待所有线程执行完毕
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // 处理线程被中断的异常
            e.printStackTrace();
        }

        if (!CollectionUtils.isEmpty(issueSSCCList)) {
            result.stream().forEach(r -> {
                if (issueSSCCList.contains(r.getSsccnumber())) {
                    r.setStatus(3);
                }
            });
        }

        result.stream().forEach(r->{
            if (r.getStatus() == 1){
                JobVO jobDescBySSCC = jobService.getJobDescBySSCC(r.getSsccnumber());
                if (!jobDescBySSCC.getJobDesc().equals("无任务")) {
                    LambdaUpdateWrapper<Stock> wrapper = new LambdaUpdateWrapper<>();
                    wrapper.eq(Stock::getSsccNumber, r.getSsccnumber());
                    wrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
                    wrapper.orderByDesc(Stock::getUpdateTime);
                    wrapper.last("limit 1");
                    wrapper.set(Stock::getQualityStatus, r.getFinalSAPStatus());
                    wrapper.set(Stock::getChangeStatus, 1);
                    wrapper.set(Stock::getUpdateBy, SecurityUtils.getUsername());
                    wrapper.set(Stock::getUpdateTime, DateUtils.getNowDate());
                    wrapper.set(Stock::getIqcUpdateBy, SecurityUtils.getUsername());
                    wrapper.set(Stock::getIqcUpdateTime, DateUtils.getNowDate());
                    boolean update = this.update(wrapper);


                    r.setStatus(update ? 4 : 1);
                }
            }
        });





        List<IQCVO> stockVOS = mapToMaterial(result);


        return stockVOS;
    }

    @Override
    public List<IQCVO> mapToMaterial(List<IQCVO> result) {
        List<String> ssccList = result.stream().map(IQCVO::getSsccnumber).collect(Collectors.toList());
        List<StockVO> stockVOS = stockMapper.selectMaterialBySSCC(ssccList);
        // 构建哈希映射
        Map<String, StockVO> mapStockVO = new HashMap<>();
        for (StockVO item : stockVOS) {
            mapStockVO.put(item.getSsccNumber(), item);
        }
        // 进行映射和赋值
        for (IQCVO item : result) {
            StockVO vo = mapStockVO.get(item.getSsccnumber());
            if (vo != null) {
                item.setMaterialName(vo.getMaterialName());
                item.setMaterialNb(vo.getMaterialNb());
            }
        }
        return result;
    }

    @Override
    public List<IQCDTO> iqcDTOSToMaterial(List<IQCDTO> result) {
        List<String> ssccList = result.stream().map(IQCDTO::getSsccnumber).collect(Collectors.toList());
        List<StockVO> stockVOS = stockMapper.selectMaterialBySSCC(ssccList);
        // 构建哈希映射
        Map<String, StockVO> mapStockVO = new HashMap<>();
        for (StockVO item : stockVOS) {
            mapStockVO.put(item.getSsccNumber(), item);
        }
        // 进行映射和赋值
        for (IQCDTO item : result) {
            StockVO vo = mapStockVO.get(item.getSsccnumber());
            if (vo != null) {
                item.setMaterialName(vo.getMaterialName());
                item.setMaterialNb(vo.getMaterialNb());
            }
        }
        return result;
    }

    @Override
    public List<StockVO> selectStockVOBySortType(StockQueryDTO stockQuerySTO) {
        List<StockVO> stockVOList = stockMapper.selectStockVOBySortType(stockQuerySTO);
        return stockVOList;
    }

    @Override
    public Double countAvailableStock(StockQueryDTO stockQueryDTO) {
        if (Objects.isNull(stockQueryDTO) || CollectionUtils.isEmpty(stockQueryDTO.getIds())) {
            throw new ServiceException("ids不能为空");
        }
        List<Long> ids = stockQueryDTO.getIds();
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(Stock::getId, ids);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> stockList = stockMapper.selectList(stockLambdaQueryWrapper);
        List<Stock> collect =
                stockList.stream().filter(item -> item.getFreezeStock() == 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() != stockList.size()) {
            throw new ServiceException("库存状态已过期，请刷新页面");
        }

        return stockList.stream().mapToDouble(Stock::getAvailableStock).sum();

    }

    @Override
    public StockVO getLastOneBySSCC(String ssccs) {

        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, ssccs);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        stockLambdaQueryWrapper.orderByDesc(BaseEntity::getUpdateTime);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        StockVO conver = BeanConverUtil.conver(stock, StockVO.class);
        return conver;
    }

    @Override
    public Stock getRecentOneBySSCC(String sscc) {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, sscc);
        stockLambdaQueryWrapper.orderByDesc(Stock::getUpdateTime);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        return stock;
    }

    @Override
    public Stock getAvailablesStockBySscc(String sscc) {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, sscc);
        stockLambdaQueryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        return stock;
    }

    @Override
    public Stock getOneStock(String sscc) {
        LambdaQueryWrapper<Stock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.eq(Stock::getSsccNumber, sscc);
        stockLambdaQueryWrapper.last("limit 1");
        Stock stock = stockMapper.selectOne(stockLambdaQueryWrapper);
        return stock;
    }

    @Override
    public List<StockVO> getBinStockLog(String binCode) {
        return stockMapper.getBinStockLog(binCode);
    }

    @Override
    public Double getMainStockCount(String materialNb) {
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stock::getMaterialNb, materialNb)
//                .eq(Stock::getFreezeStock, Double.valueOf(0))
                .eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode())
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Double.valueOf(0);
        }
        double sum = list.stream().filter(item -> AreaListConstants.mainArea(item.getAreaCode()) && !AreaListConstants.noQualifiedArea(item.getAreaCode())).mapToDouble(Stock::getAvailableStock).sum();
        return sum;
    }

    @Override
    public Double getNoAvailableStockCount(String materialNb) {
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stock::getMaterialNb, materialNb)
                .ne(Stock::getFreezeStock, Double.valueOf(0))
                .ne(Stock::getAvailableStock, Double.valueOf(0))
                .eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode())
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Double.valueOf(0);
        }
        double sum = list.stream().filter(item -> AreaListConstants.mainArea(item.getAreaCode()) && !AreaListConstants.mainArea(item.getAreaCode())).mapToDouble(Stock::getAvailableStock).sum();
        return sum;
    }

    @Override
    public Double getOutStockCount(String materialNb) {
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stock::getMaterialNb, materialNb)
                .eq(Stock::getFreezeStock, Double.valueOf(0))
                .eq(Stock::getQualityStatus, QualityStatusEnums.USE.getCode())
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> list = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Double.valueOf(0);
        }
        double sum = list.stream().filter(item -> !AreaListConstants.mainArea(item.getAreaCode()) && !AreaListConstants.noQualifiedArea(item.getAreaCode())).mapToDouble(Stock::getAvailableStock).sum();

        return sum;
    }

    @Override
    public void initStock(List<InitStockDTO> list) {
        List<BinIn> binIns = new ArrayList<>();

        list.stream().forEach(item -> {
            BinIn binIn = new BinIn();
            binIn.setSsccNumber(item.getSsccNumber());
            binIn.setQuantity(Double.valueOf(item.getRemainingQty()));
            binIn.setMaterialNb(item.getSapMaterialCode());
            binIn.setBatchNb(item.getSapBatchNumber());
//            binIn.setPalletType(materialVO.getPalletType());
            binIn.setAreaCode(item.getSapStorageLocation());
            binIn.setActualBinCode(item.getBin());

            binIn.setStatus(BinInStatusEnum.FINISH.value());
            binIn.setExpireDate(DateUtils.parseDate(item.getDluo()));

            binIn.setWareCode(getAreaVo(item.getSapStorageLocation()).getWareCode());

            binIn.setMoveType(MoveTypeEnums.BININ.getCode());
            binIn.setFromPurchaseOrder(item.getPoNumber());
            binIn.setPlantNb(item.getPlantNb());
            binIn.setQualityStatus(item.getR3StockStatus());
            binIns.add(binIn);
        });

        binInService.saveBatch(binIns);

        //库存
        List<Stock> stockList = new ArrayList<>();
        binIns.stream().forEach(binIn -> {
            Stock stock = new Stock();
            stock.setPlantNb(binIn.getPlantNb());
            stock.setWareCode(binIn.getWareCode());
            stock.setSsccNumber(binIn.getSsccNumber());
            stock.setWareCode(binIn.getWareCode());
            stock.setBinCode(binIn.getActualBinCode());
            stock.setFrameCode(binIn.getActualFrameCode());
            stock.setMaterialNb(binIn.getMaterialNb());
            stock.setBatchNb(binIn.getBatchNb());
            stock.setExpireDate(binIn.getExpireDate());
            stock.setTotalStock(binIn.getQuantity());
            stock.setFreezeStock(Double.valueOf(0));
            stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
            stock.setBinInId(binIn.getId());

            stock.setCreateBy(SecurityUtils.getUsername());
            stock.setCreateTime(new Date());
            stock.setQualityStatus(binIn.getQualityStatus());
            stock.setFromPurchaseOrder(binIn.getFromPurchaseOrder());
            stock.setAreaCode(binIn.getAreaCode());
            stock.setPalletCode(binIn.getPalletCode());
            stockList.add(stock);
        });
        this.saveBatch(stockList);

        System.out.println(list.size());
        System.out.println(stockList.size());
        System.out.println(binIns.size());

        areaMap.clear();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStock(StockEditDTO stockEditDTO) {
//        if (stockEditDTO.getSsccNumber() == null || stockEditDTO.getAvailableStock() == null || stockEditDTO.getFreezeStock() == null || stockEditDTO.getTotalStock() == null) {
//            throw new ServiceException("所有参数都不能为空");
//        }
//        if (!stockEditDTO.getTotalStock().equals(stockEditDTO.getFreezeStock()+stockEditDTO.getAvailableStock())){
//            throw new ServiceException("总库存必须等于冻结库存+可用库存");
//        }
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stock::getSsccNumber, stockEditDTO.getSsccNumber());
        queryWrapper.eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        Stock stock = this.getOne(queryWrapper);
        if (stock == null) {
            throw new ServiceException("无此库存信息");
        }
        StockAdjust stockAdjust = BeanConverUtil.conver(stock, StockAdjust.class);

        if (stockEditDTO.getType() == 0) {//领料

            if (stockEditDTO.getStockUse() > stock.getAvailableStock()) {
                throw new ServiceException("领用数量不能大于可用数量");
            }
            stock.setAvailableStock(stock.getAvailableStock() - stockEditDTO.getStockUse());
            stock.setTotalStock(stock.getTotalStock() - stockEditDTO.getStockUse());
            stock.setFreezeStock(stock.getTotalStock() - stock.getAvailableStock());
            if (stock.getAvailableStock() == Double.valueOf(0)) {
                binInService.binDown(stock.getSsccNumber());
            }else {
                this.updateById(stock);
            }


        } else if (stockEditDTO.getType() == 1) {//报废
            if (stock.getFreezeStock() > 0) {
                throw new ServiceException("该库存存在执行任务，暂时不可以报废");
            }
            binInService.binDown(stock.getSsccNumber());

        } else if (stockEditDTO.getType() == 3) {//配送到产线（玻璃瓶）//
            if (!"10310969".equals(stock.getMaterialNb())) {
                throw new ServiceException("配送到产线仅限玻璃瓶！10310969");
            }
            binInService.binDown(stock.getSsccNumber());

        } else {
            if (stockEditDTO.getSsccNumber() == null || stockEditDTO.getAvailableStock() == null || stockEditDTO.getFreezeStock() == null || stockEditDTO.getTotalStock() == null) {
                throw new ServiceException("所有参数都不能为空");
            }
            if (!stockEditDTO.getTotalStock().equals(stockEditDTO.getFreezeStock() + stockEditDTO.getAvailableStock())) {
                throw new ServiceException("总库存必须等于冻结库存+可用库存");
            }
            if (stockEditDTO.getTotalStock() == Double.valueOf(0)) {
                binInService.binDown(stock.getSsccNumber());
            } else {
                stock.setAvailableStock(stockEditDTO.getAvailableStock());
                stock.setFreezeStock(stockEditDTO.getFreezeStock());
                stock.setTotalStock(stockEditDTO.getTotalStock());
                this.updateById(stock);
            }
        }


        stockAdjust.setType(stockEditDTO.getType());
        stockAdjust.setId(null);
        stockAdjust.setAdjustFreezeStock(stock.getFreezeStock());
        stockAdjust.setAdjustTotalStock(stock.getTotalStock());
        stockAdjust.setAdjustAvailableStock(stock.getAvailableStock());

        stockAdjustService.save(stockAdjust);


    }


    private AreaVO getAreaVo(String areaCode) {
        if (areaMap.containsKey(areaCode)) {
            return areaMap.get(areaCode);
        } else {
            R<AreaVO> byCode = remoteMasterDataService.getByCode(areaCode);
            if (byCode == null || !byCode.isSuccess()) {
                throw new ServiceException("获取区域详情失败");
            }
            AreaVO areaVO = byCode.getData();
            if (areaVO == null) {
                throw new ServiceException("不存在编码为:" + areaCode + "的区域");
            }
            areaMap.put(areaCode, areaVO);
            return areaVO;
        }
    }
}
