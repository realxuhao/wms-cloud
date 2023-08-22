package com.bosch.product.mapper;

import com.bosch.product.api.domain.ShippingHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.dto.ShippingDTO;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.vo.ShippingVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_history】的数据库操作Mapper
* @createDate 2023-03-20 10:03:19
* @Entity com.bosch.product.api.domain.ShippingHistory
*/
public interface ShippingHistoryMapper extends BaseMapper<ShippingHistory> {

   Integer deleteHistoryByTaskId (Long id);

   List<ShippingHistory> searchAllFields(ShippingHistoryDTO dto);

   List<ShippingVO>selectShipping(ShippingDTO dto);

   List<ShippingPlan> selectPlanByTask (Long id);
}




