package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.enumeration.SPDNStatusEnum;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.product.api.domain.*;
import com.bosch.product.api.domain.dto.SPDNDTO;
import com.bosch.product.api.domain.enumeration.ProductSPDNPickEnum;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.product.api.domain.vo.SPDNCount;
import com.bosch.product.api.domain.vo.SPDNVO;
import com.bosch.product.mapper.SPDNMapper;
import com.bosch.product.mapper.ShippingTaskMapper;
import com.bosch.product.service.*;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.StockOperationType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IProductStockOperationService;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.hibernate.validator.internal.util.privilegedactions.NewSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:55
 **/
@Service
public class SPDNServiceImpl extends ServiceImpl<SPDNMapper, SPDN>
        implements ISPDNService {


    @Autowired
    private SPDNMapper spdnMapper;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private IProductSPDNPickService spdnPickService;

    @Autowired
    private RemoteBinInService remoteBinInService;


    @Autowired
    private IProductStockOperationService productStockOperationService;

    @Resource
    @Lazy
    private IProductWareShiftService wareShiftService;

    @Autowired
    private IUserOperationLogService userOperationLogService;


    @Resource
    private RemoteProductService remoteProductService;


    @Override
    public List<SPDNVO> getList(SPDNDTO spdndto) {
        return spdnMapper.getList(spdndto);
    }

    @Override
    public SPDNCount getSPDNCount(SPDNDTO spdndto) {
        return spdnMapper.getSPDNCount(spdndto);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceException("必须选中一条数据才能操作");
        }
        LambdaUpdateWrapper<SPDN> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SPDN::getId, ids);
        updateWrapper.set(SPDN::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
        updateWrapper.set(SPDN::getUpdateBy, SecurityUtils.getUsername());
        updateWrapper.set(SPDN::getUpdateTime, DateUtils.getNowDate());
        this.update(updateWrapper);
    }


    @Override
    public void approveBy7761(List<Long> idsList) {
        //审批需要校验1、库存是不是在7752。2、数量是否符合
        Assert.notEmpty(idsList, "需选中数据后再进行审批");
        List<SPDN> spdnList = this.listByIds(idsList);
        Map<String, Double> ssccQtyMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getQty));
        Map<String, String> ssccPlantMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getPlant));
        List<String> ssccList = spdnList.stream().map(SPDN::getSsccNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(ProductStock::getSsccNumber, ssccList);
        stockLambdaQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> stockList = productStockService.list(stockLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("库存数据为空");
        }
        Map<String, ProductStock> ssccStockMap = stockList.stream().collect(Collectors.toMap(ProductStock::getSsccNumber, Function.identity()));
        List<String> inValidPlantSSCCList = new ArrayList<>();
        List<String> inValidQtySSCCList = new ArrayList<>();
        stockList.stream().forEach(item -> {
            if (item.getFreezeStock() > 0) {
                throw new ServiceException("该托" + item.getSsccNumber() + "存在未完成的移库或捡配任务!");
            }
            if (!item.getPlantNb().equals("7752")) {
                inValidPlantSSCCList.add(item.getSsccNumber());
            }
            if (!item.getTotalStock().equals(ssccQtyMap.get(item.getSsccNumber()))) {
                inValidQtySSCCList.add(item.getSsccNumber());
            }
            if (!item.getQualityStatus().equals(QualityStatusEnums.USE.getCode())) {
                throw new ServiceException(item.getSsccNumber() + "库存状态不为U的库存数据");
            }
        });

        if (!CollectionUtils.isEmpty(inValidPlantSSCCList) || !CollectionUtils.isEmpty(inValidQtySSCCList)) {
            throw new ServiceException("存在" + inValidPlantSSCCList.size() + "条库存不在7752的数据:" + inValidPlantSSCCList + "\n" + "存在" + inValidQtySSCCList.size() + "条数量不正确的数据:" + inValidQtySSCCList);
        }


        //7761的产品同时对应的SSCC质量状态由U变为Q，对应的SSCC流转到销售库存页面，同时生成SUQ质检管理页面（原有的成品库存不再存在）。
        //7701不生成SUQA质检，对应的SSCC生成移库任务（下架，装车（track））

        // 如果是7761。那么就把库存的plantNb改成7761,同时质量状态变为Q
        ArrayList<ProductSPDNPick> spdnPickList = new ArrayList<>();
        spdnList.stream().forEach(item -> {
            if (!item.getStatus().equals(SPDNStatusEnum.SHIPPED.code())) {
                throw new ServiceException("只能选择已发运的数据");
            }
            ProductStock productStock = ssccStockMap.get(item.getSsccNumber());
            if ("7761".equals(item.getPlant())) {
                if (productStock != null) {
                    productStock.setPlantNb(item.getPlant());
                    productStock.setWareCode("7761" + productStock.getWareCode().substring(4));
                    productStock.setAreaCode("7761-0001");
                    productStock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
                    productStock.setChangeStatus(0);

                }
            } else {//如果不是7761的，生成对应的发运装车任务
                throw new ServiceException("只能选择7761的数据");
//                if (productStock != null) {
//                    productStock.setPlantNb(item.getPlant());
//                    productStock.setFreezeStock(productStock.getTotalStock());
//                    productStock.setAvailableStock((double) 0);
//                    ProductSPDNPick spdnPick = BeanConverUtil.conver(productStock, ProductSPDNPick.class);
//                    spdnPick.setSpdnId(item.getId());
//                    spdnPick.setStatus(ProductSPDNPickEnum.WAITTING_DOWN.code());
//                    spdnPick.setId(null);
//                    spdnPickList.add(spdnPick);

//                }
            }

            item.setStatus(SPDNStatusEnum.APPROVED.code());
        });
//        stockList.stream().forEach(stock->{
////            if ()
//        });

        if (!CollectionUtils.isEmpty(spdnPickList)) {
            spdnPickService.saveBatch(spdnPickList);
        }
        ArrayList<ProductStock> list = new ArrayList<>(ssccStockMap.values());
        productStockService.updateBatchById(list);
        this.updateBatchById(spdnList);
    }

    @Override
    public void approveByNo7761(List<Long> idsList) {
        //审批需要校验1、库存是不是在7752。2、数量是否符合
        Assert.notEmpty(idsList, "需选中数据后再进行审批");
        List<SPDN> spdnList = this.listByIds(idsList);
        Map<String, Double> ssccQtyMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getQty));
        Map<String, String> ssccPlantMap = spdnList.stream().collect(Collectors.toMap(SPDN::getSsccNumber, SPDN::getPlant));
        List<String> ssccList = spdnList.stream().map(SPDN::getSsccNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> stockLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stockLambdaQueryWrapper.in(ProductStock::getSsccNumber, ssccList);
        stockLambdaQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> stockList = productStockService.list(stockLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("库存数据为空");
        }
        Map<String, ProductStock> ssccStockMap = stockList.stream().collect(Collectors.toMap(ProductStock::getSsccNumber, Function.identity()));
        List<String> inValidPlantSSCCList = new ArrayList<>();
        List<String> inValidQtySSCCList = new ArrayList<>();
        stockList.stream().forEach(item -> {
            if (item.getFreezeStock() > 0) {
                throw new ServiceException("该托" + item.getSsccNumber() + "存在未完成的移库或捡配任务!");
            }
            if (!item.getPlantNb().equals("7752")) {
                inValidPlantSSCCList.add(item.getSsccNumber());
            }
            if (!item.getTotalStock().equals(ssccQtyMap.get(item.getSsccNumber()))) {
                inValidQtySSCCList.add(item.getSsccNumber());
            }
            if (!item.getQualityStatus().equals(QualityStatusEnums.USE.getCode())) {
                throw new ServiceException(item.getSsccNumber() + "库存状态不为U的库存数据");
            }
        });

        if (!CollectionUtils.isEmpty(inValidPlantSSCCList) || !CollectionUtils.isEmpty(inValidQtySSCCList)) {
            throw new ServiceException("存在" + inValidPlantSSCCList.size() + "条库存不在7752的数据\n" + "存在" + inValidQtySSCCList.size() + "条数量不正确的数据");
        }


        //7761的产品同时对应的SSCC质量状态由U变为Q，对应的SSCC流转到销售库存页面，同时生成SUQ质检管理页面（原有的成品库存不再存在）。
        //7701不生成SUQA质检，对应的SSCC生成移库任务（下架，装车（track））

        // 如果是7761。那么就把库存的plantNb改成7761,同时质量状态变为Q
        ArrayList<ProductSPDNPick> spdnPickList = new ArrayList<>();
        spdnList.stream().forEach(item -> {
            ProductStock productStock = ssccStockMap.get(item.getSsccNumber());
            if ("7761".equals(item.getPlant())) {
//                if (productStock != null) {
//                    productStock.setPlantNb(item.getPlant());
//                    productStock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
//                    productStock.setChangeStatus(0);
//
//                }
                throw new ServiceException("只能选择非7761的数据");
            } else {//如果不是7761的，生成对应的发运装车任务
                if (!item.getStatus().equals(SPDNStatusEnum.WAITING_APPROVE.code())) {
                    throw new ServiceException("只能选择已上传状态的数据");
                }
                if (productStock != null) {
                    productStock.setPlantNb(item.getPlant());
                    productStock.setFreezeStock(productStock.getTotalStock());
                    productStock.setAvailableStock((double) 0);
                    ProductSPDNPick spdnPick = BeanConverUtil.conver(productStock, ProductSPDNPick.class);
                    spdnPick.setSpdnId(item.getId());
                    spdnPick.setStatus(ProductSPDNPickEnum.WAITTING_DOWN.code());
                    spdnPick.setId(null);
                    spdnPickList.add(spdnPick);

                }
            }

            item.setStatus(SPDNStatusEnum.APPROVED.code());
        });
//        stockList.stream().forEach(stock->{
////            if ()
//        });

        if (!CollectionUtils.isEmpty(spdnPickList)) {
            spdnPickService.saveBatch(spdnPickList);
        }
        ArrayList<ProductStock> list = new ArrayList<>(ssccStockMap.values());
        productStockService.updateBatchById(list);
        this.updateBatchById(spdnList);
    }

    @Override
    public void binDown(String qrCode) {
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = productStockService.getOne(queryWrapper);
        if (productStock == null) {
            throw new ServiceException("当前不存在该SSCC" + sscc + "对应的库存信息");
        }
        LambdaQueryWrapper<ProductSPDNPick> pickWrapper = new LambdaQueryWrapper<>();
        pickWrapper.eq(ProductSPDNPick::getSsccNumber, sscc);
        pickWrapper.eq(ProductSPDNPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        pickWrapper.eq(ProductSPDNPick::getStatus, ProductSPDNPickEnum.WAITTING_DOWN.code());
        ProductSPDNPick spdnPick = spdnPickService.getOne(pickWrapper);
        if (spdnPick == null) {
            throw new ServiceException("当前不存在该SSCC" + sscc + "对应的下架信息");
        }
        productStock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        productStockService.updateById(productStock);

        spdnPick.setStatus(ProductSPDNPickEnum.WAITTING_SHIP.code());
        spdnPickService.updateById(spdnPick);


        MdProductPackagingVO productVO = getProductVO(spdnPick.getMaterialNb());


//        String plantNb, Double operationStock,String ssccNumber,String materialNb,String batchNb, Integer operationType
//        productStockOperationService.addProductStockOperation(spdnPick.getPlantNb(),spdnPick.getTotalStock() * productVO.getBoxSpecification(),
//                spdnPick.getSsccNumber(),spdnPick.getMaterialNb(),spdnPick.getBatchNb(), StockOperationType.SALESOUT.getCode());


        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCTBINOUT.getCode(), ProductQRCodeUtil.getSSCC(qrCode), spdnPick.getMaterialNb());


    }

    @Override
    public void ship(List<String> ssccList, String carNb) {
        LambdaQueryWrapper<ProductSPDNPick> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductSPDNPick::getSsccNumber, ssccList)
                .eq(ProductSPDNPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode())
                .eq(ProductSPDNPick::getStatus, ProductSPDNPickEnum.WAITTING_SHIP.code());
        List<ProductSPDNPick> pickList = spdnPickService.list(queryWrapper);

        if (CollectionUtils.isEmpty(pickList)) {
            throw new ServiceException("均是不为待发运的数据");
        }
        if (pickList.size() != ssccList.size()) {
            List<String> existSSCCList = pickList.stream().map(ProductSPDNPick::getSsccNumber).collect(Collectors.toList());
            ssccList.removeAll(existSSCCList);
            throw new ServiceException("存在状态不为待发运的数据" + existSSCCList);
        }
        List<TranshipmentOrder> transhipmentOrderList = new ArrayList<>();
        //获取next trans order
        R<String> nextOrderNbR = remoteBinInService.getNextOrderNb();
        if (nextOrderNbR == null || !nextOrderNbR.isSuccess()) {
            throw new ServiceException("请求获取发运单号失败");
        }
        String nextOrderNb = nextOrderNbR.getData();
        pickList.forEach(item -> {
            if (!item.getStatus().equals(ProductSPDNPickEnum.WAITTING_SHIP.code())) {
                throw new ServiceException("sscc:" + item.getSsccNumber() + "对应任务状态为" + ProductSPDNPickEnum.getDesc(item.getStatus()));
            }
            TranshipmentOrder transhipmentOrder = new TranshipmentOrder();
            transhipmentOrder.setOrderNumber(nextOrderNb);
            transhipmentOrder.setSsccNumber(item.getSsccNumber());
            transhipmentOrder.setMaterialCode(item.getMaterialNb());
            transhipmentOrder.setProductWareShiftId(item.getId());
            transhipmentOrder.setCarNb(carNb);
            transhipmentOrder.setStatus(0);
            transhipmentOrderList.add(transhipmentOrder);

            item.setStatus(ProductSPDNPickEnum.FINISH.code());

        });

        //修改移库状态
        spdnPickService.updateBatchById(pickList);


        if (!CollectionUtils.isEmpty(transhipmentOrderList)) {
            R saveR = remoteBinInService.saveBatch(transhipmentOrderList);
            if (saveR == null || !saveR.isSuccess()) {
                throw new ServiceException("远程调用生成转运单失败，请重试");
            }
        }
    }

    @Override
    public void batchShip(Long[] ids) {
        List<SPDN> spdns = this.listByIds(Arrays.asList(ids));
        spdns.stream().forEach(spdn -> {
            if (!spdn.getStatus().equals(SPDNStatusEnum.WAITING_APPROVE.code())) {
                throw new ServiceException("只能选择已上传状态的数据");
            }
            if (!"7761".equals(spdn.getPlant())) {
                throw new ServiceException("只能选择7761的数据进行发运");
            }
            spdn.setStatus(SPDNStatusEnum.SHIPPED.code());
        });
        this.updateBatchById(spdns);

    }

    private MdProductPackagingVO getProductVO(String code) {
        R<MdProductPackagingVO> byCode = remoteProductService.getByCode(code);
        if (byCode == null || !byCode.isSuccess()) {
            throw new ServiceException("调用主数据获取成品失败");
        }
        return byCode.getData();
    }

}
