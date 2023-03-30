package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.domain.dto.WareShiftQueryDTO;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.enumeration.ProductWareShiftEnum;
import com.bosch.product.api.domain.vo.ProductWareShiftVO;
import com.bosch.product.mapper.ProductWareShiftMapper;
import com.bosch.product.mapper.TranshipmentOrderMapper;
import com.bosch.product.service.IProductStockService;
import com.bosch.product.service.IProductWareShiftService;
import com.bosch.product.service.ITranshipmentOrderService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.MoveTypeEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.sun.corba.se.spi.ior.IORTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 16:52
 **/
@Service
public class ProductWareShiftServiceImpl extends ServiceImpl<ProductWareShiftMapper, ProductWareShift> implements IProductWareShiftService {


    @Autowired
    private IProductStockService stockService;

    @Autowired
    private ProductWareShiftMapper wareShiftMapper;

    @Autowired
    private RemoteBinInService remoteBinInService;


    @Autowired
    private ITranshipmentOrderService transhipmentOrderService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addByStockId(Long stockId) {
        ProductStock productStock = stockService.getById(stockId);
        if (Objects.isNull(productStock)) {
            throw new ServiceException("库存id" + stockId + "对应的库存数据不存在");
        }
        if (!productStock.getFreezeStock().equals(Double.valueOf(0))) {
            throw new ServiceException("该库存有冻结库存，暂时不能移库");
        }

        productStock.setFreezeStock(productStock.getTotalStock());
        productStock.setAvailableStock(Double.valueOf(0));
        stockService.updateById(productStock);

        //新增移库
        ProductWareShift wareShift = ProductWareShift.builder().sourcePlantNb(productStock.getPlantNb())
                .sourceWareCode(productStock.getWareCode())
                .sourceAreaCode(productStock.getAreaCode())
                .sourceBinCode(productStock.getBinCode())
                .quantity(productStock.getTotalStock())
                .moveType(MoveTypeEnums.WARE_SHIFT.getCode())
                .ssccNb(productStock.getSsccNumber())
                .materialNb(productStock.getMaterialNb())
                .batchNb(productStock.getBatchNb())
                .expireDate(productStock.getExpireDate())
                .productionDate(productStock.getProductionDate())
                .unit(productStock.getUnit())
                .status(ProductWareShiftEnum.WAITTING_SHIPPING.code())
                .build();
        wareShiftMapper.insert(wareShift);


    }

    @Override
    public void cancel(Long id) {
        ProductWareShift wareShift = wareShiftMapper.selectById(id);
        if (Objects.isNull(wareShift)) {
            throw new ServiceException("该id:" + id + "对应的移库任务不存在");
        }
        if (!wareShift.getStatus().equals(ProductWareShiftEnum.WAITTING_SHIPPING.code())) {
            throw new ServiceException("该任务对应状态为:" + ProductWareShiftEnum.getDesc(wareShift.getStatus()) + ", 不可取消");
        }

        wareShift.setStatus(ProductWareShiftEnum.CANCEL.code());

        //回滚库存
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber, wareShift.getSsccNb());
        queryWrapper.eq(ProductStock::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductStock productStock = stockService.getOne(queryWrapper);
        if (!Objects.isNull(productStock)) {
            productStock.setFreezeStock(Double.valueOf(0));
            productStock.setAvailableStock(productStock.getTotalStock());
            stockService.updateById(productStock);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ship(List<String> ssccList, String carNb) {

        LambdaQueryWrapper<ProductWareShift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductWareShift::getSsccNb, ssccList)
                .eq(ProductWareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<ProductWareShift> productWareShifts = this.list(queryWrapper);
        List<TranshipmentOrder> transhipmentOrderList = new ArrayList<>();
        //获取next trans order
        R<String> nextOrderNbR = remoteBinInService.getNextOrderNb();
        if (nextOrderNbR == null || !nextOrderNbR.isSuccess()) {
            throw new ServiceException("请求获取发运单号失败");
        }
        String nextOrderNb = nextOrderNbR.getData();
        productWareShifts.forEach(item -> {
            if (!item.getStatus().equals(ProductWareShiftEnum.WAITTING_SHIPPING.code())) {
                throw new ServiceException("sscc:" + item.getSsccNb() + "对应任务状态为" + ProductWareShiftEnum.getDesc(item.getStatus()));
            }
            TranshipmentOrder transhipmentOrder = new TranshipmentOrder();
            transhipmentOrder.setOrderNumber(nextOrderNb);
            transhipmentOrder.setSsccNumber(item.getSsccNb());
            transhipmentOrder.setMaterialCode(item.getMaterialNb());
            transhipmentOrder.setProductWareShiftId(item.getId());
            transhipmentOrder.setCarNb(carNb);
            transhipmentOrder.setStatus(0);
            transhipmentOrderList.add(transhipmentOrder);

            item.setStatus(ProductWareShiftEnum.WAITTING_RECEIVING.code());

        });

        //修改移库状态
        this.updateBatchById(productWareShifts);


        if (CollectionUtils.isEmpty(transhipmentOrderList)) {
            R saveR = remoteBinInService.saveBatch(transhipmentOrderList);
            if (saveR == null || !saveR.isSuccess()) {
                throw new ServiceException("远程调用生成转运单失败，请重试");
            }
        }


    }

    @Override
    public List<ProductWareShiftVO> list(WareShiftQueryDTO queryDTO) {
        return wareShiftMapper.list(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receive(String qrCode) {
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        LambdaQueryWrapper<TranshipmentOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TranshipmentOrder::getSsccNumber, sscc);
        queryWrapper.eq(TranshipmentOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        TranshipmentOrder transhipmentOrder = transhipmentOrderService.getOne(queryWrapper);
        if (transhipmentOrder == null || transhipmentOrder.getStatus() == 1) {
            throw new ServiceException("该托不在转运中。");
        }
        String orderNumber = transhipmentOrder.getOrderNumber();
        LambdaQueryWrapper<TranshipmentOrder> transhipmentQueryWrapper = new LambdaQueryWrapper<>();
        transhipmentQueryWrapper.eq(TranshipmentOrder::getOrderNumber, orderNumber);
        transhipmentQueryWrapper.eq(TranshipmentOrder::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        List<TranshipmentOrder> transhipmentOrderList = transhipmentOrderService.list(queryWrapper);
        transhipmentOrderList.forEach(item -> item.setStatus(1));
        transhipmentOrderService.updateBatchById(transhipmentOrderList);

        //更新移库状态
        List<String> ssccList = transhipmentOrderList.stream().map(TranshipmentOrder::getSsccNumber).collect(Collectors.toList());
        LambdaQueryWrapper<ProductWareShift> wareShiftWrapper = new LambdaQueryWrapper<>();
        wareShiftWrapper.in(ProductWareShift::getSsccNb, ssccList);
        wareShiftWrapper.eq(ProductWareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wareShiftWrapper.eq(ProductWareShift::getStatus, ProductWareShiftEnum.WAITTING_RECEIVING.code());
        List<ProductWareShift> wareShifts = this.list(wareShiftWrapper);
        AreaVO areaVO = stockService.getAreaByType(SecurityUtils.getWareCode(), AreaTypeEnum.PRO_TEMP.getCode());
        wareShifts.forEach(item -> {
            item.setStatus(ProductWareShiftEnum.WAITTING_BIN_IN.code());
            item.setTargetWareCode(SecurityUtils.getWareCode());
            item.setTargetAreaCode(areaVO.getCode());
            item.setTargetPlant(areaVO.getPlantNb());
        });
        this.updateBatchById(wareShifts);

        //库存修改
        stockService.generateStockByProductWareShifts(wareShifts);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wareShiftBinIn(ProductBinInDTO binInDTO) {
        String qrCode = binInDTO.getQrCode();
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        String binCode = binInDTO.getBinCode();
        String recommendBinCode = binInDTO.getRecommendBinCode();

        //移库任务完成
        LambdaQueryWrapper<ProductWareShift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductWareShift::getSsccNb, sscc);
        queryWrapper.eq(ProductWareShift::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.ne(ProductWareShift::getStatus, ProductWareShiftEnum.FINISH.code());
        queryWrapper.last("limit 1");
        ProductWareShift productWareShift = this.getOne(queryWrapper);

        if (productWareShift == null) {
            throw new ServiceException("没有该SSCC：" + sscc + "对应的任务");
        }
        if (!productWareShift.getSsccNb().equals(ProductWareShiftEnum.WAITTING_BIN_IN.code())) {
            throw new ServiceException("该SSCC：" + sscc + "对应的任务状态为:" + ProductWareShiftEnum.getDesc(productWareShift.getStatus()) + ",不可以上架");
        }

        //执行上架
        ProductStock productStock = stockService.binIn(binInDTO);


        productWareShift.setTargetPlant(productStock.getPlantNb());
        productWareShift.setTargetWareCode(productStock.getWareCode());
        productWareShift.setTargetAreaCode(productStock.getAreaCode());
        productWareShift.setTargetBinCode(productStock.getBinCode());
        productWareShift.setStatus(ProductWareShiftEnum.FINISH.code());

        this.updateById(productWareShift);


    }
}
