package com.bosch.binin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bosch.binin.api.domain.Stock;
import com.bosch.binin.api.domain.TranshipmentOrder;

import java.util.List;

public interface ITranshipmentOrderService extends IService<TranshipmentOrder> {

    /**
     * 根据运单号获取sscc集合
     * @param transhipmentOrder
     * @return
     */
    List<TranshipmentOrder> getSSCCByOrder(String transhipmentOrder);

    /**
     * 根据sscc获取运单号
     * @param sscc
     * @return
     */
    String getOrderBySSCC(String sscc);

    /**
     * 根据sscc集合获取数据
     * @param sscc
     * @return
     */
    List<TranshipmentOrder> getInfoBySSCC(List<String> sscc);

    /**
     * 根据sscc获取数据
     * @param sscc
     * @return
     */
    TranshipmentOrder getOneBySSCC(String sscc);

    Integer updateBySSCCS(List<String> ssccs);
}
