package com.bosch.product.service;

import com.bosch.product.api.domain.ShippingHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.ShippingDTO;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingHistoryVO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.api.domain.vo.ShippingVO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_history】的数据库操作Service
* @createDate 2023-03-20 10:03:19
*/
public interface IShippingHistoryService extends IService<ShippingHistory> {

    Integer deleteHistoryByTaskId (Long id);

    List<ShippingHistory> searchAllFields(ShippingHistoryDTO dto);

    List<ShippingVO>selectShipping(ShippingDTO dto);

    Integer deleteNewestByTaskId (Long id);
}
