package com.bosch.storagein.api.factory;

import com.bosch.storagein.api.RemoteMaterialInService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteStorageInFallbackFactory implements FallbackFactory<RemoteMaterialInService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteStorageInFallbackFactory.class);

    @Override
    public RemoteMaterialInService create(Throwable throwable) {
        log.error("入库服务调用失败:{}", throwable.getMessage());
        return mesBarCode -> R.fail("获取入库信息失败");
    }
}
