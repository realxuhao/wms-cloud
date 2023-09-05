package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.MaterialKanban;
import com.bosch.binin.api.domain.Stock;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.StockTakeDetail;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.api.domain.dto.StockTakeAddDTO;
import com.bosch.product.api.domain.dto.StockTakePlanDTO;
import com.bosch.product.api.domain.vo.StockTakePlanVO;
import com.bosch.product.api.enumeration.StockTakePlanDetailStatusEnum;
import com.bosch.product.api.enumeration.StockTakePlanStatusEnum;
import com.bosch.product.service.IMaterialStockService;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IStockTakeDetailService;
import com.bosch.product.service.IStockTakePlanService;
import com.bosch.product.mapper.StockTakePlanMapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author GUZ1CGD4
 * @description 针对表【stock_take_plan】的数据库操作Service实现
 * @createDate 2023-04-14 13:40:35
 */
@Service
public class StockTakePlanServiceImpl extends ServiceImpl<StockTakePlanMapper, StockTakePlan>
        implements IStockTakePlanService {

    @Autowired
    private IMaterialStockService materialStockService;

    @Autowired
    private RemoteMaterialService materialService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private IStockTakeDetailService stockTakeDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStockTakePlan(StockTakeAddDTO dto) {

        StockTakePlan stockTakePlan = new StockTakePlan();
        stockTakePlan.setCode(getNextPlanCode());
        stockTakePlan.setCell(dto.getCell());
        stockTakePlan.setWareCode(dto.getWareCode());
        stockTakePlan.setAreaCode(dto.getAreaCode());
        stockTakePlan.setType(dto.getType());
        stockTakePlan.setMethod(dto.getMethod());
        stockTakePlan.setStatus(StockTakePlanStatusEnum.CREATED.getCode());
        stockTakePlan.setTakeMaterialType(dto.getTakeMaterialType());
        stockTakePlan.setCircleTakeMonth(dto.getCircleTakeMonth());


        List<StockTakeDetail> stockTakeDetailList = new ArrayList<>();
        //原材料
        if (dto.getTakeMaterialType() == 0) {
            //根据cell获取原材料物料编码
            List<MaterialVO> materialVOList = new ArrayList<>();
            if (dto.getCell() != null) {
                R<List<MaterialVO>> materialListR = materialService.getByCell(dto.getCell());
                if (materialListR == null || !materialListR.isSuccess()) {
                    throw new ServiceException("根据cell获取物料失败");
                }
                materialVOList = materialListR.getData();
            }

            List<String> materialCodeList = materialVOList.stream().map(MaterialVO::getCode).collect(Collectors.toList());

            LambdaQueryWrapper<Stock> materialStockQueryWrapper = new LambdaQueryWrapper<>();
            materialStockQueryWrapper.eq(StringUtils.isNotEmpty(dto.getWareCode()), Stock::getWareCode, dto.getWareCode())
                    .eq(StringUtils.isNotEmpty(dto.getAreaCode()), Stock::getAreaCode, dto.getAreaCode())
                    .eq(Stock::getFreezeStock, (double) 0)
                    .in(!CollectionUtils.isEmpty(materialCodeList), Stock::getMaterialNb, materialCodeList);

            List<Stock> materialStockList = materialStockService.list(materialStockQueryWrapper);
            if (dto.getMethod() == 1) {
                long count = materialStockList.stream().map(Stock::getMaterialNb).distinct().count();
                if (count < 3) {
                    throw new ServiceException("物料种数<3，不可创建循环盘点");
                }
            }
            buildTakeDetailListByMaterials(stockTakeDetailList, materialStockList, stockTakePlan.getCode());
        } else if (dto.getTakeMaterialType() == 1) {
            //根据cell获取成品物料编码
            List<MdProductPackagingVO> productPackagingVOS = new ArrayList<>();
            if (dto.getCell() != null) {
                R<List<MdProductPackagingVO>> productServiceByCellR = productService.getByCell(dto.getCell());
                if (productServiceByCellR == null || !productServiceByCellR.isSuccess()) {
                    throw new ServiceException("根据cell获取物料失败");
                }
                productPackagingVOS = productServiceByCellR.getData();
            }

            List<String> materialCodeList = productPackagingVOS.stream().map(MdProductPackagingVO::getProductNo).collect(Collectors.toList());

            LambdaQueryWrapper<ProductStock> productStockQueryWrapper = new LambdaQueryWrapper<>();
            productStockQueryWrapper.eq(StringUtils.isNotEmpty(dto.getWareCode()), ProductStock::getWareCode, dto.getWareCode())
                    .eq(StringUtils.isNotEmpty(dto.getAreaCode()), ProductStock::getAreaCode, dto.getAreaCode())
//                    .eq(ProductStock::getFreezeStock, (double) 0)
                    .eq(ProductStock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode())
                    .in(!CollectionUtils.isEmpty(materialCodeList), ProductStock::getMaterialNb, materialCodeList);

            List<ProductStock> productStockList = productStockService.list(productStockQueryWrapper);
            if (dto.getMethod() == 1) {
                long count = productStockList.stream().map(ProductStock::getMaterialNb).distinct().count();
                if (count < 3) {
                    throw new ServiceException("物料种数<3，不可创建循环盘点");
                }
            }
            buildTakeDetailListByProducts(stockTakeDetailList, productStockList, stockTakePlan.getCode());
        }

        if (CollectionUtils.isEmpty(stockTakeDetailList)) {
            throw new ServiceException("该条件下没有可以盘点的库存。");
        }

        //如果是循环盘点,设置循环盘点月份
        if (dto.getMethod() == 1) {
            setCircleMonth(stockTakeDetailList, dto.getCircleTakeMonth());
        }

        //plan设置物料类别数量和库位数量
        long count = stockTakeDetailList.stream().map(StockTakeDetail::getMaterialCode).distinct().count();

        stockTakePlan.setCreatedMaterialQuantity((int) count);
        stockTakePlan.setCreatedBinQuantity(stockTakeDetailList.size());

        this.save(stockTakePlan);

        stockTakeDetailService.saveBatch(stockTakeDetailList);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        StockTakePlan stockTakePlan = this.getById(id);
        if (stockTakePlan == null) {
            throw new ServiceException("该条数据不存在！");
        }
        if (!stockTakePlan.getStatus().equals(StockTakePlanStatusEnum.CREATED.getCode())) {
            throw new ServiceException("该计划当前状态为：" + StockTakePlanStatusEnum.getDescByCode(stockTakePlan.getStatus()) + ",不可以删除");
        }
        this.removeById(id);

        LambdaQueryWrapper<StockTakeDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(StockTakeDetail::getPlanCode, stockTakePlan.getCode());
        stockTakeDetailService.remove(detailQueryWrapper);

    }

    @Override
    public StockTakePlan getByPlanCode(String planCode) {
        LambdaQueryWrapper<StockTakePlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StockTakePlan::getCode, planCode)
                .eq(StockTakePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode())
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<StockTakePlan> list(StockTakePlanDTO dto) {
        //判断dto每个字段，不为空和null则加入查询条件，做模糊查询
        LambdaQueryWrapper<StockTakePlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dto.getCode()), StockTakePlan::getCode, dto.getCode());
        queryWrapper.like(StringUtils.isNotEmpty(dto.getCell()), StockTakePlan::getCell, dto.getCell());
        queryWrapper.like(StringUtils.isNotEmpty(dto.getWareCode()), StockTakePlan::getWareCode, dto.getWareCode());
        queryWrapper.like(StringUtils.isNotEmpty(dto.getAreaCode()), StockTakePlan::getAreaCode, dto.getAreaCode());
        queryWrapper.eq(dto.getStatus() != null, StockTakePlan::getStatus, dto.getStatus());
        queryWrapper.eq(dto.getType() != null, StockTakePlan::getType, dto.getType());
        queryWrapper.eq(dto.getMethod() != null, StockTakePlan::getMethod, dto.getMethod());
        queryWrapper.eq(dto.getTakeMaterialType() != null, StockTakePlan::getTakeMaterialType, dto.getTakeMaterialType());
        queryWrapper.eq(dto.getCircleTakeMonth() != null, StockTakePlan::getCircleTakeMonth, dto.getCircleTakeMonth());
        queryWrapper.eq(dto.getCreatedMaterialQuantity() != null, StockTakePlan::getCreatedMaterialQuantity, dto.getCreatedMaterialQuantity());
        queryWrapper.like(StringUtils.isNotEmpty(dto.getCreateBy()), StockTakePlan::getCreateBy, dto.getCreateBy());
        queryWrapper.ge(dto.getCreateTimeStart() != null, StockTakePlan::getCreateTime,
                dto.getCreateTimeStart());
        queryWrapper.le(dto.getCreateTimeEnd() != null, StockTakePlan::getCreateTime, dto.getCreateTimeEnd());

        queryWrapper.eq(StockTakePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<StockTakePlan> stockTakePlanList = this.list(queryWrapper);

        return stockTakePlanList;
    }

    private void setCircleMonth(List<StockTakeDetail> stockTakeDetailList, Integer circleTakeMonth) {

        List<String> materialTypeList = stockTakeDetailList.stream().map(StockTakeDetail::getMaterialCode).collect(Collectors.toList());
        int partitionSize = materialTypeList.size() / 3;
        List<List<String>> partitions = IntStream.range(0, 3)
                .mapToObj(i -> materialTypeList.subList(i * partitionSize, i == 2 ? materialTypeList.size() : (i + 1) * partitionSize))
                .collect(Collectors.toList());


        stockTakeDetailList.stream().forEach(item -> {
            if (partitions.get(0).contains(item.getMaterialCode())) {
                item.setCircleTakeMonth(circleTakeMonth);
            } else if (partitions.get(1).contains(item.getMaterialCode())) {
                item.setCircleTakeMonth(getNextMonth(circleTakeMonth));
            } else if (partitions.get(2).contains(item.getMaterialCode())) {
                item.setCircleTakeMonth(getNextMonth(getNextMonth(circleTakeMonth)));
            }
        });

    }

    private int getNextMonth(int currentMonth) {
        // 如果当前月份不在1-12范围内，返回0表示无效月份
        if (currentMonth < 1 || currentMonth > 12) {
            return 0;
        }

        // 计算下一个月份
        int nextMonth = currentMonth + 1;
        if (nextMonth > 12) {
            nextMonth = 1;
        }

        return nextMonth;
    }


    private List<StockTakeDetail> buildTakeDetailListByProducts(List<StockTakeDetail> stockTakeDetailList, List<ProductStock> productStockList, String planCode) {
        productStockList.forEach(item -> {
            StockTakeDetail stockTakeDetail = new StockTakeDetail();
            stockTakeDetail.setPlanCode(planCode);
            stockTakeDetail.setMaterialCode(item.getMaterialNb());
            stockTakeDetail.setSsccNb(item.getSsccNumber());
            stockTakeDetail.setBatchNb(item.getBatchNb());
            stockTakeDetail.setWareCode(item.getWareCode());
            stockTakeDetail.setAreaCode(item.getAreaCode());
            stockTakeDetail.setBinCode(item.getBinCode());
            stockTakeDetail.setStatus(StockTakePlanDetailStatusEnum.WAIT_ISSUE.getCode());
            stockTakeDetail.setStockQuantity(item.getTotalStock());
            stockTakeDetailList.add(stockTakeDetail);
        });
        return stockTakeDetailList;
    }

    private List<StockTakeDetail> buildTakeDetailListByMaterials(List<StockTakeDetail> stockTakeDetailList, List<Stock> materialStockList, String planCode) {

        materialStockList.forEach(item -> {
            StockTakeDetail stockTakeDetail = new StockTakeDetail();
            stockTakeDetail.setPlanCode(planCode);
            stockTakeDetail.setMaterialCode(item.getMaterialNb());
            stockTakeDetail.setSsccNb(item.getSsccNumber());
            stockTakeDetail.setBatchNb(item.getBatchNb());
            stockTakeDetail.setWareCode(item.getWareCode());
            stockTakeDetail.setAreaCode(item.getAreaCode());
            stockTakeDetail.setBinCode(item.getBinCode());
            stockTakeDetail.setStatus(StockTakePlanDetailStatusEnum.WAIT_ISSUE.getCode());
            stockTakeDetail.setStockQuantity(item.getTotalStock());
            stockTakeDetailList.add(stockTakeDetail);
        });
        return stockTakeDetailList;

    }


    private String getNextPlanCode() {
        LambdaQueryWrapper<StockTakePlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StockTakePlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.orderByDesc(StockTakePlan::getCode);
        queryWrapper.last("limit 1");
        StockTakePlan stockTakePlan = this.getOne(queryWrapper);
        String currentDay = DateUtils.parseDateToStr("yyyyMMdd", new Date());

        if (!Objects.isNull(stockTakePlan)) {
            String code = stockTakePlan.getCode();
            if (code.startsWith(currentDay)) {
                return String.valueOf(Long.parseLong(code) + 1);
            }
        }
        return currentDay + "001";
    }
}




