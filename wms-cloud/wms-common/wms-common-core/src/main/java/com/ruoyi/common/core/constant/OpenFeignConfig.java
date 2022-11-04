package com.ruoyi.common.core.constant;

import feign.Feign;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(Feign.class)
@Configuration
public class OpenFeignConfig {
    /**
     * 自定义异常解码器
     * @return OpenFeignErrorDecoder
     */
    @Bean
    public ErrorDecoder errorDecoder(){
        return new OpenFeignErrorDecoder();
    }
}