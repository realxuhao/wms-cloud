package com.bosch.product.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ShippingHistory;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingHistoryVO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.service.IShippingHistoryService;
import com.bosch.product.mapper.ShippingHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GUZ1CGD4
 * @description 针对表【shipping_history】的数据库操作Service实现
 * @createDate 2023-03-20 10:03:19
 */
@Service
public class ShippingHistoryServiceImpl extends ServiceImpl<ShippingHistoryMapper, ShippingHistory>
        implements IShippingHistoryService {

    @Autowired
    private ShippingHistoryMapper shippingHistoryMapper;

    @Override
    public Integer deleteHistoryByTaskId(Long id) {
        return shippingHistoryMapper.deleteHistoryByTaskId(id);
    }

    @Override
    public List<ShippingHistory> searchAllFields(ShippingHistoryDTO dto) {
        return shippingHistoryMapper.searchAllFields(dto);
    }


}




