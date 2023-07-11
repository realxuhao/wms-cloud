package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteProductService;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.dto.ProductReceiveDTO;
import com.bosch.product.api.domain.dto.ProductReceiveQueryDTO;
import com.bosch.product.api.domain.enumeration.ProductReceiveEnum;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.mapper.ProductReceiveMapper;
import com.bosch.product.service.IProductReceiveService;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-03-29 11:20
 **/
@Service
public class ProductReceiveServiceImpl extends ServiceImpl<ProductReceiveMapper, ProductReceive> implements IProductReceiveService {

    @Autowired
    private ProductReceiveMapper receiveMapper;

    @Autowired
    private IProductStockService stockService;

    @Autowired
    private RemoteProductService remoteProductService;

    @Override
    public List<ProductReceiveVO> list(ProductReceiveQueryDTO queryDTO) {
        return receiveMapper.list(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receive(String qrCode) {
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
        Date productionDate = ProductQRCodeUtil.getProductionDate(qrCode);
        String batchNb = ProductQRCodeUtil.getBatchNb(qrCode);
        LambdaQueryWrapper<ProductReceive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductReceive::getSsccNumber, sscc);
        queryWrapper.eq(ProductReceive::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        ProductReceive productReceive = receiveMapper.selectOne(queryWrapper);
        if (Objects.isNull(productReceive)) {
            throw new ServiceException("该sscc码:" + sscc + "没有对应的入库任务");
        }
        if (!productReceive.getStatus().equals(ProductReceiveEnum.WAAITTING_RECEIVE.code())) {
            throw new ServiceException("该sscc" + sscc + "码对应入库任务状态为" + ProductReceiveEnum.getDesc(productReceive.getStatus()) + ",不可入库");
        }
        productReceive.setWareCode(SecurityUtils.getWareCode());
        productReceive.setBatchNb(batchNb);
        productReceive.setProductionDate(productionDate);
        productReceive.setStatus(ProductReceiveEnum.RECEIVED.code());
        receiveMapper.updateById(productReceive);
        //上架到成品存储区
        stockService.generateStockByReceive(productReceive);
    }

    @Override
    public void delete(Long id) {

        ProductReceive productReceive = receiveMapper.selectById(id);
        if (Objects.isNull(productReceive)) {
            throw new ServiceException("id有误，不存在id为" + id + "数据");
        }

        if (!ProductReceiveEnum.WAAITTING_RECEIVE.code().equals(productReceive.getStatus())) {
            throw new ServiceException("状态为:" + ProductReceiveEnum.getDesc(productReceive.getStatus()) + ",不可以删除");
        }

        productReceive.setDeleteFlag(DeleteFlagStatus.TRUE.getCode());
        receiveMapper.updateById(productReceive);

    }

    @Override
    public ProductReceive getProductReceiveVO(String sscc) {
        LambdaQueryWrapper<ProductReceive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductReceive::getSsccNumber,sscc);
        queryWrapper.eq(ProductReceive::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }
    @Override
    public boolean validList(List<ProductReceiveDTO> dtos) {
        //校验料号是否存在。
        List<String> codeList = dtos.stream().map(ProductReceiveDTO::getMaterialNb).collect(Collectors.toList());
        R<List<String>> notExistCodeListR = remoteProductService.getNotExistCodeList(codeList);
        if (notExistCodeListR==null|| !notExistCodeListR.isSuccess()){
            throw new ServiceException("调用成品主数据失败");
        }
        List<String> data = notExistCodeListR.getData();
        Set<String> set= new HashSet<>(data);
        if (!CollectionUtils.isEmpty(set)){
            throw new ServiceException("以下成品料号主数据中不存在："+set);
        }
        return receiveMapper.validateRecord(dtos) > 0;
    }
}
