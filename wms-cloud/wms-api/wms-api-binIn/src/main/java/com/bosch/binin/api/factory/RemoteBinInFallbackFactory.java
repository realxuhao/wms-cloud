package com.bosch.binin.api.factory;

import com.bosch.binin.api.RemoteBinInService;
import com.bosch.binin.api.domain.TranshipmentOrder;
import com.ruoyi.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

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
        log.error("binin务调用失败:{}", cause.getMessage());
        return new RemoteBinInService() {

            @Override
            public R<String> getNextOrderNb() {
                return R.fail("getNextOrderNb失败");
            }

            @Override
            public R saveBatch(List<TranshipmentOrder> transhipmentOrderList) {
                return R.fail("获取库存列表失败");
            }

            @Override
            public R listBySSCC(List<String> ssccList) {
                return R.fail("通过sscc获取库存列表失败");
            }
        };
    }
}
