package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.enumeration.SPDNStatusEnum;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.dto.SUDNDTO;
import com.bosch.product.api.domain.dto.SUDNShipDTO;
import com.bosch.product.api.domain.enumeration.ProductPickEnum;
import com.bosch.product.api.domain.enumeration.SUDNStatusEnum;
import com.bosch.product.api.domain.vo.SUDNVO;
import com.bosch.product.mapper.SUDNMapper;
import com.bosch.product.service.IProductPickService;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.ISUDNService;
import com.ruoyi.common.core.constant.AreaListConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DoubleMathUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-06-12 12:55
 **/
@Service
public class SUDNServiceImpl extends ServiceImpl<SUDNMapper, SUDN>
        implements ISUDNService {

    @Autowired
    private SUDNMapper sudnMapper;

    @Resource
    private RemoteProductService remoteProductService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private IProductPickService pickService;


    @Override
    public List<SUDNVO> getList(SUDNDTO sudndto) {
        return sudnMapper.getList(sudndto);
    }

    @Override
    public void validList(List<SUDNDTO> dtos) {
        List<String> deliveries = dtos.stream().map(SUDNDTO::getDelivery).collect(Collectors.toList());
        LambdaQueryWrapper<SUDN> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SUDN::getDelivery, deliveries);
        queryWrapper.eq(SUDN::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<SUDN> sudnList = this.list(queryWrapper);
        if (!CollectionUtils.isEmpty(sudnList)) {
            List<String> list = sudnList.stream().map(SUDN::getDelivery).collect(Collectors.toList());
            HashSet<String> strings = new HashSet<>(list);
            throw new ServiceException("以下DN号在系统中 已经存在，请删除后再上传。" + strings);
        }


        dtos.stream().forEach(item -> {
            setQty(item.getDeliveryQuantityString(), item);
            item.setStatus(SPDNStatusEnum.WAITING_APPROVE.code());
        });
    }

    public void setQty(String qty, SUDNDTO sudndto) {
        try {
            if (StringUtils.isNotEmpty(qty)) {
                sudndto.setDeliveryQuantity(new DecimalFormat().parse(qty).doubleValue());
            }

        } catch (Exception e) {
            throw new ServiceException("Qty列格式不正确");
        }
    }

    @Override
    public void batchDelete(List<Long> ids) {
        List<SUDN> sudnList = this.listByIds(ids);
//        List<SUDN> sudnList = sudns.stream().filter(sudn -> sudn.getStatus() == SUDNStatusEnum.WAITING_GEN.code()).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(sudnList)) {
//            throw new ServiceException("无待生成的数据，请选择数据后重试。");
//        }
        sudnList.stream().forEach(item -> item.setDeleteFlag(DeleteFlagStatus.TRUE.getCode()));

        List<Long> sudnIds = sudnList.stream().map(SUDN::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ProductPick> pickQueryWrapper = new LambdaQueryWrapper<>();
        pickQueryWrapper.in(ProductPick::getSudnId, sudnIds);
        pickQueryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        pickQueryWrapper.ne(ProductPick::getStatus, ProductPickEnum.CANCEL.code());
        List<ProductPick> list = pickService.list(pickQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> pickIds = list.stream().map(ProductPick::getId).collect(Collectors.toList());
            pickService.batchCancel(pickIds);
        }

        this.updateBatchById(sudnList);
    }

    @Override
    public void generate(List<Long> idsList) {
        List<SUDN> sudnList = this.listByIds(idsList);
        sudnList.stream().forEach(item -> {
            if (item.getStatus() == SUDNStatusEnum.GENERATED.code()) {
                throw new ServiceException("包含已经生成的数据。");
            }
        });

        List<SUDN> sortedSUDNList = sudnList.stream().sorted(Comparator.comparing(SUDN::getShipToParty).thenComparing(SUDN::getItem)).collect(Collectors.toList());

        List<String> materialCodeList = sortedSUDNList.stream().map(SUDN::getMaterial).collect(Collectors.toList());
        //查询所有库存
        LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.in(ProductStock::getMaterialNb, materialCodeList);
        stockWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockWrapper.eq(ProductStock::getPlantNb, "7761");
        stockWrapper.eq(ProductStock::getQualityStatus, QualityStatusEnums.USE.getCode());
        stockWrapper.ge(ProductStock::getAvailableStock, Double.valueOf(0));
        List<ProductStock> stockList = productStockService.list(stockWrapper);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new ServiceException("无可用库存");
        }
        Map<String, List<ProductStock>> materialStockMap = stockList.stream()
                .collect(Collectors.groupingBy(ProductStock::getMaterialNb));

        List<ProductPick> pickInsertList = new ArrayList<>();

        for (SUDN item : sortedSUDNList) {
            //查询成品信息
            MdProductPackagingVO productVO = getProductVO(item.getMaterial());
            Double boxSpecification = productVO.getBoxSpecification();
            String material = item.getMaterial();

            //查询库存
            List<ProductStock> productStocks = materialStockMap.get(material);
            if (CollectionUtils.isEmpty(productStocks)) {
                throw new ServiceException("当前没有物料号为" + material + "的库存");
            }
            List<ProductStock> productSortedStockList = productStocks.stream()
                    .sorted(Comparator.comparing(ProductStock::getExpireDate).thenComparing(ProductStock::getFromProdOrder).thenComparing(ProductStock::getTotalStock).thenComparing(ProductStock::getSsccNumber)).collect(Collectors.toList());

            //如果有指定批次的，需要筛选指定批次的库存信息
            if (StringUtils.isNotEmpty(item.getStorageLocation())) {
                productSortedStockList = productSortedStockList.stream().filter(stock -> stock.getFromProdOrder().equals(item.getStorageLocation())).collect(Collectors.toList());
            }else {
                 productSortedStockList = productSortedStockList.stream()
                        .filter(stock -> !AreaListConstants.oDDdArea(stock.getAreaCode()))
                        .filter(stock -> !AreaListConstants.noQualifiedArea(stock.getAreaCode()))
                        .sorted(Comparator.comparing(ProductStock::getExpireDate).thenComparing(ProductStock::getFromProdOrder).thenComparing(ProductStock::getTotalStock).thenComparing(ProductStock::getSsccNumber)).collect(Collectors.toList());

            }
            double currentQty = 0.0;
            List<ProductStock> useProductStocks = new ArrayList<>();
            //判断库存是否还能满足
            AtomicReference<Double> sum = new AtomicReference<>((double) 0);
//            double sum = productSortedStockList.stream().mapToDouble(ProductStock::getAvailableStock).sum();
            productSortedStockList.forEach(p->{
                sum.set(DoubleMathUtil.doubleMathCalculation(sum.get(), p.getAvailableStock(),"+"));
            });
            if ((double) Math.round((boxSpecification * sum.get() * 100) / 100.0) < item.getDeliveryQuantity()) {
                throw new ServiceException("DN" + item.getDelivery() + ",item:" + item.getItem() + ",Material:" + item.getMaterial() + "当前库存已不满足");
            }
            for (ProductStock stock : productSortedStockList) {
                if (stock.getAvailableStock() <= 0) {
                    continue;
                }
                Double availableStock = stock.getAvailableStock();
                availableStock = (double) Math.round((boxSpecification * availableStock * 100) / 100.0);
                currentQty += availableStock;
                useProductStocks.add(stock);
                if (currentQty >= item.getDeliveryQuantity()) {
                    break; // 达到目标值，结束遍历
                }
            }
            //箱
            AtomicReference<Double> useSum = new AtomicReference<>((double) 0);
            useProductStocks.forEach(u -> useSum.set(DoubleMathUtil.doubleMathCalculation(useSum.get(), u.getAvailableStock(), "+")));
//            double useSum = useProductStocks.stream().mapToDouble(ProductStock::getAvailableStock).sum();
            //  转化为PCS
            useSum.set((double) Math.round((boxSpecification * useSum.get() * 100) / 100.0));
            List<ProductPick> pickList = new ArrayList<>();
            for (ProductStock stock : useProductStocks) {
                ProductPick productPick = BeanConverUtil.conver(item, ProductPick.class);
                productPick.setId(null);
                productPick.setWareCode(stock.getWareCode());

                productPick.setSscc(stock.getSsccNumber());
                productPick.setBinCode(stock.getBinCode());
                productPick.setBatch(stock.getBatchNb());
                productPick.setStatus(ProductPickEnum.WAITING_ISSUE.code());
                productPick.setFrameCode(stock.getFrameCode());
                productPick.setSudnId(item.getId());
                productPick.setAreaCode(stock.getAreaCode());
                productPick.setDeliveryQuantity((double) Math.round(stock.getAvailableStock() * boxSpecification * 100 / 100.0));
                productPick.setBinDownQuantity((double) Math.round(stock.getAvailableStock() * boxSpecification * 100 / 100.0));
                productPick.setProductionBatch(stock.getFromProdOrder());
                productPick.setExpireDate(stock.getExpireDate());

                if (useProductStocks.get(useProductStocks.size() - 1) == stock) {

                    double abs = (double) Math.round(Math.abs(useSum.get() - item.getDeliveryQuantity()) * 100 / 100.0);//PCS
                    //对应的箱
                    double boxDiff = abs / boxSpecification;
                    stock.setFreezeStock(stock.getFreezeStock() + (stock.getAvailableStock() - boxDiff));
                    productPick.setDeliveryQuantity((double) Math.round((stock.getAvailableStock() * boxSpecification - abs) * 100 / 100.0));
                    productPick.setBinDownQuantity((double) Math.round((stock.getAvailableStock() * boxSpecification - abs) * 100 / 100.0));

                } else {
                    stock.setFreezeStock(stock.getFreezeStock() + stock.getAvailableStock());
                }
                stock.setAvailableStock(stock.getTotalStock() - stock.getFreezeStock());
                pickList.add(productPick);
            }
            pickInsertList.addAll(pickList);
            item.setStatus(SUDNStatusEnum.GENERATED.code());


        }

        this.updateBatchById(sudnList);

        pickService.saveBatch(pickInsertList);

        materialStockMap.forEach((key, value) -> productStockService.updateBatchById(value));


    }

    @Override
    public void cancel(Long id) {
        ArrayList<Long> list = new ArrayList<>();
        list.add(id);
        batchDelete(list);
    }

    @Override
    public void modifyQuantity(Long id, Double quantity) {
        SUDN sudn = this.getById(id);
        if (sudn == null) {
            throw new ServiceException("不存在该条数据");
        }
        if (sudn.getStatus() != SUDNStatusEnum.WAITING_GEN.code()) {
            throw new ServiceException("当前状态为:" + SUDNStatusEnum.getDesc(sudn.getStatus()) + ",不可以修改");
        }
        sudn.setDeliveryQuantity(quantity);
        this.updateById(sudn);

    }

    @Override
    public List<SUDNVO> getUnFinishedSUDN(SUDNDTO sudndto) {
        return sudnMapper.getUnFinishedSUDN(sudndto);
    }

    @Override
    public List<SUDNVO> getFinishedSUDN(SUDNDTO sudndto) {
        return sudnMapper.getFinishedSUDN(sudndto);
    }

    @Override
    public List<SUDNVO> getUnFinishedShipSUDN(SUDNDTO sudndto) {
        return sudnMapper.getUnFinishedShipSUDN(sudndto);
    }

    @Override
    public List<SUDNVO> getFinishedShipSUDN(SUDNDTO sudndto) {
        return sudnMapper.getFinishedShipSUDN(sudndto);
    }

    @Override
    public void ship(SUDNShipDTO shipDTO) {
        Long sudnId = shipDTO.getSudnId();
        SUDN sudn = this.getById(sudnId);
        if (sudn == null) {
            throw new ServiceException("该条数据不存在");
        }
        if (sudn.getShipQuantity() > sudn.getDeliveryQuantity()) {
            throw new ServiceException("该订单已经全部发运完");
        }
        LambdaQueryWrapper<ProductPick> pickWrapper = new LambdaQueryWrapper<>();
        pickWrapper.eq(ProductPick::getSudnId, sudnId);
        pickWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductPick> pickList = pickService.list(pickWrapper);
        if (CollectionUtils.isEmpty(pickList)) {
            throw new ServiceException("该SUDN下无捡配任务或无已下架信息");
        }

//        double binDownSum = pickList.stream().filter(item -> item.getStatus() == ProductPickEnum.WAITTING_SHIP.code()).mapToDouble(ProductPick::getBinDownQuantity).sum();
        double binDownSum = sudn.getSumBinDownQuantity();
        double shipQuantity = shipDTO.getShipQuantity();
        if (shipQuantity > binDownSum) {
            throw new ServiceException("发运数量不能大于已下架数量");
        }
        sudn.setSumBinDownQuantity(sudn.getSumBinDownQuantity() - shipQuantity);
        sudn.setShipQuantity(sudn.getShipQuantity() == null ? shipQuantity : sudn.getShipQuantity() + shipQuantity);
        this.updateById(sudn);


    }


    private MdProductPackagingVO getProductVO(String code) {
        R<MdProductPackagingVO> byCode = remoteProductService.getByCode(code);
        if (byCode == null || !byCode.isSuccess()) {
            throw new ServiceException("调用主数据获取成品失败");
        }
        return byCode.getData();
    }


}
