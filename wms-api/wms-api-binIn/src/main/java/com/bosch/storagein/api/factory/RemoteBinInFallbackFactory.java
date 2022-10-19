package com.bosch.storagein.api.factory;

import com.bosch.storagein.api.RemoteBinInService;
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
public class RemoteBinInFallbackFactory implements FallbackFactory<RemoteBinInService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteBinInFallbackFactory.class);

    @Override
    public RemoteBinInService create(Throwable cause) {
        return null;
    }
}
