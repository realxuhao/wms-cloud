package com.bosch.masterdata.api;

import com.bosch.masterdata.api.domain.vo.MesBarCodeVO;
import com.bosch.masterdata.api.factory.RemoteMaterialFallbackFactory;
import com.bosch.masterdata.api.factory.RemoteMesBarCodeFallbackFactory;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-02 14:35
 **/
@FeignClient(contextId = "remoteMesBarCodeService", value = ServiceNameConstants.MASTER_DATA_SERVICE, fallbackFactory = RemoteMesBarCodeFallbackFactory.class)
public interface RemoteMesBarCodeService {

    @GetMapping("/mesBarCode/parseMesBarCode/{mesBarCode}")
    public R<MesBarCodeVO> parseMesBarCode(@PathVariable("mesBarCode") String mesBarCode);
}
