package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.RemoteMasterDataService;
import com.bosch.masterdata.api.domain.vo.AreaVO;
import com.bosch.masterdata.api.enumeration.AreaTypeEnum;
import com.bosch.product.api.domain.ProductReceive;
import com.bosch.product.api.domain.ProductStock;
import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductReceiveVO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.mapper.ProductStockMapper;
import com.bosch.product.service.IProductStockService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.QualityStatusEnums;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //查询区域列表
        //根据areaType查询区域
        R<List<AreaVO>> areaListR = remoteMasterDataService.getByWareCode(SecurityUtils.getWareCode());
        if (areaListR == null || !areaListR.isSuccess()) {
            throw new ServiceException("调用主数据服务查询区域列表失败");
        }
        if (StringUtils.isEmpty(areaListR.getData())) {
            throw new ServiceException("没有区域，请维护主数据");
        }
        List<AreaVO> areaVOList = areaListR.getData();
        List<AreaVO> areaList = areaVOList.stream().filter(item -> item.getAreaType() == AreaTypeEnum.PRO_TEMP.getCode()).collect(Collectors.toList());
        if (StringUtils.isEmpty(areaList)) {
            throw new ServiceException("没有类型为" + AreaTypeEnum.getDescByCode(AreaTypeEnum.PRO_TEMP.getCode()) + "的区域");
        }
        AreaVO areaVO = areaList.get(0);

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

        stockMapper.insert(stock);

    }

    @Override
    public List<ProductStockVO> list(ProductStockQueryDTO stockQueryDTO) {
        return stockMapper.list(stockQueryDTO);
    }


}
