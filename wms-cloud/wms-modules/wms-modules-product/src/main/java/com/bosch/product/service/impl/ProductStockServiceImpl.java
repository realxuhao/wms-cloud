package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.SplitRecord;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.domain.dto.SplitPalletDTO;
import com.bosch.binin.api.enumeration.ManuTransStatusEnum;
import com.bosch.binin.api.enumeration.MaterialReturnStatusEnum;
import com.bosch.binin.api.enumeration.StockWholeFlagEnum;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.*;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.enumeration.ProductStockBinInEnum;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.product.api.domain.vo.ProductReturnVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.mapper.ProductStockMapper;
import com.bosch.product.service.*;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.*;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.log.enums.MaterialType;
import com.ruoyi.common.log.enums.StockOperationType;
import com.ruoyi.common.log.enums.UserOperationType;
import com.ruoyi.common.log.service.IProductStockOperationService;
import com.ruoyi.common.log.service.IUserOperationLogService;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:21
 **/
@Service
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements IProductStockService {

    @Autowired
    private ProductStockMapper stockMapper;

    @Resource
    private RemoteProductService remoteProductService;

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;


    @Autowired
    private IProductStockAdjustService stockAdjustService;

    @Autowired
    private IProductReturnService productReturnService;

    @Autowired
    private IManualTransferOrderService manualTransferOrderService;

    @Autowired
    private IProductSplitService productSplitService;

    @Autowired
    private IProductStockOperationService productStockOperationService;

    @Autowired
    @Lazy
    private IProductWareShiftService wareShiftService;

    @Autowired
    private IUserOperationLogService userOperationLogService;

    @Override
    public void generateStockByReceive(ProductReceive receive) {
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, receive.getSsccNumber());
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = stockMapper.selectOne(queryWrapper);
        if (Objects.nonNull(productStock)) {
            throw new ServiceException("该SSCC码:" + receive.getSsccNumber() + "已有库存");
        }

        AreaVO areaVO = getAreaByType(SecurityUtils.getWareCode(), AreaTypeEnum.PRO.getCode());

        ProductStock stock = new ProductStock();
        stock.setSsccNumber(receive.getSsccNumber());
        stock.setPlantNb(receive.getPlantNb());
        stock.setWareCode(receive.getWareCode());
        stock.setAreaCode(areaVO.getCode());
        stock.setMaterialNb(receive.getMaterialNb());
        stock.setBatchNb(receive.getBatchNb());
        stock.setExpireDate(DateUtils.parseDate(receive.getExpireDate()));
        stock.setTotalStock(receive.getInQuantity());
        stock.setFreezeStock((double) 0);
        stock.setAvailableStock(receive.getInQuantity());
        stock.setFromProdOrder(receive.getFromProdOrder());
        stock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
        stock.setProductionDate(receive.getProductionDate());
        stock.setUnit(receive.getUnit());


        if (stock.getPlantNb().contains("7751")) {

            ProductWareShift wareShift = ProductWareShift.builder().sourcePlantNb(stock.getPlantNb())
                    .sourceWareCode(stock.getWareCode())
                    .sourceAreaCode(stock.getAreaCode())
                    .sourceBinCode(stock.getBinCode())
                    .quantity(stock.getTotalStock())
                    .moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                    .ssccNb(stock.getSsccNumber())
                    .materialNb(stock.getMaterialNb())
                    .batchNb(stock.getBatchNb())
                    .expireDate(stock.getExpireDate())
                    .productionDate(stock.getProductionDate())
                    .unit(stock.getUnit())
                    .status(ProductWareShiftEnum.WAITTING_SHIPPING.code())
                    .fromProdOrder(stock.getFromProdOrder())
                    .build();
            wareShiftService.save(wareShift);
            stock.setFreezeStock(stock.getTotalStock());
            stock.setAvailableStock(Double.valueOf(0));
        }

        stockMapper.insert(stock);


    }

    @Override
    public void generateStockByProductWareShifts(List<ProductWareShift> productWareShiftList) {
        AreaVO areaVO = getAreaByType(SecurityUtils.getWareCode(), AreaTypeEnum.PRO.getCode());
        List<String> ssccList = productWareShiftList.stream().map(ProductWareShift::getSsccNb).collect(Collectors.toList());
        //修改库存
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductStock::getSsccNumber, ssccList);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductStock> productStocks = this.list(queryWrapper);
        productStocks.forEach(item -> {
            item.setFreezeStock(Double.valueOf(0));
            item.setAvailableStock(item.getTotalStock());
            item.setWareCode(areaVO.getWareCode());
            item.setAreaCode(areaVO.getCode());
            item.setPlantNb(areaVO.getPlantNb());
            item.setBinInFlag(ProductStockBinInEnum.WAITTING_BIN_IN.code());
        });
        this.updateBatchById(productStocks);

    }

    @Override
    public List<ProductStockVO> list(ProductStockQueryDTO stockQueryDTO) {
        return stockMapper.list(stockQueryDTO);
    }

    @Override
    public List<ProductStockVO> allList(ProductStockQueryDTO stockQueryDTO) {
        return stockMapper.allList(stockQueryDTO);
    }


    public AreaVO getAreaByType(String wareCode, Integer areaType) {
        //查询区域列表
        //根据areaType查询区域
        R<List<AreaVO>> areaListR = remoteMasterDataService.getByWareCode(wareCode);
        if (areaListR == null || !areaListR.isSuccess()) {
            throw new ServiceException("调用主数据服务查询区域列表失败");
        }
        if (StringUtils.isEmpty(areaListR.getData())) {
            throw new ServiceException("没有区域，请维护主数据");
        }
        List<AreaVO> areaVOList = areaListR.getData();
        List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType().equals(areaType)).collect(Collectors.toList());
        if (StringUtils.isEmpty(areaList)) {
            throw new ServiceException("没有类型为" + AreaTypeEnum.getDescByCode(AreaTypeEnum.PRO.getCode()) + "的区域");
        }
        return areaList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductStock binIn(ProductBinInDTO binInDTO) {
        String sscc = binInDTO.getSscc();
        String binCode = binInDTO.getBinCode();
        String recommendBinCode = binInDTO.getRecommendBinCode();
        //校验状态是不是待上架
        LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.eq(ProductStock::getSsccNumber, sscc);
        stockWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockWrapper.last("limit 1");
        ProductStock productStock = this.getOne(stockWrapper);
        if (Objects.isNull(productStock)) {
            throw new ServiceException("该sscc" + sscc + "没有对应上架任务");
        }
        if (!productStock.getBinInFlag().equals(ProductStockBinInEnum.WAITTING_BIN_IN.code())) {
            throw new ServiceException("该sscc" + sscc + "非待上架状态");
        }

        stockWrapper.clear();
        stockWrapper.eq(ProductStock::getBinCode, binCode);
        stockWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockWrapper.eq(ProductStock::getBinInFlag, ProductStockBinInEnum.FINISH.code());
        stockWrapper.last("limit 1");
        ProductStock stock = this.getOne(stockWrapper);
        if (!Objects.isNull(stock)) {
            throw new ServiceException("该库位" + binCode + "已经被占用！");
        }

        BinVO binVO = getBinVOByBinCode(binCode);


        productStock.setBinInFlag(ProductStockBinInEnum.FINISH.code());
        productStock.setRecommendBinCode(recommendBinCode);
        productStock.setBinCode(binCode);
        productStock.setAreaCode(binVO.getAreaCode());
        productStock.setFrameCode(binVO.getFrameCode());

        updateById(productStock);

        return productStock;

    }

    @Override
    public List<ProductStockVO> selectIQCManagementList(ProductIQCManagementQueryDTO queryDTO) {
        return stockMapper.selectIQCManagementList(queryDTO);
    }

    @Override
    public boolean validateStatus(Long id) {
        return stockMapper.validateStatus(id) == 1;
    }

    @Override
    public Integer changeStatus(IQCChangeStatusDTO iqcChangeStatusDTO) {
        Integer i = stockMapper.changeStatus(iqcChangeStatusDTO);
        return i;
    }

    @Override
    public ProductStock binInToArea(ProductBinInDTO binInDTO) {
        //校验状态是不是待上架
        LambdaQueryWrapper<ProductStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.eq(ProductStock::getSsccNumber, binInDTO.getSscc());
        stockWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockWrapper.last("limit 1");
        ProductStock productStock = this.getOne(stockWrapper);
        if (Objects.isNull(productStock)) {
            throw new ServiceException("该sscc" + binInDTO.getSscc() + "没有对应上架任务");
        }
        if (!productStock.getBinInFlag().equals(ProductStockBinInEnum.WAITTING_BIN_IN.code())) {
            throw new ServiceException("该sscc" + binInDTO.getSscc() + "非待上架状态");
        }
        productStock.setBinInFlag(ProductStockBinInEnum.FINISH.code());
        productStock.setRecommendBinCode(binInDTO.getRecommendBinCode());
        productStock.setAreaCode(binInDTO.getAreaCode());
        updateById(productStock);
        return productStock;
    }

    @Override
    public void editStock(EditStockDTO dto) {
        if (dto.getFreezeStock() < dto.getTotalStock()) {
            throw new ServiceException("冻结库存不可以大于总库存");
        }
        String sscc = ProductQRCodeUtil.getSSCC(dto.getBarCode());
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = this.getOne(queryWrapper);
        productStock.setTotalStock(dto.getTotalStock());
        productStock.setFreezeStock(dto.getFreezeStock());

        this.updateById(productStock);


    }

    @Override
    public List<ProductStockVO> selectSUQAIQCManagementList(ProductIQCManagementQueryDTO queryDTO) {
        return stockMapper.selectSUQAIQCManagementList(queryDTO);
    }

    @Override
    public boolean validateSUQAStatus(Long id) {
        return stockMapper.validateSUQAStatus(id) == 1;
    }

    @Override
    public Integer changeSUQAStatus(IQCChangeStatusDTO iqcChangeStatusDTO) {
        Integer i = stockMapper.changeSUQAStatus(iqcChangeStatusDTO);
        return i;
    }

    @Override
    public List<ProductStockVO> spdnStocklist(ProductStockQueryDTO stockQueryDTO) {
        return stockMapper.spdnStocklist(stockQueryDTO);
    }

    @Override
    public void adjustStock(ProductStockEditDTO stockEditDTO) {
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, stockEditDTO.getSsccNumber());
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock stock = this.getOne(queryWrapper);
        if (stock == null) {
            throw new ServiceException("无此库存信息");
        }

        ProductStockAdjust stockAdjust = BeanConverUtil.conver(stock, ProductStockAdjust.class);

        MdProductPackagingVO productVO = getProductVO(stock.getMaterialNb());

        //   PCS/TR
        Double boxSpecification = productVO.getBoxSpecification();


        if (stockEditDTO.getType() == 0 || stockEditDTO.getType() == 1) {//质检取样和取样

            Double stockUseTR = stockEditDTO.getStockUse() / boxSpecification;


            if (stockUseTR > stock.getAvailableStock()) {
                throw new ServiceException("领用数量不能大于可用数量");
            }


            stock.setAvailableStock(stock.getAvailableStock() - stockUseTR);
            stock.setTotalStock(stock.getTotalStock() - stockUseTR);
            stock.setFreezeStock(stock.getTotalStock() - stock.getAvailableStock());
            if (stock.getAvailableStock() == Double.valueOf(0)) {
                stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            }


            productStockOperationService.addProductStockOperation(stock.getPlantNb(), stockEditDTO.getStockUse(), stock.getSsccNumber(),
                    stock.getMaterialNb(), stock.getFromProdOrder(), StockOperationType.OTHEROUT.getCode());
            this.updateById(stock);


        } else if (stockEditDTO.getType() == 2) {//报废
//            if (stock.getFreezeStock() > 0) {
//                throw new ServiceException("该库存存在执行任务，暂时不可以报废");
//            }
//            //根据areaType查询区域
//            R<List<AreaVO>> areaListR = remoteMasterDataService.getByWareCode(SecurityUtils.getWareCode());
//            if (!areaListR.isSuccess() || areaListR == null) {
//                throw new ServiceException("调用主数据服务查询区域列表失败");
//            }
//            if (StringUtils.isEmpty(areaListR.getData())) {
//                throw new ServiceException("没有区域，请维护主数据");
//            }
//            List<AreaVO> areaVOList = areaListR.getData();
//            List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType() == AreaTypeEnum.DISCARD.getCode()).collect(Collectors.toList());
//            if (StringUtils.isEmpty(areaList)) {
//                log.error("没有类型为" + "报废" + "的区域");
//                throw new ServiceException("没有类型为报废的区域");
//            }
//            AreaVO areaVO = areaList.get(0);
//            stock.setAreaCode(areaVO.getCode());
//            stock.setBinCode(null);
//            stock.setQualityStatus(QualityStatusEnums.BLOCK.getCode());
            Double stockUseTR = stockEditDTO.getStockUse() / boxSpecification;
            if (stockUseTR > stock.getAvailableStock()) {
                throw new ServiceException("报废数量不能大于可用数量");
            }
            stock.setAvailableStock(stock.getAvailableStock() - stockUseTR);
            stock.setTotalStock(stock.getTotalStock() - stockUseTR);
            stock.setFreezeStock(stock.getTotalStock() - stock.getAvailableStock());
            if (stock.getAvailableStock() <= 0) {
                stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            }

            productStockOperationService.addProductStockOperation(stock.getPlantNb(), stockEditDTO.getStockUse(), stock.getSsccNumber(),
                    stock.getMaterialNb(), stock.getFromProdOrder(), StockOperationType.OTHEROUT.getCode());

            this.updateById(stock);

        } else if (stockEditDTO.getType() == 3) {//整托出库
            if (stock.getFreezeStock() > 0) {
                throw new ServiceException("该库存存在执行任务，暂时不可以整托出库");
            }
            stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            productStockOperationService.addProductStockOperation(stock.getPlantNb(), stock.getTotalStock() * boxSpecification, stock.getSsccNumber(),
                    stock.getMaterialNb(), stock.getFromProdOrder(), StockOperationType.OTHEROUT.getCode());
            this.updateById(stock);
        } else {

            if (stockEditDTO.getSsccNumber() == null || stockEditDTO.getAvailableStock() == null || stockEditDTO.getFreezeStock() == null || stockEditDTO.getTotalStock() == null) {
                throw new ServiceException("所有参数都不能为空");
            }
            if (!stockEditDTO.getTotalStock().equals(stockEditDTO.getFreezeStock() + stockEditDTO.getAvailableStock())) {
                throw new ServiceException("总库存必须等于冻结库存+可用库存");
            }


            double diff = stock.getTotalStock() - stockEditDTO.getTotalStock();

            Double diffTR = diff / boxSpecification;

            stock.setAvailableStock(stockEditDTO.getAvailableStock() / boxSpecification);
            stock.setFreezeStock(stockEditDTO.getFreezeStock() / boxSpecification);
            stock.setTotalStock(stockEditDTO.getTotalStock() / boxSpecification);


            if (diff > 0) {
                productStockOperationService.addProductStockOperation(stock.getPlantNb(), diff, stock.getSsccNumber(),
                        stock.getMaterialNb(), stock.getFromProdOrder(), StockOperationType.IN.getCode());
            } else {
                productStockOperationService.addProductStockOperation(stock.getPlantNb(), diff, stock.getSsccNumber(),
                        stock.getMaterialNb(), stock.getFromProdOrder(), StockOperationType.OTHEROUT.getCode());
            }
            this.updateById(stock);

        }


        stockAdjust.setType(stockEditDTO.getType());
        stockAdjust.setId(null);
        stockAdjust.setAdjustFreezeStock(stock.getFreezeStock());
        stockAdjust.setAdjustTotalStock(stock.getTotalStock());
        stockAdjust.setAdjustAvailableStock(stock.getAvailableStock());
        stockAdjustService.save(stockAdjust);
    }

    private BinVO getBinVOByBinCode(String binCode) {
        R<BinVO> binInfoByCodeResult = remoteMasterDataService.getBinInfoByCode(binCode);
        if (StringUtils.isNull(binInfoByCodeResult) || StringUtils.isNull(binInfoByCodeResult.getData())) {
            throw new ServiceException("该库位：" + binCode + " 不存在");
        }

        if (R.FAIL == binInfoByCodeResult.getCode()) {
            throw new ServiceException(binInfoByCodeResult.getMsg());
        }
        return binInfoByCodeResult.getData();
    }


    @Override
    public void stockReturn(ProductReturnDTO productReturnDTO) {
        String qrCode = productReturnDTO.getQrCode();
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        String batchNb = ProductQRCodeUtil.getBatchNb(qrCode);
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = this.getOne(queryWrapper);
        if (productStock != null) {
            throw new ServiceException("该托已存在于库存！");
        }

        //根据areaType查询区域
        R<List<AreaVO>> areaListR = remoteMasterDataService.getByWareCode(SecurityUtils.getWareCode());
        if (!areaListR.isSuccess() || areaListR == null) {
            throw new ServiceException("调用主数据服务查询区域列表失败");
        }
        if (StringUtils.isEmpty(areaListR.getData())) {
            throw new ServiceException("没有区域，请维护主数据");
        }
        List<AreaVO> areaVOList = areaListR.getData();
        List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType() == AreaTypeEnum.RETURN.getCode()).collect(Collectors.toList());
        if (StringUtils.isEmpty(areaList)) {
            log.error("没有类型为" + "退货" + "的区域");
            throw new ServiceException("没有类型为退货的区域");
        }
        AreaVO areaVO = areaList.get(0);

        //查询物料号等信息
        LambdaQueryWrapper<ProductStock> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ProductStock::getBatchNb, batchNb);
        queryWrapper1.last("limit 1");
        ProductStock oldStock = this.getOne(queryWrapper1);

        MdProductPackagingVO productVO = getProductVO(oldStock.getMaterialNb());


        ProductStock stock = new ProductStock();
        if (productReturnDTO.getType() == 0) {//经销商退货，是退货到销售库存，7761
            stock.setSsccNumber(sscc);
            stock.setPlantNb("7761");
            stock.setWareCode(SecurityUtils.getWareCode());
            stock.setAreaCode(areaVO.getCode());
            if (oldStock != null) {
                stock.setMaterialNb(oldStock.getMaterialNb());
                stock.setBatchNb(oldStock.getBatchNb());
                stock.setExpireDate(oldStock.getExpireDate());
            }
            stock.setTotalStock(productReturnDTO.getQuantity() / productVO.getBoxSpecification());
            stock.setFreezeStock((double) 0);
            stock.setAvailableStock(productReturnDTO.getQuantity() / productVO.getBoxSpecification());
            stock.setQualityStatus(QualityStatusEnums.BLOCK.getCode());
            stock.setProductionDate(ProductQRCodeUtil.getProductionDate(qrCode));
            stock.setBinInFlag(ProductStockBinInEnum.NONE.code());
            this.save(stock);
        } else if (productReturnDTO.getType() == 1) {//退货到工厂。也就是从7761退货到工厂。
            //获取当前仓库的信息。
            stock.setSsccNumber(sscc);
            stock.setPlantNb(areaVO.getPlantNb());
            stock.setWareCode(areaVO.getWareCode());
            stock.setAreaCode(areaVO.getCode());
            if (oldStock != null) {
                stock.setMaterialNb(oldStock.getMaterialNb());
                stock.setBatchNb(oldStock.getBatchNb());
                stock.setExpireDate(oldStock.getExpireDate());
            }
            stock.setTotalStock(productReturnDTO.getQuantity() / productVO.getBoxSpecification());
            stock.setFreezeStock((double) 0);
            stock.setAvailableStock(productReturnDTO.getQuantity() / productVO.getBoxSpecification());
            stock.setQualityStatus(QualityStatusEnums.BLOCK.getCode());
            stock.setProductionDate(ProductQRCodeUtil.getProductionDate(qrCode));
            stock.setBinInFlag(ProductStockBinInEnum.NONE.code());
            this.save(stock);

        }


        ProductReturn productReturn = new ProductReturn();
        productReturn.setPlantNb(stock.getPlantNb());
        productReturn.setMaterialNb(oldStock.getMaterialNb());
        productReturn.setStatus(MaterialReturnStatusEnum.FINISH.value());
        productReturn.setBatchNb(oldStock.getBatchNb());
        productReturn.setExpireDate(oldStock.getExpireDate());
        productReturn.setMoveType(MoveTypeEnums.MATERIAL_RETURN.getCode());
        productReturn.setSsccNumber(sscc);
        productReturn.setAreaCode(areaVO.getCode());
        productReturn.setWareCode(areaVO.getWareCode());
        productReturn.setType(productReturnDTO.getType());
        productReturn.setQuantity(productReturnDTO.getQuantity());
//        productReturn.setCell(productReturnDTO.getCell());
//
        productReturnService.save(productReturn);



        productStockOperationService.addProductStockOperation(productReturn.getPlantNb(),productReturn.getQuantity(),productReturn.getSsccNumber(),productReturn.getMaterialNb(),stock.getFromProdOrder(), StockOperationType.IN.getCode());


    }

    @Override
    public void trans(ManualBinInDTO binInDTO) {
        String mesBarCode = binInDTO.getMesBarCode();
        String sscc = ProductQRCodeUtil.getSSCC(mesBarCode);
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        ProductStock stock = this.getOne(queryWrapper);


        ManualTransferOrder manualTransferOrder = new ManualTransferOrder();
        manualTransferOrder.setSourcePlantNb(stock.getPlantNb());
        manualTransferOrder.setSourceWareCode(stock.getWareCode());
        manualTransferOrder.setSourceAreaCode(stock.getAreaCode());
        manualTransferOrder.setSourceBinCode(stock.getBinCode());
//        manualTransferOrder.setTargetAreaCode(targetAreaCode);
        manualTransferOrder.setSsccNb(stock.getSsccNumber());
        manualTransferOrder.setMaterialNb(stock.getMaterialNb());
//        manualTransferOrder.setType(dto.getNormalType());
        manualTransferOrder.setMoveType(MoveTypeEnums.IN_TRANSFER.getCode());
        manualTransferOrder.setStatus(ManuTransStatusEnum.FINISH.code());
        manualTransferOrder.setMaterialProductType(1);

        if (binInDTO.getType() == 1) {
            manualTransferOrder.setTargetAreaCode(binInDTO.getActualCode());
            stock.setAreaCode(binInDTO.getActualCode());
            stock.setBinCode(null);
        } else {
            BinVO binVO = getBinVOByBinCode(binInDTO.getActualCode());

            manualTransferOrder.setTargetBinCode(binVO.getCode());
            manualTransferOrder.setTargetAreaCode(binVO.getAreaCode());
            stock.setAreaCode(binVO.getAreaCode());
            stock.setBinCode(binVO.getCode());
        }

        manualTransferOrderService.save(manualTransferOrder);

        userOperationLogService.insertUserOperationLog(MaterialType.PRODUCT.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PRODUCT_TRANS.getCode(), ProductQRCodeUtil.getSSCC(binInDTO.getMesBarCode()), manualTransferOrder.getMaterialNb());

        this.updateById(stock);
    }

    @Override
    public List<ProductReturnVO> returnList(ProductReturnDTO queryDTO) {

        return productReturnService.getReturnList(queryDTO);
    }

    @Override
    public void addSplit(SplitPalletDTO splitPallet) {
        String sourceSsccNb = splitPallet.getSourceSsccNb();
        LambdaQueryWrapper<ProductStock> stockQueryWrapper = new LambdaQueryWrapper<>();
        stockQueryWrapper.eq(ProductStock::getSsccNumber, sourceSsccNb);
        stockQueryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        stockQueryWrapper.last("limit 1");
        stockQueryWrapper.last("for update");
        ProductStock sourceStock = this.getOne(stockQueryWrapper);
        if (Objects.isNull(sourceStock)) {
            throw new ServiceException("SSCC: " + sourceSsccNb + ",无库存，不可拆托");
        }
        String newMesBarCode = splitPallet.getNewMesBarCode();

        if (splitPallet.getSplitQuantity() <= 0) {
            throw new ServiceException("拆托数不可以小于0");
        }


        if (newMesBarCode.length() > 55 && ProductQRCodeUtil.getSSCC(newMesBarCode).equals(splitPallet.getSourceSsccNb())) {
            throw new ServiceException("拆托SSCC不可以和源SSCC一致");
        } else if (newMesBarCode.length() <= 55 && newMesBarCode.equals(splitPallet.getSourceSsccNb())) {
            throw new ServiceException("拆托SSCC不可以和源SSCC一致");
        }


        String newSscc = ProductQRCodeUtil.getSSCC(newMesBarCode);


        //校验拆托数量
        Double splitQuantity = splitPallet.getSplitQuantity();
        if (sourceStock.getAvailableStock() <= splitQuantity) {
            throw new ServiceException("拆托数量不可以大于源库存可用数量");
        }


        SplitRecord splitRecord = new SplitRecord();
        splitRecord.setSplitQuantity(splitQuantity);
        splitRecord.setMaterialNb(sourceStock.getMaterialNb());
        splitRecord.setNewMesBarCode(newMesBarCode);
        splitRecord.setSsccNb(newMesBarCode.length() == 50 || newMesBarCode.contains(".") ? MesBarCodeUtil.getSSCC(newMesBarCode) : newMesBarCode);
        splitRecord.setSourceSscc(sourceSsccNb);
        splitRecord.setSourceTotalStock(sourceStock.getTotalStock());
        splitRecord.setWareCode(sourceStock.getWareCode());
        splitRecord.setType(1);//成品
        productSplitService.save(splitRecord);


        MdProductPackagingVO productVO = getProductVO(splitRecord.getMaterialNb());


//        productStockOperationService.addProductStockOperation(sourceStock.getPlantNb(), DoubleMathUtil.doubleMathCalculation(sourceStock.getTotalStock(), splitQuantity, "-") * productVO.getBoxSpecification(), sourceStock.getSsccNumber(), sourceStock.getMaterialNb(),
//                sourceStock.getBatchNb(), StockOperationType.IN.getCode());

        sourceStock.setTotalStock(DoubleMathUtil.doubleMathCalculation(sourceStock.getTotalStock(), splitQuantity, "-"));
        sourceStock.setAvailableStock(DoubleMathUtil.doubleMathCalculation(sourceStock.getTotalStock(), sourceStock.getFreezeStock(), "-"));
        sourceStock.setWholeFlag(StockWholeFlagEnum.NOT_WHOLE.code());
        this.updateById(sourceStock);


        ProductStock newStock = BeanConverUtil.conver(sourceStock, ProductStock.class);
        newStock.setTotalStock(splitRecord.getSplitQuantity());
        newStock.setAvailableStock(splitRecord.getSplitQuantity());
        newStock.setFreezeStock(Double.valueOf(0));
        newStock.setSsccNumber(ProductQRCodeUtil.getSSCC(splitRecord.getNewMesBarCode()));
        newStock.setId(null);
//        productStockOperationService.addProductStockOperation(newStock.getPlantNb(), newStock.getTotalStock() * productVO.getBoxSpecification(), newStock.getSsccNumber(), newStock.getMaterialNb(),
//                newStock.getBatchNb(), StockOperationType.IN.getCode());
        this.save(newStock);

        userOperationLogService.insertUserOperationLog(MaterialType.MATERIAL.getCode(), null, SecurityUtils.getUsername(), UserOperationType.PALLETSPLIT.getCode(), splitPallet.getSourceSsccNb(), newStock.getMaterialNb());

    }


    private MdProductPackagingVO getProductVO(String code) {
        R<MdProductPackagingVO> byCode = remoteProductService.getByCode(code);
        if (byCode == null || !byCode.isSuccess()) {
            throw new ServiceException("调用主数据获取成品失败");
        }
        return byCode.getData();
    }


}
