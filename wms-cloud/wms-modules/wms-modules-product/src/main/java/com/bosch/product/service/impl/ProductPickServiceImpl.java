package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.SUDN;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.dto.EditBinDownQuantityDTO;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.enumeration.ProductPickEnum;
import com.bosch.product.api.domain.vo.ProductPickBinDownVO;
import com.bosch.product.api.domain.vo.ProductPickExportVO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.mapper.ProductPickMapper;
import com.bosch.product.service.IProductPickService;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.ISUDNService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.StockOperationType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IProductStockOperationService;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:20
 **/
@Service
public class ProductPickServiceImpl extends ServiceImpl<ProductPickMapper, ProductPick> implements IProductPickService {


    @Autowired
    private ProductPickMapper productPickMapper;

    @Autowired
    private IProductStockService productStockService;

    @Resource
    private RemoteProductService remoteProductService;

    @Autowired
    @Lazy
    private ISUDNService sudnService;

    @Autowired
    private IProductStockOperationService productStockOperationService;

    @Autowired
    private IUserOperationLogService userOperationLogService;


    @Override
    public List<ProductPickVO> list(ProductPickDTO queryDTO) {
        return productPickMapper.list(queryDTO);
    }

    @Override
    public void batchCancel(List<Long> idList) {
        List<ProductPick> productPicks = this.listByIds(idList);
        List<String> sscccList = productPicks.stream().map(ProductPick::getSscc).collect(Collectors.toList());
        LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.in(ProductStock::getSsccNumber,sscccList);
        stockWrapper.eq(ProductStock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> productStocks = productStockService.list(stockWrapper);
        Map<String, List<ProductStock>> ssccStockMap = productStocks.stream()
                .collect(Collectors.groupingBy(ProductStock::getSsccNumber));


        productPicks.stream().forEach(item -> {
            if (!(item.getStatus().equals(ProductPickEnum.WAITING_ISSUE.code()) || item.getStatus().equals(ProductPickEnum.WAITTING_DOWN.code()))) {
                throw new ServiceException("存在对应捡配任务该状态下不可以取消:" + ProductPickEnum.getDesc(item.getStatus()));
            }
            item.setStatus(ProductPickEnum.CANCEL.code());

            ProductStock productStock = ssccStockMap.get(item.getSscc()).get(0);
            MdProductPackagingVO productVO = getProductVO(productStock.getMaterialNb());
            Double binDownQuantity = item.getBinDownQuantity();
            //转换为TR
            double v = binDownQuantity / productVO.getBoxSpecification();
            productStock.setAvailableStock(productStock.getAvailableStock()+v);
            productStock.setFreezeStock(productStock.getFreezeStock()-v);


        });
        this.updateBatchById(productPicks);
        //删除后还要恢复库存
        productStockService.updateBatchById(productStocks);
    }

    @Override
    public void cancel(Long id) {
        ArrayList<Long> list = new ArrayList<>();
        list.add(id);
        batchCancel(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifySscc(Long id, Long stockId) {
        ProductPick productPick = this.getById(id);
        if (productPick.getStatus() != ProductPickEnum.WAITING_ISSUE.code() || productPick.getStatus() != ProductPickEnum.WAITTING_DOWN.code()) {
            throw new ServiceException(ProductPickEnum.getDesc(productPick.getStatus()) + "状态下暂不可修改");
        }
        ProductStock productStock = productStockService.getById(stockId);
        if (productStock.getAvailableStock() - productPick.getDeliveryQuantity() < 0) {
            throw new ServiceException("该托可用库存不足，请重新选择");
        }
        //修改库存信息
        productStock.setAvailableStock(productStock.getAvailableStock() - productPick.getDeliveryQuantity());
        productStock.setFreezeStock(productStock.getFreezeStock() + productPick.getDeliveryQuantity());


        ProductPick newPick = BeanConverUtil.conver(productPick, ProductPick.class);
        newPick.setId(null);
        newPick.setSscc(productStock.getSsccNumber());
        newPick.setBatch(productStock.getBatchNb());
        newPick.setWareCode(productStock.getWareCode());
        newPick.setAreaCode(productStock.getAreaCode());
        newPick.setFrameCode(productStock.getFrameCode());
        newPick.setBinCode(productStock.getBinCode());
        this.save(newPick);

        //原任务取消
        productPick.setStatus(ProductPickEnum.CANCEL.code());
        this.updateById(productPick);

        productStockService.updateById(productStock);


    }

    @Override
    public ProductPickBinDownVO binDown(String qrCode, Long sudnId) {

        String sscc = ProductQRCodeUtil.getSSCC(qrCode);

        LambdaQueryWrapper<ProductPick> pickQueryWrapper = new LambdaQueryWrapper<>();
        pickQueryWrapper.eq(ProductPick::getSscc, sscc);
        pickQueryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        pickQueryWrapper.eq(ProductPick::getSudnId, sudnId);
        pickQueryWrapper.last("limit 1");
        ProductPick productPick = this.getOne(pickQueryWrapper);
        if (productPick.getStatus() != ProductPickEnum.WAITTING_DOWN.code()) {
            throw new ServiceException("状态为：" + ProductPickEnum.getDesc(productPick.getStatus()) + "，暂时不可下架");
        }
        //先查询库存信息
        LambdaQueryWrapper<ProductStock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(ProductStock::getSsccNumber, sscc);
        stockQueryWrapper.eq(ProductStock::getPlantNb, "7761");
        stockQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        ProductStock productStock = productStockService.getOne(stockQueryWrapper);

        productStock.setTotalStock(productStock.getTotalStock() - productPick.getDeliveryQuantity());
        productStock.setFreezeStock(productStock.getFreezeStock() - productPick.getDeliveryQuantity());


        ProductPickBinDownVO productPickBinDownVO = new ProductPickBinDownVO();
        productPickBinDownVO.setSscc(sscc);

        //如果总库存量是0 了，那么久一整托下架就可以了
        if (productStock.getTotalStock() == 0) {
            productStock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            productPickBinDownVO.setType(0);
        } else {//如果不是0 ，代表还有，PDA需要提示把原托放到
            productPickBinDownVO.setType(1);
        }
        productPickBinDownVO.setBinDownQuality(productPick.getDeliveryQuantity());

        productStockService.updateById(productStock);


        productPick.setStatus(ProductPickEnum.WAITTING_SHIP.code());
        this.updateById(productPick);

        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCTBINOUT.getCode(), ProductQRCodeUtil.getSSCC(qrCode),productPick.getMaterial());


        return productPickBinDownVO;


    }

    @Override
    public List<ProductPickVO> getBySudnId(Long sudnId, Integer status) {
        ProductPickDTO productPickDTO = new ProductPickDTO();
        productPickDTO.setSudnId(sudnId);
        productPickDTO.setStatus(status);
        return productPickMapper.list(productPickDTO);
    }

    @Override
    public void editBinDownQuantity(EditBinDownQuantityDTO dto) {
        Long pickId = dto.getPickId();
        ProductPick pick = this.getById(pickId);
        if (pick == null) {
            throw new ServiceException("该条数据不存在");
        }
        if (pick.getStatus() != ProductPickEnum.WAITTING_SHIP.code()) {
            throw new ServiceException("该条数据状态为:" + ProductPickEnum.getDesc(pick.getStatus()) + ",不可修改下架量");
        }

        String sscc = pick.getSscc();
        Double diff = pick.getBinDownQuantity() - dto.getNewBinDownQuantity();

        //转化为箱
        MdProductPackagingVO productVO = getProductVO(pick.getMaterial());

        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        ProductStock productStock = productStockService.getOne(queryWrapper);
        if (productStock == null) {
            throw new ServiceException("该托目前在库存中已经不存在");
        }
        if (productStock != null) {
            productStock.setAvailableStock(productStock.getAvailableStock() + diff / productVO.getBoxSpecification());
            productStock.setTotalStock(productStock.getTotalStock() + diff / productVO.getBoxSpecification());
            productStockService.updateById(productStock);
        } else {//如果为空，放到原库位
            LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
            stockWrapper.eq(ProductStock::getSsccNumber, sscc);
            stockWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.TRUE.getCode());
            stockWrapper.orderByDesc(ProductStock::getUpdateTime);
            stockWrapper.last("limit 1");
            ProductStock stock = productStockService.getOne(stockWrapper);
            if (stock == null) {
                throw new ServiceException("该托不存在于库存中。修改失败");
            }
            stock.setDeleteFlag(DeleteFlagStatus.FALSE.getCode());
            stock.setTotalStock(diff / productVO.getBoxSpecification());
            stock.setFreezeStock(Double.valueOf(0));
            stock.setAvailableStock(diff / productVO.getBoxSpecification());
            productStockService.updateById(stock);
        }
        pick.setBinDownQuantity(dto.getNewBinDownQuantity());


        productStockOperationService.addProductStockOperation(productStock.getPlantNb(), diff,
                productStock.getSsccNumber(), productStock.getMaterialNb(), productStock.getFromProdOrder(), StockOperationType.SALESOUT.getCode());



        this.updateById(pick);

    }

    @Override
    public void batchIssue(List<Long> idList) {
        List<ProductPick> pickList = this.listByIds(idList);
        pickList.forEach(item -> {
            if (!item.getStatus().equals(ProductPickEnum.WAITING_ISSUE.code())) {
                throw new ServiceException("存在非待下发的任务");
            }
            item.setStatus(ProductPickEnum.WAITTING_DOWN.code());
        });
        this.updateBatchById(pickList);

    }

    @Override
    public List<ProductPickVO> binDownlist(ProductPickDTO queryDTO) {
        return productPickMapper.binDownlist(queryDTO);
    }

    @Override
    public ProductPickBinDownVO sumBinDown(String qrCode) {

        String sscc = qrCode;
        if (qrCode.length() != 18) {
            sscc = ProductQRCodeUtil.getSSCC(qrCode);
        }
        LambdaQueryWrapper<ProductPick> pickQueryWrapper = new LambdaQueryWrapper<>();
        pickQueryWrapper.eq(ProductPick::getSscc, sscc);
        pickQueryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        pickQueryWrapper.eq(ProductPick::getStatus, ProductPickEnum.WAITTING_DOWN.code());
        List<ProductPick> productPicks = this.list(pickQueryWrapper);
        if (CollectionUtils.isEmpty(productPicks)) {
            throw new ServiceException("该SSCC" + sscc + "不存在捡配任务");
        }
        double downSum = productPicks.stream().mapToDouble(ProductPick::getDeliveryQuantity).sum();


        LambdaQueryWrapper<ProductStock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(ProductStock::getSsccNumber, sscc);
        stockQueryWrapper.eq(ProductStock::getPlantNb, "7761");
        stockQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        ProductStock productStock = productStockService.getOne(stockQueryWrapper);

        MdProductPackagingVO productVO = getProductVO(productStock.getMaterialNb());

        //转化为箱
        double tr = downSum / productVO.getBoxSpecification();

        productStock.setTotalStock(productStock.getTotalStock() - tr);
        productStock.setFreezeStock(productStock.getFreezeStock() - tr);

        productPicks.stream().forEach(item -> {
            item.setBinDownQuantity(item.getDeliveryQuantity());
            item.setStatus(ProductPickEnum.FINISH.code());
        });

        ProductPickBinDownVO productPickBinDownVO = new ProductPickBinDownVO();
        productPickBinDownVO.setSscc(sscc);

        //如果总库存量是0 了，那么久一整托下架就可以了
        if (productStock.getTotalStock() == 0) {
            productStock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            productPickBinDownVO.setType(0);
        } else {//如果不是0 ，代表还有，PDA需要提示把原托放到
            productPickBinDownVO.setType(1);
        }

        productStockService.updateById(productStock);


        this.updateBatchById(productPicks);


        Map<Long, List<ProductPick>> groupBySudn = productPicks.stream().collect(Collectors.groupingBy(ProductPick::getSudnId));

        AtomicReference<Double> total = new AtomicReference<>((double) 0);

        groupBySudn.forEach((sudnID, picks) -> {
            SUDN sudn = sudnService.getById(sudnID);
            double sum = picks.stream().mapToDouble(ProductPick::getBinDownQuantity).sum();
            total.set(total.get() + sum);
            sudn.setSumBinDownQuantity(sudn.getSumBinDownQuantity() + sum);
            sudnService.updateById(sudn);
        });


        productStockOperationService.addProductStockOperation(productStock.getPlantNb(),total.get(),
                productStock.getSsccNumber(),productStock.getMaterialNb(),productStock.getFromProdOrder(), StockOperationType.SALESOUT.getCode());

        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCTBINOUT.getCode(), ProductQRCodeUtil.getSSCC(qrCode),productPicks.get(0).getMaterial());


        return productPickBinDownVO;


    }

    @Override
    public ProductPickBinDownVO sumBinDownBySSCC(String sscc) {
        LambdaQueryWrapper<ProductPick> pickQueryWrapper = new LambdaQueryWrapper<>();
        pickQueryWrapper.eq(ProductPick::getSscc, sscc);
        pickQueryWrapper.eq(ProductPick::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        pickQueryWrapper.eq(ProductPick::getStatus, ProductPickEnum.WAITTING_DOWN.code());
        List<ProductPick> productPicks = this.list(pickQueryWrapper);
        if (CollectionUtils.isEmpty(productPicks)) {
            throw new ServiceException("该SSCC" + sscc + "不存在捡配任务");
        }
        double downSum = productPicks.stream().mapToDouble(ProductPick::getDeliveryQuantity).sum();


        LambdaQueryWrapper<ProductStock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(ProductStock::getSsccNumber, sscc);
        stockQueryWrapper.eq(ProductStock::getPlantNb, "7761");
        stockQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        ProductStock productStock = productStockService.getOne(stockQueryWrapper);

        MdProductPackagingVO productVO = getProductVO(productStock.getMaterialNb());

        //转化为箱
        double tr = downSum / productVO.getBoxSpecification();

        productStock.setTotalStock(productStock.getTotalStock() - tr);
        productStock.setFreezeStock(productStock.getFreezeStock() - tr);

        productPicks.stream().forEach(item -> {
            item.setBinDownQuantity(item.getDeliveryQuantity());
            item.setStatus(ProductPickEnum.FINISH.code());
        });

        ProductPickBinDownVO productPickBinDownVO = new ProductPickBinDownVO();
        productPickBinDownVO.setSscc(sscc);

        //如果总库存量是0 了，那么久一整托下架就可以了
        if (productStock.getTotalStock() == 0) {
            productStock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            productPickBinDownVO.setType(0);
        } else {//如果不是0 ，代表还有，PDA需要提示把原托放到
            productPickBinDownVO.setType(1);
        }

        productStockService.updateById(productStock);


        this.updateBatchById(productPicks);


        Map<Long, List<ProductPick>> groupBySudn = productPicks.stream().collect(Collectors.groupingBy(ProductPick::getSudnId));

        AtomicReference<Double> total = new AtomicReference<>((double) 0);

        groupBySudn.forEach((sudnID, picks) -> {
            SUDN sudn = sudnService.getById(sudnID);
            double sum = picks.stream().mapToDouble(ProductPick::getBinDownQuantity).sum();
            total.set(total.get() + sum);
            sudn.setSumBinDownQuantity(sudn.getSumBinDownQuantity() + sum);
            sudnService.updateById(sudn);
        });


        productStockOperationService.addProductStockOperation(productStock.getPlantNb(),total.get(),
                productStock.getSsccNumber(),productStock.getMaterialNb(),productStock.getFromProdOrder(), StockOperationType.SALESOUT.getCode());

        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCTBINOUT.getCode(), sscc,productPicks.get(0).getMaterial());


        return productPickBinDownVO;
    }

    @Override
    public List<ProductPickExportVO> getSUDNPickExportVO(ProductPickDTO sudndto) {
        return productPickMapper.getSUDNPickExportVO(sudndto);
    }


    private MdProductPackagingVO getProductVO(String code) {
        R<MdProductPackagingVO> byCode = remoteProductService.getByCode(code);
        if (byCode == null || !byCode.isSuccess()) {
            throw new ServiceException("调用主数据获取成品失败");
        }
        return byCode.getData();
    }


}
