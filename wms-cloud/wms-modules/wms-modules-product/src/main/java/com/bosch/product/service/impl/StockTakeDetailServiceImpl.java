package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.Stock;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.StockTakeDetail;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.api.domain.dto.PdaTakeOperateDTO;
import com.bosch.product.api.domain.dto.StockTakeDetailQueryDTO;
import com.bosch.product.api.domain.vo.StockTakeDetailVO;
import com.bosch.product.api.domain.vo.StockTakeTaskVO;
import com.bosch.product.api.enumeration.StockTakePlanDetailStatusEnum;
import com.bosch.product.api.enumeration.StockTakePlanStatusEnum;
import com.bosch.product.mapper.MaterialStockMapper;
import com.bosch.product.service.IMaterialStockService;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IStockTakeDetailService;
import com.bosch.product.mapper.StockTakeDetailMapper;
import com.bosch.product.service.IStockTakePlanService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author GUZ1CGD4
 * @description 针对表【stock_take_detail】的数据库操作Service实现
 * @createDate 2023-04-14 13:40:25
 */
@Service
public class StockTakeDetailServiceImpl extends ServiceImpl<StockTakeDetailMapper, StockTakeDetail>
        implements IStockTakeDetailService {

    @Autowired
    private StockTakeDetailMapper detailMapper;

    @Autowired
    @Lazy
    private IStockTakePlanService planService;

    @Autowired
    private IMaterialStockService materialStockService;

    @Autowired
    private RemoteMaterialService materialService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private MaterialStockMapper materialStockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(StockTakeDetailQueryDTO dto) {
        if (dto == null) {
            dto = new StockTakeDetailQueryDTO();
        }
        if (dto.getIssueType() == 0) {
            dto.setStatus(StockTakePlanDetailStatusEnum.WAIT_ISSUE.getCode());
            issueNormal(dto);
        } else if (dto.getIssueType() == 1) {
            issueCircle(dto);
        }
    }

    @Override
    public List<StockTakeTaskVO> getTaskList(StockTakeDetailQueryDTO dto) {
        return detailMapper.getTaskList(dto);
    }

    @Override
    public List<StockTakeDetailVO> getDetailList(StockTakeDetailQueryDTO queryDTO) {
        return detailMapper.getDetailList(queryDTO);
    }

    @Override
    public StockTakeDetailVO getByBarCode(String sscc) {
        if (StringUtils.isEmpty(sscc)) {
            throw new ServiceException("sscc不能为空");
        }
        StockTakeDetailQueryDTO queryDTO = new StockTakeDetailQueryDTO();
        queryDTO.setSsccNb(sscc);
        queryDTO.setStatus(StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
        List<StockTakeDetailVO> detailList = this.getDetailList(queryDTO);
        if (CollectionUtils.isEmpty(detailList)) {
            throw new ServiceException("没有该sscc:" + sscc + " 对应的待盘点任务");
        }
        StockTakeDetailVO stockTakeDetailVO = detailList.get(0);
        if (stockTakeDetailVO.getType() == 1) {//盲盘把数量设置为null
            stockTakeDetailVO.setStockQuantity(null);
        }
        return stockTakeDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operate(PdaTakeOperateDTO pdaTakeOperateDTO) {
        StockTakeDetail takeDetail = this.getById(pdaTakeOperateDTO.getDetailId());
        StockTakePlan takePlan = planService.getByPlanCode(takeDetail.getPlanCode());
        if (takePlan.getType() == 1) {//盲盘
            takeDetail.setTakeQuantity(pdaTakeOperateDTO.getPdaTakeQuantity());
            if (takeDetail.getStockQuantity().equals(pdaTakeOperateDTO.getPdaTakeQuantity())) {
                takeDetail.setIsDiff(0);
            } else {
                takeDetail.setIsDiff(1);
                takePlan.setRemark(pdaTakeOperateDTO.getRemark());
                takePlan.setDiffBinQuantity(takePlan.getDiffBinQuantity() + 1);
            }
        } else {//明盘
            if (pdaTakeOperateDTO.getIsDiff().equals(Boolean.FALSE)) {
                takeDetail.setTakeQuantity(pdaTakeOperateDTO.getPdaTakeQuantity());
                takeDetail.setIsDiff(1);
                takePlan.setRemark(pdaTakeOperateDTO.getRemark());
                takePlan.setDiffBinQuantity(takePlan.getDiffBinQuantity() + 1);
            } else {
                takeDetail.setIsDiff(0);
            }
        }
        takePlan.setTakeBinQuantity(takePlan.getTakeBinQuantity() + 1);
        takePlan.setRemark(pdaTakeOperateDTO.getRemark());
        takeDetail.setStatus(StockTakePlanDetailStatusEnum.WAIT_CONFIRM.getCode());
        takeDetail.setTakeBy(SecurityUtils.getUsername());
        takeDetail.setTakeTime(new Date());

        //判断所有plan下所有任务是否完成，如果完成，更新状态
//        LambdaQueryWrapper<StockTakeDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
//        detailQueryWrapper.eq(StockTakeDetail::getPlanCode, takeDetail.getPlanCode());
//        detailQueryWrapper.eq(StockTakeDetail::getStatus, StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
//        List<StockTakeDetail> list = this.list(detailQueryWrapper);
//        if (!CollectionUtils.isEmpty(list) && list.size() == 1) {
//            takePlan.setStatus(StockTakePlanStatusEnum.FINISH.getCode());
//        }

        this.updateById(takeDetail);
//        planService.updateById(takePlan);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(StockTakeDetailQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new StockTakeDetailQueryDTO();
        }
        queryDTO.setStatus(StockTakePlanDetailStatusEnum.WAIT_CONFIRM.getCode());
        List<StockTakeDetailVO> detailList = this.getDetailList(queryDTO);
        if (CollectionUtils.isEmpty(detailList)) {
            throw new ServiceException("该条件下没有待确认的数据");
        }
        detailList.stream().forEach(item -> item.setStatus(StockTakePlanDetailStatusEnum.FINISH.getCode()));
        //原材料
        List<StockTakeDetailVO> materialTakeDetails = detailList.stream().filter(item -> item.getTakeMaterialType() == 0).collect(Collectors.toList());
        //成品
        List<StockTakeDetailVO> productTakeDetails = detailList.stream().filter(item -> item.getTakeMaterialType() == 1).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(materialTakeDetails)) {
            updateMaterialStock(materialTakeDetails);
        }
        if (!CollectionUtils.isEmpty(productTakeDetails)) {
            updateProductStock(productTakeDetails);
        }

        //更新detail为完成
        List<Integer> idList = detailList.stream().map(StockTakeDetailVO::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<StockTakeDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(StockTakeDetail::getId, idList);
        updateWrapper.eq(StockTakeDetail::getStatus, StockTakePlanDetailStatusEnum.WAIT_CONFIRM.getCode());
        updateWrapper.set(StockTakeDetail::getStatus, StockTakePlanDetailStatusEnum.FINISH.getCode());
        this.update(updateWrapper);

        //如果所在的detial全部完成了，更新plan
        List<String> planCodes = detailList.stream().map(StockTakeDetail::getPlanCode).collect(Collectors.toList());
        LambdaQueryWrapper<StockTakeDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.in(StockTakeDetail::getPlanCode, planCodes);
        List<StockTakeDetail> stockTakeDetails = this.list(detailQueryWrapper);
        Map<String, List<StockTakeDetail>> planCodeMap = stockTakeDetails.stream().collect(Collectors.groupingBy(StockTakeDetail::getPlanCode));
        List<String> completePlanCodsList = new ArrayList<>();
        planCodeMap.forEach((key, value) -> {
            List<StockTakeDetail> tempDetails = value.stream().filter(item -> !item.getStatus().equals(StockTakePlanDetailStatusEnum.FINISH.getCode())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(tempDetails)) {
                completePlanCodsList.add(key);
            }
        });
        if (!CollectionUtils.isEmpty(completePlanCodsList)) {
            LambdaUpdateWrapper<StockTakePlan> planUpdateWrapper = new LambdaUpdateWrapper<>();
            planUpdateWrapper.in(StockTakePlan::getCode, completePlanCodsList);
            planUpdateWrapper.set(StockTakePlan::getStatus, StockTakePlanStatusEnum.FINISH.getCode());
            planService.update(planUpdateWrapper);
        }
    }

    @Override
    public void editTakeQuantity(PdaTakeOperateDTO operateDTO) {
        StockTakeDetail takeDetail = this.getById(operateDTO.getDetailId());
        if (Objects.isNull(takeDetail)) {
            throw new ServiceException("该条数据不存在");
        }
        if (!takeDetail.getStatus().equals(StockTakePlanDetailStatusEnum.WAIT_CONFIRM.getCode())) {
            throw new ServiceException("该条数据状态为" + StockTakePlanDetailStatusEnum.getDescByCode(takeDetail.getStatus()) + "，不可以修改");
        }
        takeDetail.setTakeQuantity(operateDTO.getPdaTakeQuantity());
        takeDetail.setEditBy(SecurityUtils.getUsername());
        takeDetail.setEditTime(new Date());
        this.updateById(takeDetail);
    }

    private void updateMaterialStock(List<StockTakeDetailVO> materialTakeDetails) {

        Map<String, StockTakeDetailVO> detailVOMap = materialTakeDetails.stream().collect(Collectors.toMap(StockTakeDetailVO::getSsccNb, Function.identity()));

        List<String> ssccList = materialTakeDetails.stream().map(StockTakeDetailVO::getSsccNb).collect(Collectors.toList());
        LambdaQueryWrapper<Stock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Stock::getSsccNumber, ssccList)
                .eq(Stock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<Stock> materialStockList = materialStockService.list(queryWrapper);
        materialStockList.stream().forEach(item -> {
            StockTakeDetailVO stockTakeDetailVO = detailVOMap.get(item.getSsccNumber());
            if (stockTakeDetailVO != null) {
                item.setTotalStock(stockTakeDetailVO.getTakeQuantity());
                item.setAvailableStock(item.getTotalStock() - item.getFreezeStock());
            }
        });
        materialStockService.updateBatchById(materialStockList);
    }

    private void updateProductStock(List<StockTakeDetailVO> materialTakeDetails) {

        Map<String, StockTakeDetailVO> detailVOMap = materialTakeDetails.stream().collect(Collectors.toMap(StockTakeDetailVO::getSsccNb, Function.identity()));

        List<String> ssccList = materialTakeDetails.stream().map(StockTakeDetailVO::getSsccNb).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductStock::getSsccNumber, ssccList)
                .eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> productStockList = productStockService.list(queryWrapper);
        productStockList.stream().forEach(item -> {
            StockTakeDetailVO stockTakeDetailVO = detailVOMap.get(item.getSsccNumber());
            if (stockTakeDetailVO != null) {
                item.setTotalStock(stockTakeDetailVO.getTakeQuantity());
                item.setAvailableStock(item.getTotalStock() - item.getFreezeStock());
            }
        });
        productStockService.updateBatchById(productStockList);
    }

    private void issueCircle(StockTakeDetailQueryDTO dto) {
        if (StringUtils.isEmpty(dto.getPlanCode()) || dto.getCircleTakeMonth() == null) {
            throw new ServiceException("下发循环盘点，计划号和月份不可以为空");
        }

        String taskNo = "task_" + DateUtils.parseDateToStr("yyyyMMddHHmm", new Date());

        LambdaQueryWrapper<StockTakePlan> planQueryWrapper = new LambdaQueryWrapper<>();
        planQueryWrapper.eq(StockTakePlan::getCode, dto.getPlanCode());
        planQueryWrapper.eq(StockTakePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        planQueryWrapper.last("limit 1");
        StockTakePlan stockTakePlan = planService.getOne(planQueryWrapper);


        //根据cell获取物料
        List<MaterialVO> materialVOList = new ArrayList<>();
        if (stockTakePlan.getCell() != null) {
            R<List<MaterialVO>> materialListR = materialService.getByCell(stockTakePlan.getCell());
            if (materialListR == null || !materialListR.isSuccess()) {
                throw new ServiceException("根据cell获取物料失败");
            }
            materialVOList = materialListR.getData();
        }
        List<String> materialCodeList = materialVOList.stream().map(MaterialVO::getCode).collect(Collectors.toList());


        //详细列表
        LambdaQueryWrapper<StockTakeDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(StockTakeDetail::getPlanCode, dto.getPlanCode());
        List<StockTakeDetail> takeDetailList = this.list(detailQueryWrapper);
        List<String> codeList = takeDetailList.stream().map(StockTakeDetail::getMaterialCode).collect(Collectors.toList());

        detailQueryWrapper.clear();
        detailQueryWrapper.eq(StockTakeDetail::getPlanCode, dto.getPlanCode());
        detailQueryWrapper.eq(StockTakeDetail::getCircleTakeMonth, dto.getCircleTakeMonth());
        List<StockTakeDetail> takeDetailListByMonth = this.list(detailQueryWrapper);
        takeDetailListByMonth.forEach(item -> {
            item.setStatus(StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
            item.setTaskNo(taskNo);
            item.setIssueTime(new Date());
        });

        int diffCount = 0;
        //原材料
        if (dto.getTakeMaterialType() == 0) {
            LambdaQueryWrapper<Stock> materialStockQueryWrapper = new LambdaQueryWrapper<>();
            materialStockQueryWrapper.eq(StringUtils.isNotEmpty(stockTakePlan.getWareCode()), Stock::getWareCode, stockTakePlan.getWareCode())
                    .eq(StringUtils.isNotEmpty(stockTakePlan.getAreaCode()), Stock::getAreaCode, stockTakePlan.getAreaCode())
                    .in(!CollectionUtils.isEmpty(materialCodeList), Stock::getMaterialNb, materialCodeList);
            List<Stock> materialStockList = materialStockService.list(materialStockQueryWrapper);
            diffCount = dealDiffMaterial(materialStockList, codeList, dto.getPlanCode(), dto.getCircleTakeMonth(), taskNo);
        } else if (dto.getTakeMaterialType() == 1) {
            LambdaQueryWrapper<ProductStock> productStockQueryWrapper = new LambdaQueryWrapper<>();
            productStockQueryWrapper.eq(StringUtils.isNotEmpty(stockTakePlan.getWareCode()), ProductStock::getWareCode, stockTakePlan.getWareCode())
                    .eq(StringUtils.isNotEmpty(stockTakePlan.getAreaCode()), ProductStock::getAreaCode, stockTakePlan.getAreaCode())
                    .in(!CollectionUtils.isEmpty(materialCodeList), ProductStock::getMaterialNb, materialCodeList);
            List<ProductStock> productStockList = productStockService.list(productStockQueryWrapper);
            diffCount = dealDiffProduct(productStockList, codeList, dto.getPlanCode(), dto.getCircleTakeMonth(), taskNo);
        }

        if (CollectionUtils.isEmpty(takeDetailListByMonth) && diffCount == 0) {
            throw new ServiceException("无待下发的盘点明细");
        }


        int count = (int) takeDetailListByMonth.stream().map(StockTakeDetail::getMaterialCode).distinct().count();
        if (dto.getCircleTakeMonth() == stockTakePlan.getCircleTakeMonth()) {
            stockTakePlan.setFirstIssueMaterialQuantity(count + diffCount);
        } else if (dto.getCircleTakeMonth() - stockTakePlan.getCircleTakeMonth() == 1) {
            stockTakePlan.setSecondIssueMaterialQuantity(count + diffCount);
        } else if (dto.getCircleTakeMonth() - stockTakePlan.getCircleTakeMonth() == 2) {
            stockTakePlan.setThirdIssueMaterialQuantity(count + diffCount);
        }
        int firstIssueQuantity = stockTakePlan.getFirstIssueMaterialQuantity() == null ? 0 : stockTakePlan.getFirstIssueMaterialQuantity();
        int secondIssueQuantity = stockTakePlan.getSecondIssueMaterialQuantity() == null ? 0 : stockTakePlan.getSecondIssueMaterialQuantity();
        int thirdIssueQuantity = stockTakePlan.getThirdIssueMaterialQuantity() == null ? 0 : stockTakePlan.getThirdIssueMaterialQuantity();

        stockTakePlan.setTotalIssueQuantity(firstIssueQuantity + secondIssueQuantity + thirdIssueQuantity);
        planService.updateById(stockTakePlan);

        this.updateBatchById(takeDetailListByMonth);
    }

    private Integer dealDiffProduct(List<ProductStock> productStockList, List<String> codeList, String planCode, Integer month, String taskNo) {
        List<String> stockCodes = productStockList.stream().map(ProductStock::getMaterialNb).collect(Collectors.toList());

        List<String> diffCodes = new ArrayList<>(stockCodes);
        diffCodes.removeAll(codeList);
        List<StockTakeDetail> detailList = new ArrayList<>();

        //如果不为空的话，需要新增
        if (!CollectionUtils.isEmpty(diffCodes)) {
            List<ProductStock> stockList = productStockList.stream().filter(item -> diffCodes.contains(item.getMaterialNb())).collect(Collectors.toList());
            stockList.forEach(item -> {
                StockTakeDetail stockTakeDetail = new StockTakeDetail();
                stockTakeDetail.setPlanCode(planCode);
                stockTakeDetail.setMaterialCode(item.getMaterialNb());
                stockTakeDetail.setSsccNb(item.getSsccNumber());
                stockTakeDetail.setBatchNb(item.getBatchNb());
                stockTakeDetail.setWareCode(item.getWareCode());
                stockTakeDetail.setAreaCode(item.getAreaCode());
                stockTakeDetail.setBinCode(item.getBinCode());
                stockTakeDetail.setStockQuantity(item.getTotalStock());
                stockTakeDetail.setCircleTakeMonth(month);
                stockTakeDetail.setStatus(StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
                stockTakeDetail.setTaskNo(taskNo);
                stockTakeDetail.setIssueTime(new Date());
                detailList.add(stockTakeDetail);
            });
            this.saveBatch(detailList);
        }
        return (int) detailList.stream().map(StockTakeDetail::getMaterialCode).distinct().count();
    }

    private Integer dealDiffMaterial(List<Stock> materialStockList, List<String> codeList, String planCode, Integer month, String taskNo) {
        List<String> stockCodes = materialStockList.stream().map(Stock::getMaterialNb).collect(Collectors.toList());

        List<String> diffCodes = new ArrayList<>(stockCodes);
        diffCodes.removeAll(codeList);
        List<StockTakeDetail> detailList = new ArrayList<>();

        //如果不为空的话，需要新增
        if (!CollectionUtils.isEmpty(diffCodes)) {
            List<Stock> stockList = materialStockList.stream().filter(item -> diffCodes.contains(item.getMaterialNb())).collect(Collectors.toList());
            stockList.forEach(item -> {
                StockTakeDetail stockTakeDetail = new StockTakeDetail();
                stockTakeDetail.setPlanCode(planCode);
                stockTakeDetail.setMaterialCode(item.getMaterialNb());
                stockTakeDetail.setSsccNb(item.getSsccNumber());
                stockTakeDetail.setBatchNb(item.getBatchNb());
                stockTakeDetail.setWareCode(item.getWareCode());
                stockTakeDetail.setAreaCode(item.getAreaCode());
                stockTakeDetail.setBinCode(item.getBinCode());
                stockTakeDetail.setStatus(StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
                stockTakeDetail.setStockQuantity(item.getTotalStock());
                stockTakeDetail.setCircleTakeMonth(month);
                stockTakeDetail.setTaskNo(taskNo);
                stockTakeDetail.setIssueTime(new Date());
                detailList.add(stockTakeDetail);
            });
            this.saveBatch(detailList);
        }
        return (int) detailList.stream().map(StockTakeDetail::getMaterialCode).distinct().count();
    }


    private void issueNormal(StockTakeDetailQueryDTO dto) {
        List<StockTakeDetailVO> takeDetailVOList = detailMapper.getDetailList(dto);
        if (CollectionUtils.isEmpty(takeDetailVOList)) {
            throw new ServiceException("该条件下没有待下发的detail");
        }
        //任务号
        String taskNo = "task_" + DateUtils.parseDateToStr("yyyyMMddHHmm", new Date());
        takeDetailVOList.forEach(item -> {
            item.setStatus(StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
            item.setTaskNo(taskNo);
            item.setIssueTime(new Date());
        });
        List<String> planCodes = takeDetailVOList.stream().map(StockTakeDetail::getPlanCode).collect(Collectors.toList());
        Map<String, List<StockTakeDetailVO>> detailMap = takeDetailVOList.stream()
                .collect(Collectors.groupingBy(StockTakeDetailVO::getPlanCode));


        LambdaQueryWrapper<StockTakePlan> planQueryWrapper = new LambdaQueryWrapper<>();
        planQueryWrapper.in(StockTakePlan::getCode, planCodes);
        List<StockTakePlan> planList = planService.list(planQueryWrapper);
        planList.forEach(item -> {
            if (detailMap.containsKey(item.getCode())) {
                long count = detailMap.get(item.getCode()).stream().map(StockTakeDetail::getMaterialCode).distinct().count();
                item.setTotalIssueQuantity((int) count);
            }
            item.setStatus(StockTakePlanStatusEnum.PROCESSING.getCode());
            item.setTotalIssueQuantity(takeDetailVOList.size());
        });
        planService.updateBatchById(planList);

        List<StockTakeDetail> stockTakeDetailList = BeanConverUtil.converList(takeDetailVOList, StockTakeDetail.class);
        this.updateBatchById(stockTakeDetailList);
    }


}




