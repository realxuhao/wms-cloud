package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.service.IShippingTaskService;
import com.bosch.product.mapper.ShippingTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_task】的数据库操作Service实现
* @createDate 2023-03-16 14:55:20
*/
@Service
public class ShippingTaskServiceImpl extends ServiceImpl<ShippingTaskMapper, ShippingTask>
    implements IShippingTaskService {

    @Autowired
    private ShippingTaskMapper shippingTaskMapper;
    @Override
    public List<ShippingTask> selectAllByFields(ShippingTaskDTO dto) {
        return shippingTaskMapper.selectAllByFields(dto);
    }
}




