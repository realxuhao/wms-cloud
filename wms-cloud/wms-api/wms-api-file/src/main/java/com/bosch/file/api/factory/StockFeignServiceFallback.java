package com.bosch.file.api.factory;

import com.bosch.file.api.FileFeignService;
import org.springframework.stereotype.Component;

@Component
public class StockFeignServiceFallback implements FileFeignService {
    @Override
    public String reduct(){
        return "Fallback,降级了";
    }
}
