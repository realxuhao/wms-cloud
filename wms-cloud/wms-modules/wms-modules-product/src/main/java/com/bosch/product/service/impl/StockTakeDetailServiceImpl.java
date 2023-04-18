package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
                takePlan.setDiffBinQuantity(takePlan.getDiffBinQuantity() + 1);
            }
        } else {//明盘
            if (pdaTakeOperateDTO.getIsDiff().equals(Boolean.FALSE)) {
                takeDetail.setTakeQuantity(pdaTakeOperateDTO.getPdaTakeQuantity());
                takeDetail.setIsDiff(1);
                takePlan.setDiffBinQuantity(takePlan.getDiffBinQuantity() + 1);
            } else {
                takeDetail.setIsDiff(0);
            }
        }
        takePlan.setTakeBinQuantity(takePlan.getTakeBinQuantity() + 1);
        takeDetail.setStatus(StockTakePlanDetailStatusEnum.FINISH.getCode());

        //判断所有plan下所有任务是否完成，如果完成，更新状态
        LambdaQueryWrapper<StockTakeDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(StockTakeDetail::getPlanCode, takeDetail.getPlanCode());
        detailQueryWrapper.eq(StockTakeDetail::getStatus, StockTakePlanDetailStatusEnum.WAIT_TAKE.getCode());
        List<StockTakeDetail> list = this.list(detailQueryWrapper);
        if (!CollectionUtils.isEmpty(list) && list.size() == 1) {
            takePlan.setStatus(StockTakePlanStatusEnum.FINISH.getCode());
        }

        this.updateById(takeDetail);
        planService.updateById(takePlan);


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
        stockTakePlan.setTotalIssueQuantity(stockTakePlan.getFirstIssueMaterialQuantity() + stockTakePlan.getSecondIssueMaterialQuantity() + stockTakePlan.getThirdIssueMaterialQuantity());
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
            throw new ServiceException("查询detail为空。");
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




