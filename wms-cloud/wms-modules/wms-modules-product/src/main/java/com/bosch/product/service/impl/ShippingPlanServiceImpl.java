package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GUZ1CGD4
 * @description 针对表【shipping_plan】的数据库操作Service实现
 * @createDate 2023-03-15 16:35:14
 */
@Service
public class ShippingPlanServiceImpl extends ServiceImpl<ShippingPlanMapper, ShippingPlan>
        implements IShippingPlanService {

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
    public List<ShippingPlanVO> getPlanList(Long id) {
        ShippingTask shippingTask = shippingTaskMapper.selectById(id);
        if (shippingTask == null) {
            return null;
        }
        String shippingPlanIds = shippingTask.getShippingPlanId();
        // 1. 将id字符串按照","进行分割，得到一个id集合。
        List<Long> idList = Arrays.stream(shippingPlanIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
        // 2. 构造一个Wrapper条件对象，使用in方法传入idList作为参数。
        LambdaQueryWrapper<ShippingPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ShippingPlan::getId, idList);

        // 3. 调用shippingPlanMapper的selectList方法，获取符合条件的ShippingPlan列表。
        List<ShippingPlan> shippingPlanList = shippingPlanMapper.selectList(wrapper);
        List<ShippingPlanVO> shippingPlanVOS = BeanConverUtil.converList(shippingPlanList, ShippingPlanVO.class);
        return shippingPlanVOS;
    }

    @Override
    public Integer updatePlan(MdProductPackagingDTO dto) {
        return null;
    }

    @Override
    public Integer deletePlan(Long[] ids) {
        return shippingPlanMapper.deletePlan(ids);
    }


}




