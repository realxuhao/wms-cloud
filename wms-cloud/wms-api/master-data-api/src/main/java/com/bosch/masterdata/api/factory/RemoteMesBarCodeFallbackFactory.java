package com.bosch.masterdata.api.factory;

import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.RemoteMesBarCodeService;
import com.ruoyi.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-02 14:36
 **/
@Component
public class RemoteMesBarCodeFallbackFactory implements FallbackFactory<RemoteMesBarCodeService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMesBarCodeFallbackFactory.class);

    @Override
    public RemoteMesBarCodeService create(Throwable cause) {
        log.error("主数据服务调用失败:{}", cause.getMessage());
        return palletType -> R.fail("主数据服务调用失败");
    }
}
