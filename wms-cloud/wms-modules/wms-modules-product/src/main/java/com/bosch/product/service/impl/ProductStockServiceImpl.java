package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.ManualTransferOrder;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.ManualBinInDTO;
import com.bosch.binin.api.enumeration.ManuTransStatusEnum;
import com.bosch.binin.api.enumeration.MaterialReturnStatusEnum;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.*;
import com.bosch.product.api.domain.dto.*;
import com.bosch.product.api.domain.enumeration.ProductStockBinInEnum;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.mapper.ProductStockMapper;
import com.bosch.product.service.IManualTransferOrderService;
import com.bosch.product.service.IProductReturnService;
import com.bosch.product.service.IProductStockAdjustService;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.MesBarCodeUtil;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private RemoteMasterDataService remoteMasterDataService;


    @Autowired
    private IProductStockAdjustService stockAdjustService;

    @Autowired
    private IProductReturnService productReturnService;

    @Autowired
    private IManualTransferOrderService manualTransferOrderService;

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
        stock.setTotalStock(receive.getQuantity());
        stock.setFreezeStock((double) 0);
        stock.setAvailableStock(receive.getQuantity());
        stock.setFromProdOrder(receive.getFromProdOrder());
        stock.setQualityStatus(QualityStatusEnums.WAITING_QUALITY.getCode());
        stock.setProductionDate(receive.getProductionDate());
        stock.setUnit(receive.getUnit());
        stock.setBinInFlag(ProductStockBinInEnum.NONE.code());

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

        if (stockEditDTO.getType() == 0||stockEditDTO.getType() == 1) {//质检取样和取样

            if (stockEditDTO.getStockUse() > stock.getAvailableStock()) {
                throw new ServiceException("领用数量不能大于可用数量");
            }
            stock.setAvailableStock(stock.getAvailableStock() - stockEditDTO.getStockUse());
            stock.setTotalStock(stock.getTotalStock() - stockEditDTO.getStockUse());
            stock.setFreezeStock(stock.getTotalStock() - stock.getAvailableStock());
            if (stock.getAvailableStock()==Double.valueOf(0)) {
                stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            }
            this.updateById(stock);



        } else if (stockEditDTO.getType() == 2) {//报废
            if (stock.getFreezeStock() > 0) {
                throw new ServiceException("该库存存在执行任务，暂时不可以报废");
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
            List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType() == AreaTypeEnum.DISCARD.getCode()).collect(Collectors.toList());
            if (StringUtils.isEmpty(areaList)) {
                log.error("没有类型为" + "报废" + "的区域");
                throw new ServiceException("没有类型为报废的区域");
            }
            AreaVO areaVO = areaList.get(0);
            stock.setAreaCode(areaVO.getCode());
            stock.setBinCode(null);
            stock.setQualityStatus(QualityStatusEnums.BLOCK.getCode());
            this.updateById(stock);

        }else if (stockEditDTO.getType() == 3){//整托出库
            if (stock.getFreezeStock() > 0) {
                throw new ServiceException("该库存存在执行任务，暂时不可以整托出库");
            }
            stock.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
            this.updateById(stock);
        } else {
            if (stockEditDTO.getSsccNumber() == null || stockEditDTO.getAvailableStock() == null || stockEditDTO.getFreezeStock() == null || stockEditDTO.getTotalStock() == null) {
                throw new ServiceException("所有参数都不能为空");
            }
            if (!stockEditDTO.getTotalStock().equals(stockEditDTO.getFreezeStock() + stockEditDTO.getAvailableStock())) {
                throw new ServiceException("总库存必须等于冻结库存+可用库存");
            }
            stock.setAvailableStock(stockEditDTO.getAvailableStock());
            stock.setFreezeStock(stockEditDTO.getFreezeStock());
            stock.setTotalStock(stockEditDTO.getTotalStock());
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
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber,sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = this.getOne(queryWrapper);
        if (productStock!=null){
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

        ProductStock stock = new ProductStock();
        //添加为销售库存
        stock.setPlantNb("7761");
        stock.setWareCode(SecurityUtils.getWareCode());
        //获取退获区域
        stock.setAreaCode(areaVO.getCode());

//        stock.setMaterialNb(ProductQRCodeUtil.);
//        stock.setBatchNb(receive.getBatchNb());
//        stock.setExpireDate(DateUtils.parseDate(receive.getExpireDate()));
        stock.setTotalStock(productReturnDTO.getQuantity());
        stock.setFreezeStock((double) 0);
        stock.setAvailableStock(productReturnDTO.getQuantity());
        stock.setQualityStatus(QualityStatusEnums.BLOCK.getCode());
        stock.setProductionDate(ProductQRCodeUtil.getProductionDate(qrCode));
//        stock.setUnit(receive.getUnit());
        stock.setBinInFlag(ProductStockBinInEnum.NONE.code());

        this.save(stock);

        ProductReturn productReturn = new ProductReturn();
//        productReturn.setMaterialNb(stock.getMaterialNb());
//        productReturn.setStatus(MaterialReturnStatusEnum.WAITING_CONFIRM.value());
//        productReturn.setBatchNb(stock.getBatchNb());
//        productReturn.setExpireDate(MesBarCodeUtil.getExpireDate(mesBarCode));
//        productReturn.setMoveType(MoveTypeEnums.MATERIAL_RETURN.getCode());
//        productReturn.setSsccNumber(MesBarCodeUtil.getSSCC(mesBarCode));
//        productReturn.setAreaCode(areaCode);
//        productReturn.setWareCode(wareCode);
//        productReturn.setType(type);
//        productReturn.setQuantity(quantity);
//        productReturn.setCell(productReturnDTO.getCell());
//
        productReturnService.save(productReturn);






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

        this.updateById(stock);
    }

}
