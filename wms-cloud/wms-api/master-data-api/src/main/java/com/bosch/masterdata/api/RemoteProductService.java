package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.vo.MaterialVO;
import com.bosch.masterdata.api.domain.vo.MdProductPackagingVO;
import com.bosch.masterdata.api.factory.RemotePalletFallbackFactory;
import com.bosch.masterdata.api.factory.RemoteProductFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-04-20 11:03
 **/
@FeignClient(contextId = "remoteProductService", value = ServiceNameConstants.MASTER_DATA_SERVICE, fallbackFactory = RemoteProductFallbackFactory.class)
public interface RemoteProductService {

    @GetMapping("/mdProductPackaging/getByCell/{cell}")
    public R<List<MdProductPackagingVO>> getByCell(@PathVariable("cell") String cell);
}
