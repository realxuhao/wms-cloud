package com.bosch.masterdata.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.*;
import com.bosch.masterdata.api.domain.dto.MaterialBinDTO;
import com.bosch.masterdata.api.domain.dto.ProductFrameDTO;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.mapper.FrameMapper;
import com.bosch.masterdata.mapper.MdProductPackagingMapper;
import com.bosch.masterdata.mapper.ProductFrameMapper;
import com.bosch.masterdata.service.IProductFrameService;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-03 13:34
 **/
@Service
public class ProductFrameServiceImpl extends ServiceImpl<ProductFrameMapper, ProductFrame> implements IProductFrameService {

    @Autowired
    private ProductFrameMapper productFrameMapper;
    @Autowired
    private FrameMapper frameMapper;

    @Autowired
    private MdProductPackagingMapper productPackagingMapper;

    @Override
    public List<ProductFrameVO> getBinInRule(String materialCode, String wareCode) {
       return productFrameMapper.getBinInRule(materialCode,wareCode);
    }

        public boolean validOne(ProductFrameDTO dto) {

        LambdaQueryWrapper<ProductFrame> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ProductFrame::getFrameTypeCode, dto.getFrameTypeCode());
        lambdaQueryWrapper.eq(ProductFrame::getMaterialId, dto.getMaterialId());
        Integer integer = productFrameMapper.selectCount(lambdaQueryWrapper);
        if (integer > 0) {
            return false;
        }

        return true;
    }

    @Override
    public int insertProductFrame(ProductFrame productFrame) {
//        LambdaQueryWrapper<ProductFrame> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.eq(ProductFrame::getId,productFrame.getMaterialId());
//        ProductFrame product = productFrameMapper.selectOne(lambdaQueryWrapper);
//        if(productFrame==null){
//            throw  new ServiceException("通过物料id未找到物料code");
//        }
        productFrame.setMaterialCode("");//product.getCode()
        productFrame.setCreateTime(DateUtils.getNowDate());
        productFrame.setCreateBy(SecurityUtils.getUsername());
        return productFrameMapper.insertProductFrame(productFrame);
    }

    @Override
    public ProductFrameVO selectProductFrameById(Long id) {
        ProductFrameDTO productFrameDTO=new ProductFrameDTO();
        productFrameDTO.setId(id);
        List<ProductFrameVO> productFrameVOS = productFrameMapper.selectProductFrameList(productFrameDTO);
        if (CollectionUtils.isNotEmpty(productFrameVOS)){
            return productFrameVOS.get(0);
        }
        return null;
    }

    @Override
    public List<ProductFrameVO> selectProductFrameList(ProductFrameDTO productFrameDTO) {
        return productFrameMapper.selectProductFrameList(productFrameDTO);
    }

    @Override
    public int updateProductFrame(ProductFrame productFrame) {
        productFrame.setUpdateTime(DateUtils.getNowDate());
        return productFrameMapper.updateProductFrame(productFrame);
    }

    @Override
    public int deleteProductFrameByIds(Long[] ids) {
        return productFrameMapper.deleteProductFrameByIds(ids);
    }

    public boolean validList(List<ProductFrameDTO> dtos) {
        for (ProductFrameDTO dto : dtos) {
            LambdaQueryWrapper<ProductFrame> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ProductFrame::getFrameTypeCode, dto.getFrameTypeCode());
            lambdaQueryWrapper.eq(ProductFrame::getMaterialCode, dto.getMaterialCode());
            ProductFrame productFrame = productFrameMapper.selectOne(lambdaQueryWrapper);
            if (productFrame != null) {
                return false;
            }
        }
        return true;
    }


    public List<ProductFrameDTO> setValue(List<ProductFrameDTO> dtos) {
        //获取集合

        List<String> products =
                dtos.stream().map(ProductFrameDTO::getMaterialCode).collect(Collectors.toList());
        //获取map
        Map<String, Long> materialsMap = getTypeMap(products, 2);
        //绑定id
        dtos.forEach(x -> {
            if (materialsMap.get(x.getMaterialCode()) == null) {
                throw new ServiceException("包含不存在的成品代码");
            }
            x.setMaterialId(materialsMap.get(x.getMaterialCode()));
        });
        return dtos;
    }

    public Map<String, Long> getTypeMap(List<String> codes, int i) {
        Map<String, Long> collect = new HashMap<>();
        if (i == 1) {
            LambdaQueryWrapper<Frame> queryWrapper = new LambdaQueryWrapper<Frame>();
            queryWrapper.in(Frame::getCode, codes);
            List<Frame> frames = frameMapper.selectList(queryWrapper);
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(frames)) {
                collect = frames.stream().collect(Collectors.toMap(Frame::getCode, Frame::getId));
            }
            return collect;
        } else {
            LambdaQueryWrapper<ProductPackaging> queryWrapper = new LambdaQueryWrapper<ProductPackaging>();
            queryWrapper.in(ProductPackaging::getProductNo, codes);
            queryWrapper.eq(ProductPackaging::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
            List<ProductPackaging> productPackagings = productPackagingMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(productPackagings)) {
                collect = productPackagings.stream().collect(Collectors.toMap(ProductPackaging::getProductNo, ProductPackaging::getId));
            }
            return collect;
        }
    }
}
