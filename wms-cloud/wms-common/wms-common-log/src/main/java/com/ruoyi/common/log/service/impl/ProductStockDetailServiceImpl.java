package com.ruoyi.common.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.system.api.domain.ProductStockDetail;
import com.bosch.system.api.domain.vo.ProductStockDetailVO;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import com.ruoyi.common.log.mapper.ProductStockDetailMapper;
import com.ruoyi.common.log.service.IProductStockDetailService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【product_stock_detail(库存期初期末（job执行）)】的数据库操作Service实现
* @createDate 2023-07-07 09:44:16
*/
@Service
public class ProductStockDetailServiceImpl extends ServiceImpl<ProductStockDetailMapper, ProductStockDetail>
    implements IProductStockDetailService {


    @Autowired
    private ProductStockDetailMapper productStockDetailMapper;

    public void genProStock(){

            //查询每日库存
            List<ProductStockDetailVO> productStock = productStockDetailMapper.getProductStock();
            List<ProductStockDetail> productStockDetails = BeanConverUtil.converList(productStock, ProductStockDetail.class);
            //保存每日库存
            boolean b = this.saveBatch(productStockDetails);

    }
}




