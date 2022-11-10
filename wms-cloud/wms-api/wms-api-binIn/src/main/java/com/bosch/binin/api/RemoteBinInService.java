package com.bosch.binin.api;


import com.bosch.binin.api.factory.RemoteBinInFallbackFactory;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(contextId = "remoteBinInService", value = ServiceNameConstants.STORAGE_IN, fallbackFactory = RemoteBinInFallbackFactory.class)
public interface RemoteBinInService {



}
