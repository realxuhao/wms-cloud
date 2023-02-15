package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.vo.TimeWindowVO;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(contextId = "remoteTimeWindowService", value = ServiceNameConstants.MASTER_DATA_SERVICE)
public interface RemoteTimeWindowService {

    @GetMapping(value = "/timeWindow/ware/{wareId}")
    public R<List<TimeWindowVO>> getListByWareId(@PathVariable("wareId") Long wareId);

}
