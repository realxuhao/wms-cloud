package com.bosch.masterdata.api.factory;

import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.RemotePalletService;
import com.ruoyi.common.core.domain.R;
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
public class RemotePalletFallbackFactory implements FallbackFactory<RemotePalletService> {
    private static final Logger log = LoggerFactory.getLogger(RemotePalletFallbackFactory.class);

    @Override
    public RemotePalletService create(Throwable throwable) {
        log.error("主数据服务调用失败:{}", throwable.getMessage());
        return palletType -> R.fail("主数据服务调用失败");
    }
}
