package com.bosch.product.service;

import com.bosch.product.api.domain.ShippingTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_task】的数据库操作Service
* @createDate 2023-03-16 14:55:20
*/
public interface IShippingTaskService extends IService<ShippingTask> {

    public List<ShippingTask> selectAllByFields(ShippingTaskDTO dto);

    /**
     * 删除
     * @param ids
     * @return
     */
    Integer deleteShippingTask(Long[] ids);
}
