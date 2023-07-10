package com.ruoyi.common.log.mapper;

import com.bosch.system.api.domain.ProductStockDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.system.api.domain.vo.ProductStockDetailVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【product_stock_detail(库存期初期末（job执行）)】的数据库操作Mapper
* @createDate 2023-07-07 09:44:16
* @Entity com.bosch.system.api.domain.ProductStockDetail
*/
@Mapper
public interface ProductStockDetailMapper extends BaseMapper<ProductStockDetail> {

    List<ProductStockDetailVO> getProductStock();
}




