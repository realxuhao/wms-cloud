package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.StockTakePlan;
import com.bosch.product.service.IStockTakePlanService;
import com.bosch.product.mapper.StockTakePlanMapper;
import org.springframework.stereotype.Service;

/**
* @author GUZ1CGD4
* @description 针对表【stock_take_plan】的数据库操作Service实现
* @createDate 2023-04-14 13:40:35
*/
@Service
public class StockTakePlanServiceImpl extends ServiceImpl<StockTakePlanMapper, StockTakePlan>
    implements IStockTakePlanService {

}




