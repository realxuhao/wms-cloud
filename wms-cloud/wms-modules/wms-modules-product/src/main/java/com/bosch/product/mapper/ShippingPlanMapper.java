package com.bosch.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_plan】的数据库操作Mapper
* @createDate 2023-03-15 16:35:14
* @Entity com.bosch.product.domain.ShippingPlan
*/
public interface ShippingPlanMapper extends BaseMapper<ShippingPlan> {
    /**
     * 删除
     * @param ids
     * @return
     */
    public Integer deletePlan(Long[] ids);

    /**
     * 更新
     * @param
     * @return
     */
    public Integer updatePlan(ShippingPlanDTO dto);
}




