package com.bosch.binin.api;


import com.bosch.binin.api.domain.TranshipmentOrder;
import com.bosch.binin.api.domain.vo.StockVO;
import com.bosch.binin.api.factory.RemoteBinInFallbackFactory;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@FeignClient(contextId = "remoteBinInService",  value = ServiceNameConstants.BIN_IN, fallbackFactory = RemoteBinInFallbackFactory.class)
public interface RemoteBinInService {


    @GetMapping("/trans-ship/getNextOrderNb")
    public R<String> getNextOrderNb();

    @PostMapping("/trans-ship/saveBatch")
    public R saveBatch(@RequestBody List<TranshipmentOrder> transhipmentOrderList);

    @PostMapping("/stock/listBySSCC")
    public R<List<StockVO>> listBySSCC(@RequestBody List<String> ssccList);
}
