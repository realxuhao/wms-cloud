package com.bosch.product.api;


import com.bosch.product.api.domain.dto.ProductStockQueryDTO;
import com.bosch.product.api.domain.vo.ProductStockVO;
import com.bosch.product.api.factory.RemoteProductStockFallbackFactory;
import com.ruoyi.common.core.config.FeignConfig;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(contextId = "remoteProductStockService",configuration = FeignConfig.class,  value = ServiceNameConstants.PRODUCT, fallbackFactory = RemoteProductStockFallbackFactory.class)
public interface RemoteProductStockService {

    @GetMapping(value = "/product-stock/getByBarCode/{barCode}")
    public R<ProductStockVO> getByBarCode(@PathVariable("barCode") String barCode);


    @GetMapping(value = "/product-stock/listByBinCode/{binCode}")
    public R<List<ProductStockVO>> listByBinCode(@PathVariable("binCode") String binCode);

}
