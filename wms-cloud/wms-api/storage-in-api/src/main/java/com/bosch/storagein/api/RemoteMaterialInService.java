package com.bosch.storagein.api;

import com.bosch.storagein.api.domain.vo.MaterialInVO;
import com.bosch.storagein.api.domain.vo.MaterialReceiveVO;
import com.bosch.storagein.api.factory.RemoteStorageInFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "remoteMaterialInService", value = ServiceNameConstants.STORAGE_IN, fallbackFactory = RemoteStorageInFallbackFactory.class)
public interface RemoteMaterialInService {


    @GetMapping(value = "/material-in/getByMesBarCode/{mesBarCode}")
    public R<MaterialInVO> getByMesBarCode(@PathVariable("mesBarCode") String mesBarCode);

    @GetMapping(value = "/material-in/getSameBatchList")
    public R<List<MaterialReceiveVO>> getSameBatchList(@RequestParam("materialNb") String materialNb,
                                                       @RequestParam("batchNb") String batchNb);

}
