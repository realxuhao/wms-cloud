package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.Pallet;
import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.bosch.masterdata.api.factory.RemotePalletFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(contextId = "remotePalletService", value = ServiceNameConstants.MASTER_DATA_SERVICE, fallbackFactory = RemotePalletFallbackFactory.class)
public interface RemotePalletService {


    @GetMapping(value = "/pallet/getByType/{palletType}")
    public R<Pallet> getByType(@PathVariable("palletType") String palletType);


}
