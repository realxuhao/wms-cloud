package com.bosch.product.mapper;

import com.bosch.product.api.domain.ShippingTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_task】的数据库操作Mapper
* @createDate 2023-03-16 14:55:20
* @Entity com.bosch.product.api.domain.ShippingTask
*/
public interface ShippingTaskMapper extends BaseMapper<ShippingTask> {

    public List<ShippingTask>  selectAllByFields(ShippingTaskDTO dto);
}




