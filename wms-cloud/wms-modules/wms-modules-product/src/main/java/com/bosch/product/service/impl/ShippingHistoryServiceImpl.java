package com.bosch.product.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bosch.product.api.domain.ShippingHistory;
import com.bosch.product.api.domain.dto.ShippingDTO;
import com.bosch.product.api.domain.dto.ShippingHistoryDTO;
import com.bosch.product.api.domain.dto.ShippingTaskDTO;
import com.bosch.product.api.domain.vo.ShippingHistoryVO;
import com.bosch.product.api.domain.vo.ShippingTaskVO;
import com.bosch.product.api.domain.vo.ShippingVO;
import com.bosch.product.service.IShippingHistoryService;
import com.bosch.product.mapper.ShippingHistoryMapper;
import com.ruoyi.common.core.enums.DeleteFlagStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GUZ1CGD4
 * @description 针对表【shipping_history】的数据库操作Service实现
 * @createDate 2023-03-20 10:03:19
 */
@Service
public class ShippingHistoryServiceImpl extends ServiceImpl<ShippingHistoryMapper, ShippingHistory>
        implements IShippingHistoryService {

    @Autowired
    private ShippingHistoryMapper shippingHistoryMapper;

    @Override
    public Integer deleteHistoryByTaskId(Long id) {
        return shippingHistoryMapper.deleteHistoryByTaskId(id);
    }

    @Override
    public List<ShippingHistory> searchAllFields(ShippingHistoryDTO dto) {
        return shippingHistoryMapper.searchAllFields(dto);
    }

    @Override
    public List<ShippingVO> selectShipping(ShippingDTO dto) {
        return shippingHistoryMapper.selectShipping(dto);
    }
    @Override
    public List<ShippingVO> selectShippingNotLike(ShippingDTO dto) {
        return shippingHistoryMapper.selectShippingNotLike(dto);
    }

    @Override
    public Integer deleteNewestByTaskId(Long id) {
        //根据taskid为条件,按照create_time倒序,获取history表的最新一条数据
        LambdaQueryWrapper<ShippingHistory> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShippingHistory::getShippingTaskId,id);
        wrapper.eq(ShippingHistory::getDeleteFlag, DeleteFlagStatus.FALSE.getCode());
        wrapper.orderByDesc(ShippingHistory::getCreateTime);
        wrapper.last("LIMIT 1");
        int delete = shippingHistoryMapper.delete(wrapper);
        return delete;
    }


}




