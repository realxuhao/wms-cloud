package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.mapper.ShippingTaskMapper;
import com.bosch.product.service.IShippingPlanService;
import com.bosch.product.mapper.ShippingPlanMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.StatusEnums;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author GUZ1CGD4
* @description 针对表【shipping_plan】的数据库操作Service实现
* @createDate 2023-03-15 16:35:14
*/
@Service
public class ShippingPlanServiceImpl extends ServiceImpl<ShippingPlanMapper, ShippingPlan>
    implements IShippingPlanService {
    public class Pair {
        public List<ShippingTask> shippingTasks;
        public List<ShippingPlan> shippingPlans;

        public Pair(List<ShippingTask> shippingTask, List<ShippingPlan> shippingPlan) {
            this.shippingTasks = shippingTask;
            this.shippingPlans = shippingPlan;
        }
    }
    @Autowired
    private ShippingPlanMapper shippingPlanMapper;
    @Autowired
    private ShippingTaskMapper shippingTaskMapper;

    @Override
    public List<ShippingPlan> getList(ShippingPlanDTO dto) {
        LambdaQueryWrapper<ShippingPlan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShippingPlan::getStatus, StatusEnums.FALSE);
        queryWrapper.eq(ShippingPlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());

        return shippingPlanMapper.selectList(queryWrapper);
    }

    @Override
    public Pair  converList(List<ShippingPlan> list) {
        List<ShippingTask> result=new ArrayList<>();
        if  (CollectionUtils.isNotEmpty(list)){
            list.forEach(item -> {
                ShippingTask shippingTask=BeanConverUtil.conver(item, ShippingTask.class);
                shippingTask.setShippingPlanId(item.getId());
                shippingTask.setPackageNo(item.getStockMovementDate().replaceAll("[\\\\:\\s]+", ""));
                result.add(shippingTask);
                item.setStatus(StatusEnums.TRUE.getCode());
            }) ;
        }
        return new Pair(result, list);
    }

}




