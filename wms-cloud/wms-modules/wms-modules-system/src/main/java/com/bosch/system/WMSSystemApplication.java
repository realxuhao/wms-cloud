package com.bosch.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import com.ruoyi.common.swagger.annotation.EnableCustomSwagger2;

/**
 * 系统模块
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@SpringBootApplication
public class WMSSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(WMSSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "___.                       .__        _________________   \n" +
                "\\_ |__   ____  ______ ____ |  |__    /   _____/\\_____  \\  \n" +
                " | __ \\ /  _ \\/  ___// ___\\|  |  \\   \\_____  \\  /   |   \\ \n" +
                " | \\_\\ (  <_> )___ \\\\  \\___|   Y  \\  /        \\/    |    \\\n" +
                " |___  /\\____/____  >\\___  >___|  / /_______  /\\_______  /\n" +
                "     \\/           \\/     \\/     \\/          \\/         \\/ ");
    }
}
