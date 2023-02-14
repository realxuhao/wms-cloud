package com.bosch.vehiclereservation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bosch.vehiclereservation.api.domain.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购单Mapper接口
 *
 * @author taojd
 * @date 2023-02-14
 */
@Mapper
@Repository("purchaseOrderMapper")
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

    /**
     * 查询采购单列表
     *
     * @param purchaseOrder 采购单
     * @return 采购单集合
     */
    public List<PurchaseOrder> selectPurchaseOrderList(PurchaseOrder purchaseOrder);

}
