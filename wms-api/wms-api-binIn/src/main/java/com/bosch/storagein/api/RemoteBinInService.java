package com.bosch.storagein.api;


import com.bosch.storagein.api.factory.RemoteBinInFallbackFactory;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remoteBinInService", value = ServiceNameConstants.STORAGE_IN, fallbackFactory = RemoteBinInFallbackFactory.class)
public interface RemoteBinInService {



}
