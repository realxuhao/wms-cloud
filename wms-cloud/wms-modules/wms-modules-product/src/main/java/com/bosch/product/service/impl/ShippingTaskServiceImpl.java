package com.bosch.product.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ShippingHistory;
import com.bosch.product.api.domain.ShippingPlan;
import com.bosch.product.api.domain.ShippingTask;
import com.bosch.product.api.domain.dto.ListPair;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.mapper.ShippingHistoryMapper;
import com.bosch.product.service.IShippingTaskService;
import com.bosch.product.mapper.ShippingTaskMapper;
import com.ruoyi.common.core.utils.bean.BeanConverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private ShippingHistoryMapper shippingHistoryMapper;

    @Override
    public List<ShippingTask> selectAllByFields(ShippingTaskDTO dto) {
        return shippingTaskMapper.selectAllByFields(dto);
    }

    @Override
    public Integer deleteShippingTask(Long[] ids) {
        return shippingTaskMapper.deleteShippingTask(ids);
    }

    @Override
    public ListPair genTask(List<ShippingPlan> list) {
        List<ShippingTask> result = new ArrayList<>();
        // 按照stockMovementDate字段进行分组
        Map<String, List<ShippingPlan>> groupedByDate =
                list.stream()
                        .collect(Collectors.groupingBy(ShippingPlan::getStockMovementDate));

        // 遍历每个分组
        for (Map.Entry<String, List<ShippingPlan>> entry : groupedByDate.entrySet()) {
            String stockMovementDate = entry.getKey();
            List<ShippingPlan> plans = entry.getValue();

            // 按照shippingMark和etoPo进行分组
            Map<String, List<ShippingPlan>> groupedByMarkAndPo =
                    plans.stream()
                            .collect(Collectors.groupingBy(plan ->
                                    plan.getShippingMark() + plan.getEtoPo()));

            // 遍历每个分组
            for (Map.Entry<String, List<ShippingPlan>> markAndPoEntry : groupedByMarkAndPo.entrySet()) {
                String shippingMarkAndPo = markAndPoEntry.getKey();
                List<ShippingPlan> markAndPoPlans = markAndPoEntry.getValue();

                // 将palletQuantity和afterPacking分别求和
                int totalPalletQuantity = markAndPoPlans.stream()
                        .mapToInt(plan -> (int) Math.floor(Double.parseDouble(plan.getPalletQuantity())))
                        .sum();
                int totalAfterPacking = markAndPoPlans.stream()
                        .mapToInt(plan -> (int) Math.floor(Double.parseDouble(plan.getAfterPacking())))
                        .sum();

                String idsStr = markAndPoPlans.stream()
                        .map(ShippingPlan::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                markAndPoPlans.stream().forEach(sp -> sp.setStatus(1));
                // 输出分组后的数据和注释
                //ShippingTask taskDo = new ShippingTask();
                ShippingTask conver = BeanConverUtil.conver(markAndPoPlans.get(0), ShippingTask.class);
                conver.setShippingPlanId(idsStr);
                conver.setPackageNo(markAndPoPlans.get(0).getStockMovementDate().replaceAll("[\\\\:\\s]+", ""));
                conver.setPalletQuantity(String.valueOf(totalPalletQuantity));
                conver.setAfterPacking(String.valueOf(totalAfterPacking));
                result.add(conver);
//                System.out.println("日期：" + stockMovementDate +
//                        "，货运标记和ETO PO：" + shippingMarkAndPo +
//                        "，托盘数量合计：" + totalPalletQuantity +
//                        "，装箱后数量合计：" + totalAfterPacking);
            }
        }
        return new ListPair(result,list);
    }

    @Override
    public boolean updateStatus(Long[] ids, Integer status) {
        return shippingTaskMapper.updateStatus(ids,status)>0?true:false;
    }

    @Override
    public boolean checkHistory(Long taskId) {

        ShippingHistoryDTO dto=new ShippingHistoryDTO();
        dto.setShippingTaskId(taskId);
        List<ShippingHistory> shippingHistories = shippingHistoryMapper.searchAllFields(dto);
        if (CollectionUtils.isEmpty(shippingHistories)){
            return false;
        }
        //获取task下所有扫描sscc码的个数
        int count = shippingHistories.stream()
                .map(ShippingHistory::getSsccNumbers)
                .filter(Objects::nonNull)
                .mapToInt(ssccNumbers -> ssccNumbers.split(",").length)
                .sum();
        //获取task
        ShippingTask shippingTask = shippingTaskMapper.selectById(taskId);
        if (shippingTask == null){
            return false;
        }
        //	同打包任务下下做扫描的总次数（扫描的SSCC个数）≥W列[Pallet Quantity]的和
        boolean checktotal = count >= Integer.parseInt(shippingTask.getPalletQuantity());
        //同打包任务下所有托数
        boolean checksize = shippingHistories.size() == Integer.parseInt(shippingTask.getAfterPacking());

        if(checktotal&&checksize){
            return true;
        }
        return false;
    }

    @Override
    public List<ShippingTaskVO> getDashboard() {
        return shippingTaskMapper.getDashboard();
    }

}




