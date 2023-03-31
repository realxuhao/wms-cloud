package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.binin.api.domain.dto.IQCChangeStatusDTO;
import com.bosch.binin.api.domain.dto.IQCManagementQueryDTO;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.domain.vo.BinVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.ProductWareShift;
import com.bosch.product.api.domain.dto.ProductBinInDTO;
import com.bosch.product.api.domain.dto.ProductIQCManagementQueryDTO;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.enumeration.ProductStockBinInEnum;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.mapper.ProductStockMapper;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.ProductQRCodeUtil;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.ehcache.shadow.org.terracotta.statistics.derived.histogram.StripedHistogram;
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

        AreaVO areaVO = getAreaByType(SecurityUtils.getWareCode(), AreaTypeEnum.PRO_TEMP.getCode());

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
        stock.setQualityStatus(QualityStatusEnums.USE.getCode());
        stock.setProductionDate(receive.getProductionDate());
        stock.setUnit(receive.getUnit());
        stock.setBinInFlag(ProductStockBinInEnum.NONE.code());

        stockMapper.insert(stock);

    }

    @Override
    public void generateStockByProductWareShifts(List<ProductWareShift> productWareShiftList) {
        AreaVO areaVO = getAreaByType(SecurityUtils.getWareCode(), AreaTypeEnum.PRO_TEMP.getCode());
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
            throw new ServiceException("没有类型为" + AreaTypeEnum.getDescByCode(AreaTypeEnum.PRO_TEMP.getCode()) + "的区域");
        }
        return areaList.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductStock binIn(ProductBinInDTO binInDTO) {
        String qrCode = binInDTO.getQrCode();
        String sscc = ProductQRCodeUtil.getSSCC(qrCode);
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
        stockWrapper.eq(ProductStock::getBinCode,binCode);
        stockWrapper.eq(ProductStock::getDeleteFlag,DeleteFlagStatus.FALSE.getCode());
        stockWrapper.eq(ProductStock::getBinInFlag,ProductStockBinInEnum.FINISH.code());
        stockWrapper.last("limit 1");
        ProductStock stock = this.getOne(stockWrapper);
        if (!Objects.isNull(stock)){
            throw new ServiceException("该库位"+binCode+"已经被占用！");
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


}
