package com.ruoyi.common.core.config;

import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.querymap.BeanQueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: wms-cloud
 * @description:
 * @author: xuhao
 * @create: 2023-02-09 10:34
 **/
@Configuration
public class FeignConfig {


    @Bean
    Logger.Level feignLoggerLevel() {
        //这里记录所有，根据实际情况选择合适的日志level
        return Logger.Level.FULL;
    }

    /**
     * @Description 替换解析queryMap的类，实现父类中变量的映射

     */
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .queryMapEncoder(new BeanQueryMapEncoder())
                .retryer(Retryer.NEVER_RETRY);
    }

}
