package com.bosch.masterdata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sftp")
@Data
public class SFtpConfig {

    /**
     * protocol
     */
    private String protocol;


    /**
     * sftp服务的地址
     */
    private String host;

    /**
     * 连接端口
     */
    private int port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 上传文件存放的路径
     */
    private String remoteDirectory;

}
