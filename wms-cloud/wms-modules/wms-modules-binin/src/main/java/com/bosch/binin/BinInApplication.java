package com.bosch.binin;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 入库服务模块
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class BinInApplication {
    public static void main(String[] args) {
        SpringApplication.run(BinInApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  上架服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "___.                       .__        _________________   \n" +
                "\\_ |__   ____  ______ ____ |  |__    /   _____/\\_____  \\  \n" +
                " | __ \\ /  _ \\/  ___// ___\\|  |  \\   \\_____  \\  /   |   \\ \n" +
                " | \\_\\ (  <_> )___ \\\\  \\___|   Y  \\  /        \\/    |    \\\n" +
                " |___  /\\____/____  >\\___  >___|  / /_______  /\\_______  /\n" +
                "     \\/           \\/     \\/     \\/          \\/         \\/ ");
    }
}
