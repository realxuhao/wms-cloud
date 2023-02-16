package com.bosch.masterdata.api.factory;

import com.bosch.masterdata.api.RemoteIQCService;
import com.bosch.masterdata.api.RemoteMaterialService;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.domain.dto.MaterialDTO;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.PageVO;
import com.ruoyi.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-16 15:16
 **/
@Component
public class RemoteIQCFallbackFactory implements FallbackFactory<RemoteIQCService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIQCFallbackFactory.class);

    @Override
    public RemoteIQCService create(Throwable throwable) {
        log.error("物料查询服务调用失败:{}", throwable.getMessage());
        return new RemoteIQCService() {


            @Override
            public R<Nmd> getByMaterialNb(String materialNb) {
                return R.fail("根据物料号查询IQC规则失败");
            }
        };
    }
}
