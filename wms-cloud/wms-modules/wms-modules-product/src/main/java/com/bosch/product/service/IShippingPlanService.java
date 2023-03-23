package com.bosch.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.service.impl.ShippingPlanServiceImpl;

import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_plan】的数据库操作Service
* @createDate 2023-03-15 16:35:14
*/
public interface IShippingPlanService extends IService<ShippingPlan> {

    List<ShippingPlan> getList(ShippingPlanDTO dto);


    /**
     * 根据taskid获取plan
     * @param id
     * @return
     */
    List<ShippingPlanVO> getPlanList(Long id);


    /**
     * 更新
     * @param dto
     * @return
     */
    Integer updatePlan(MdProductPackagingDTO dto);

    /**
     * 删除
     * @param ids
     * @return
     */
    Integer deletePlan(Long[] ids);
}
