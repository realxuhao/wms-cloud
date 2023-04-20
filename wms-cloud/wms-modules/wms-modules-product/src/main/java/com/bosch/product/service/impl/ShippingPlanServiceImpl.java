package com.bosch.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bosch.masterdata.api.domain.dto.MdProductPackagingDTO;
import com.bosch.product.api.domain.ShippingHistory;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ShippingPlanDTO;
import com.bosch.product.api.domain.dto.ShippingPlanVO;
import com.bosch.product.mapper.ShippingHistoryMapper;
import com.bosch.product.mapper.ShippingTaskMapper;
import com.bosch.product.service.IShippingPlanService;
import com.bosch.product.mapper.ShippingPlanMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import com.ruoyi.common.core.enums.StatusEnums;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private ShippingHistoryMapper shippingHistoryMapper;

    @Override
    public List<ShippingPlan> getList(ShippingPlanDTO shippingPlanDTO) {
        LambdaQueryWrapper<ShippingPlan> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(ShippingPlan::getStatus, StatusEnums.FALSE);
        wrapper.eq(ShippingPlan::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        if (shippingPlanDTO.getStatus() != null) {
            wrapper.eq(ShippingPlan::getStatus, shippingPlanDTO.getStatus());
        }
        if (StringUtils.isNotBlank(shippingPlanDTO.getShippingMark())) {
            wrapper.like(ShippingPlan::getShippingMark, shippingPlanDTO.getShippingMark());
        }
        if (StringUtils.isNotBlank(shippingPlanDTO.getEtoPo())) {
            wrapper.like(ShippingPlan::getEtoPo, shippingPlanDTO.getEtoPo());
        }
        if (StringUtils.isNotBlank(shippingPlanDTO.getEtoPlant())) {
            wrapper.like(ShippingPlan::getEtoPlant, shippingPlanDTO.getEtoPlant());
        }

        // Handle StockMovementDate range query
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (shippingPlanDTO.getStockMovementDateStart() != null && shippingPlanDTO.getStockMovementDateEnd() != null) {

            String startDateStr = sdf.format(shippingPlanDTO.getStockMovementDateStart());
            String endDateStr = sdf.format(shippingPlanDTO.getStockMovementDateEnd());
            String columnExpr = "STR_TO_DATE(" + "stock_movement_date" + ", '%Y/%c/%e %k:%i')";
            wrapper.apply(columnExpr + " BETWEEN '" + startDateStr + "' AND '" + endDateStr + "'");

        } else if (shippingPlanDTO.getStockMovementDateStart() != null) {
            String startDateStr = sdf.format(shippingPlanDTO.getStockMovementDateStart());
            String columnExpr = "STR_TO_DATE(" + "stock_movement_date" + ", '%Y/%c/%e %k:%i')";
            wrapper.apply(columnExpr + " >= '" + startDateStr);
        } else if (shippingPlanDTO.getStockMovementDateEnd() != null) {
            String endDateStr = sdf.format(shippingPlanDTO.getStockMovementDateEnd());
            String columnExpr = "STR_TO_DATE(" + "stock_movement_date" + ", '%Y/%c/%e %k:%i')";
            wrapper.apply(columnExpr + " <= '" + endDateStr);
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getCountry())) {
            wrapper.like(ShippingPlan::getCountry, shippingPlanDTO.getCountry());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getProdOrder())) {
            wrapper.like(ShippingPlan::getProdOrder, shippingPlanDTO.getProdOrder());
        }

        if (shippingPlanDTO.getQty() != null) {
            wrapper.eq(ShippingPlan::getQty, shippingPlanDTO.getQty());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getIsDisassembled())) {
            wrapper.like(ShippingPlan::getIsDisassembled, shippingPlanDTO.getIsDisassembled());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getTr())) {
            wrapper.like(ShippingPlan::getTr, shippingPlanDTO.getTr());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getSapCode())) {
            wrapper.like(ShippingPlan::getSapCode, shippingPlanDTO.getSapCode());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getPalletQuantity())) {
            wrapper.like(ShippingPlan::getPalletQuantity, shippingPlanDTO.getPalletQuantity());
        }

        if (StringUtils.isNotBlank(shippingPlanDTO.getAfterPacking())) {
            wrapper.like(ShippingPlan::getAfterPacking, shippingPlanDTO.getAfterPacking());
        }
        //根据STR_TO_DATE(stock_movement_date, '%Y/%m/%d %H:%i')正序
        String orderBy = "STR_TO_DATE(" + "stock_movement_date" + ", '%Y/%c/%e %k:%i')";
        wrapper.last("order by "+orderBy + " ASC");

        return shippingPlanMapper.selectList(wrapper);
    }

    private static <T> String testStr(SFunction<T, ?> lambda) {
        return "STR_TO_DATE(" + lambda + ", '%Y/%c/%e %k:%i')";
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

    @Override
    public boolean validate(List<ShippingPlanDTO> list) {

        //将list的shippingMark，etoPo转换为一个新set
        Set<String> set = new HashSet<>();
        for (ShippingPlanDTO shippingPlanDTO : list) {
            set.add(shippingPlanDTO.getShippingMark() + "+" + shippingPlanDTO.getEtoPo());
        }
        QueryWrapper<ShippingPlan> wrapper = new QueryWrapper<>();
        wrapper.in("CONCAT(shipping_mark,'+', eto_po)", set);
        //deleteflag为0
        wrapper.eq("delete_flag", 0);
        int count = shippingPlanMapper.selectCount(wrapper);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRepeatPlan(List<ShippingPlanDTO> list) {
        //将list的shippingMark，etoPo转换为一个新set
        Set<String> set = new HashSet<>();
        for (ShippingPlanDTO shippingPlanDTO : list) {
            set.add(shippingPlanDTO.getShippingMark() + "+" + shippingPlanDTO.getEtoPo());
        }
        QueryWrapper<ShippingPlan> wrapper = new QueryWrapper<>();
        wrapper.in("CONCAT(shipping_mark,'+', eto_po)", set);
        //deleteflag为0
        wrapper.eq("delete_flag", 0);
        //删除plan
        int countPlan = shippingPlanMapper.delete(wrapper);
        //删除task
        QueryWrapper<ShippingTask> taskWrapper = new QueryWrapper<>();
        taskWrapper.in("CONCAT(shipping_mark,'+', eto_po)", set);
        taskWrapper.eq("delete_flag", 0);
        //查询要删除的task的id
        List<ShippingTask> shippingTasks = shippingTaskMapper.selectList(taskWrapper);
        int countTask = shippingTaskMapper.delete(taskWrapper);
        //获取要删除的task的id，用lambda表达式
        //如果shippingTasks不为空，就返回shippingTasks的id
        if(CollectionUtils.isNotEmpty(shippingTasks)){
            List<Long> taskIds = shippingTasks.stream().map(ShippingTask::getId).collect(Collectors.toList());
            //删除history，delete_flag为0
            QueryWrapper<ShippingHistory> historyWrapper = new QueryWrapper<>();
            historyWrapper.in("shipping_task_id", taskIds);
            historyWrapper.eq("delete_flag", 0);
            int countHistory = shippingHistoryMapper.delete(historyWrapper);
        }
        if (countPlan > 0 && countTask > 0) {
            return true;
        }
        return false;
    }


}




