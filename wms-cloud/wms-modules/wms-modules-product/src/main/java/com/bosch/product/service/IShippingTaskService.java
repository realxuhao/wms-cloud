package com.bosch.product.service;

import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.product.api.domain.dto.ListPair;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.service.impl.ShippingPlanServiceImpl;
import javafx.util.Pair;

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

    ListPair genTask(List<ShippingPlan> list);

    boolean updateStatus(Long[] ids, Integer status);

    boolean checkHistory(Long taskId);

    boolean checkOneHistory(ShippingHistoryDTO dto);
    List<ShippingTaskVO> getDashboard(ShippingTaskDTO dto);
}
