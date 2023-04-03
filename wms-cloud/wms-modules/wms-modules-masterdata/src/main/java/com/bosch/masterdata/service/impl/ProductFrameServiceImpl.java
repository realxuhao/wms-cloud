package com.bosch.masterdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.masterdata.api.domain.ProductFrame;
import com.bosch.masterdata.api.domain.vo.ProductFrameVO;
import com.bosch.masterdata.mapper.ProductFrameMapper;
import com.bosch.masterdata.service.IProductFrameService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<ProductFrameVO> getBinInRule(String materialCode, String wareCode) {
       return productFrameMapper.getBinInRule(materialCode,wareCode);
    }
}
