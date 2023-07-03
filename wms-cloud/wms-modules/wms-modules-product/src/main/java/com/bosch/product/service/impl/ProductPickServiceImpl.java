package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ProductPick;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.EditBinDownQuantityDTO;
import com.bosch.product.api.domain.dto.ProductPickDTO;
import com.bosch.product.api.domain.enumeration.ProductPickEnum;
import com.bosch.product.api.domain.vo.ProductPickBinDownVO;
import com.bosch.product.api.domain.vo.ProductPickVO;
import com.bosch.product.mapper.ProductPickMapper;
import com.bosch.product.service.IProductPickService;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    public List<ProductPickVO> list(ProductPickDTO queryDTO) {
        return productPickMapper.list(queryDTO);
    }

    @Override
    public void batchCancel(List<Long> idList) {
        List<ProductPick> productPicks = this.listByIds(idList);
        productPicks.stream().forEach(item -> {
            if (item.getStatus() != ProductPickEnum.WAITING_ISSUE.code() || item.getStatus().equals(ProductPickEnum.WAITTING_DOWN.code())) {
                throw new ServiceException("该状态下不可以取消" + ProductPickEnum.getDesc(item.getStatus()));
            }
            item.setStatus(ProductPickEnum.CANCEL.code());
        });
        this.updateBatchById(productPicks);
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
        if (productPick.getStatus() == ProductPickEnum.WAITTING_DOWN.code()) {
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
            throw new ServiceException("该条数据状态为:"+ProductPickEnum.getDesc(pick.getStatus())+",不可修改下架量");
        }

        String sscc = pick.getSscc();
        Double diff = pick.getBinDownQuantity()- dto.getNewBinDownQuantity();
        LambdaQueryWrapper<ProductStock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductStock::getSsccNumber,sscc);
        queryWrapper.eq(ProductStock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());

        ProductStock productStock = productStockService.getOne(queryWrapper);
        if (productStock==null){
            throw new ServiceException("该托目前在库存中已经不存在");
        }
        productStock.setAvailableStock(productStock.getAvailableStock()+diff);
        productStock.setTotalStock(productStock.getTotalStock()+diff);

        pick.setBinDownQuantity(dto.getNewBinDownQuantity());

        this.updateById(pick);
        productStockService.updateById(productStock);


    }


}
