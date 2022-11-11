package com.bosch.file.api;

import com.bosch.file.api.factory.StockFeignServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@FeignClient(name = "stock-service",path = "/stock",fallback = StockFeignServiceFallback.class)
public interface FileFeignService {
    //声明需要调用的rest接口对应的方法
    @RequestMapping("/reduct")
    String reduct();

}
