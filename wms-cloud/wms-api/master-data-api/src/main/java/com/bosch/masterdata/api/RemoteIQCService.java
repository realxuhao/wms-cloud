package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Ecn;
import com.bosch.masterdata.api.domain.Fsmp;
import com.bosch.masterdata.api.domain.Nmd;
import com.bosch.masterdata.api.factory.RemoteIQCFallbackFactory;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.ruoyi.common.core.config.FeignConfig;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-16 15:14
 **/
@FeignClient(contextId = "remoteIQCService", configuration = FeignConfig.class,value = ServiceNameConstants.MASTER_DATA_SERVICE, fallbackFactory = RemoteIQCFallbackFactory.class)
public interface RemoteIQCService {

    @GetMapping("/nmd/getByMaterialNb/{materialNb}")
    public R<Nmd> getNmdByMaterialNb(@PathVariable("materialNb") String materialNb);


    @GetMapping("/ecn/getByMaterialNb/{materialNb}")
    public R<Ecn> getEcnByMaterialNb(@PathVariable("materialNb") String materialNb);

    @GetMapping("/fsmp/getByMaterialNb/{materialNb}")
    public R<Fsmp> getFsmpByMaterialNb(@PathVariable("materialNb") String materialNb);
}
