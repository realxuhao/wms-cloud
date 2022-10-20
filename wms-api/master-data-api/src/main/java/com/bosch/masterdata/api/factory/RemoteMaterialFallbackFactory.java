package com.bosch.masterdata.api.factory;

import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
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
public class RemoteMaterialFallbackFactory implements FallbackFactory<RemoteMaterialService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMaterialFallbackFactory.class);

    @Override
    public RemoteMaterialService create(Throwable throwable) {
        log.error("物料查询服务调用失败:{}", throwable.getMessage());
        return new RemoteMaterialService() {
            @Override
            public R<MaterialVO> getInfoByMaterialCode(String materialCode) {
                return R.fail("物料明细查询失败");
            }


        };
    }
}
