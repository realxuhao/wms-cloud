package com.bosch.product.api.factory;

import com.bosch.product.api.RemoteProductStockService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-23 13:31
 **/
@Component
public class RemoteProductStockFallbackFactory implements FallbackFactory<RemoteProductStockService> {
    @Override
    public RemoteProductStockService create(Throwable cause) {
        return null;
    }
}
