/*
 Navicat Premium Data Transfer

 Source Server         : 121.36.136.109
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 121.36.136.109:3306
 Source Schema         : wms-config

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 28/10/2022 10:31:32
*/


create database if not exists wms-config;

use wms-config;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                               `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                               `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
                               `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                               `src_user` text COLLATE utf8_bin COMMENT 'source user',
                               `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
                               `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
                               `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `c_schema` text COLLATE utf8_bin,
                               `encrypted_data_key` text COLLATE utf8_bin COMMENT '秘钥',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (56, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\npagehelper: \n  pageSizeZero: true\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n', '9bfa7b22e2a2278e7b94ba8e508ef99a', '2022-10-10 05:20:28', '2022-10-24 07:12:58', 'nacos', '39.144.153.188', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (57, 'ruoyi-monitor-dev.yml', 'DEFAULT_GROUP', '# spring\r\nspring: \r\n  security:\r\n    user:\r\n      name: ruoyi\r\n      password: 123456\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: 若依服务状态监控\r\n', 'd8997d0707a2fd5d9fc4e8409da38129', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (58, 'ruoyi-gen-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: \n  datasource: \n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 代码生成接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n# 代码生成\ngen: \n  # 作者\n  author: xuhao\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.bosch.masterdata\n  # 自动去除表前缀，默认是false\n  autoRemovePre: true\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: md_\n', 'c0766a1edeb1d280d67f8fa789399813', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (59, 'ruoyi-job-dev.yml', 'DEFAULT_GROUP', '# spring配置\r\nspring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n    username: root\r\n    password: 123456\r\n\r\n# mybatis配置\r\nmybatis:\r\n    # 搜索指定包别名\r\n    typeAliasesPackage: com.ruoyi.job.domain\r\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n    mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 定时任务接口文档\r\n  license: Powered By ruoyi\r\n  licenseUrl: https://ruoyi.vip\r\n', '8e325b28f4dfe963873503c83d302db1', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (60, 'ruoyi-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (61, 'sentinel-ruoyi-gateway', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"ruoyi-auth\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-gen\",\n        \"count\": 200,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]', '411936d945573749e5956f2df0b04989', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'text', NULL, '');
INSERT INTO `config_info` VALUES (62, 'master-data-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.masterdata\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\npagehelper: \n  pageSizeZero: true', 'a8fa7f223c634c8a7ebc1371fde48484', '2022-10-10 05:20:28', '2022-10-26 06:51:13', 'nacos', '117.136.67.208', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (63, 'wms-system-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 127.0.0.1:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.bosch.system\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', 'e9521679b77e6af8dfda425204d61c97', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (64, 'wms-gateway-dev.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wms-auth\n          uri: lb://wms-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: wms-system\n          uri: lb://wms-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wms-file\n          uri: lb://wms-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n         # 主数据服务\n        - id: master-data\n          uri: lb://master-data\n          predicates:\n            - Path=/masterdata/**\n          filters:\n            - StripPrefix=1\n         # 入库服务\n        - id: storage-in\n          uri: lb://storage-in\n          predicates:\n            - Path=/storagein/**\n          filters:\n            - StripPrefix=1\n\n\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: false\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /data/**\n      - /masterdata/*\n      - /supplierInfo/*\n', 'fecfd9a8bbae4e25286c2a8ba44bb084', '2022-10-10 05:20:28', '2022-10-24 06:21:52', 'nacos', '117.136.68.21', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (65, 'wms-auth-dev.yml', 'DEFAULT_GROUP', 'spring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n', 'c7e1dd3db506d101aed2158d586b8034', '2022-10-10 05:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (67, 'storage-in-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.storagein\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n\npagehelper: \n  pageSizeZero: true', '0e4b4ef9bcab800278bf37ed611a0722', '2022-10-10 05:20:28', '2022-10-26 06:50:22', 'nacos', '117.136.67.208', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (69, 'wms-file-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  servlet:\n    multipart:\n      max-file-size: 10MB\n      max-request-size: 10MB\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://121.36.136.109:9800/\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', 'a4fbb5a43bce9f17d380d56fea3832d1', '2022-10-18 05:51:11', '2022-10-26 06:50:38', 'nacos', '117.136.67.208', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (71, 'bin-in-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.binin\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n\npagehelper: \n  pageSizeZero: true', '7d3dbf9dcd88ced87872129401b1609a', '2022-10-20 01:55:57', '2022-10-24 07:31:34', 'nacos', '39.144.153.188', '', '', '', '', '', 'yaml', '', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
                                    `datum_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
                                    `content` longtext COLLATE utf8_bin NOT NULL COMMENT '内容',
                                    `gmt_modified` datetime NOT NULL COMMENT '修改时间',
                                    `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
                                    `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                                    `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
                                    `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
                                    `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
                                    `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
                                    `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
                                    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `src_user` text COLLATE utf8_bin COMMENT 'source user',
                                    `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
                                    `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                                    `encrypted_data_key` text COLLATE utf8_bin COMMENT '秘钥',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                                   `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
                                   `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
                                   `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
                                   `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
                                   `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `src_user` text COLLATE utf8_bin COMMENT 'source user',
                                   `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
                                        `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
                                        `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                                        `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
                                        `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
                                        `nid` bigint(20) NOT NULL AUTO_INCREMENT,
                                        PRIMARY KEY (`nid`) USING BTREE,
                                        UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`) USING BTREE,
                                        KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
                                  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
                                  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
                                  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
                                  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
                                  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
                                  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
                                  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info` (
                                   `id` bigint(20) unsigned NOT NULL,
                                   `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                   `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
                                   `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
                                   `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext COLLATE utf8_bin NOT NULL,
                                   `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `src_user` text COLLATE utf8_bin,
                                   `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL,
                                   `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
                                   `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                                   `encrypted_data_key` text COLLATE utf8_bin COMMENT '秘钥',
                                   PRIMARY KEY (`nid`) USING BTREE,
                                   KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
                                   KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
                                   KEY `idx_did` (`data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
INSERT INTO `his_config_info` VALUES (2, 48, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n         # 主数据服务\n        - id: master-data\n          uri: lb://master-data\n          predicates:\n            - Path=/masterdata/**\n          filters:\n            - StripPrefix=1\n\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /data/**\n      - /masterdata/**\n', 'e6fb165a36e36349e8560cc61f74f5b8', '2022-10-09 15:40:41', '2022-10-09 07:40:41', 'nacos', '39.144.153.179', 'U', '', '');
INSERT INTO `his_config_info` VALUES (1, 49, 'application-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n', 'aaa73b809cfd4d0058893aa13da57806', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (2, 50, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  cloud:\n    gateway:\n      globalcors:\n        corsConfigurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n         # 主数据服务\n        - id: master-data\n          uri: lb://master-data\n          predicates:\n            - Path=/masterdata/**\n          filters:\n            - StripPrefix=1\n\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /data/**\n      - /masterdata/**\n', '7f8dc5e4814af085ab8fd5b8afad5702', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (3, 51, 'ruoyi-auth-dev.yml', 'DEFAULT_GROUP', '', 'spring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n', 'c7e1dd3db506d101aed2158d586b8034', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (4, 52, 'ruoyi-monitor-dev.yml', 'DEFAULT_GROUP', '', '# spring\r\nspring: \r\n  security:\r\n    user:\r\n      name: ruoyi\r\n      password: 123456\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: 若依服务状态监控\r\n', 'd8997d0707a2fd5d9fc4e8409da38129', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (5, 53, 'ruoyi-system-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 127.0.0.1:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.system\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '334089cdd720a41436e31044d8f9f31e', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (6, 54, 'ruoyi-gen-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: \n  datasource: \n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 代码生成接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n# 代码生成\ngen: \n  # 作者\n  author: xuhao\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.bosch.masterdata\n  # 自动去除表前缀，默认是false\n  autoRemovePre: true\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: md_\n', 'c0766a1edeb1d280d67f8fa789399813', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (7, 55, 'ruoyi-job-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\r\nspring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n    username: root\r\n    password: 123456\r\n\r\n# mybatis配置\r\nmybatis:\r\n    # 搜索指定包别名\r\n    typeAliasesPackage: com.ruoyi.job.domain\r\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n    mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 定时任务接口文档\r\n  license: Powered By ruoyi\r\n  licenseUrl: https://ruoyi.vip\r\n', '8e325b28f4dfe963873503c83d302db1', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (8, 56, 'ruoyi-file-dev.yml', 'DEFAULT_GROUP', '', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (9, 57, 'sentinel-ruoyi-gateway', 'DEFAULT_GROUP', '', '[\r\n    {\r\n        \"resource\": \"ruoyi-auth\",\r\n        \"count\": 500,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-system\",\r\n        \"count\": 1000,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-gen\",\r\n        \"count\": 200,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-job\",\r\n        \"count\": 300,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    }\r\n]', '9f3a3069261598f74220bc47958ec252', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (41, 58, 'master-data-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.bosch.masterdata\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\npagehelper:\n  pageSizeZero: true', 'ed26c3e78db695898ec3777d01ffc593', '2022-10-10 13:20:20', '2022-10-10 05:20:21', NULL, '39.144.153.22', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 59, 'application-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n', 'aaa73b809cfd4d0058893aa13da57806', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 60, 'ruoyi-monitor-dev.yml', 'DEFAULT_GROUP', '', '# spring\r\nspring: \r\n  security:\r\n    user:\r\n      name: ruoyi\r\n      password: 123456\r\n  boot:\r\n    admin:\r\n      ui:\r\n        title: 若依服务状态监控\r\n', 'd8997d0707a2fd5d9fc4e8409da38129', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 61, 'ruoyi-gen-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: \n  datasource: \n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 代码生成接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n# 代码生成\ngen: \n  # 作者\n  author: xuhao\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.bosch.masterdata\n  # 自动去除表前缀，默认是false\n  autoRemovePre: true\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: md_\n', 'c0766a1edeb1d280d67f8fa789399813', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 62, 'ruoyi-job-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\r\nspring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n    password: \r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\r\n    username: root\r\n    password: 123456\r\n\r\n# mybatis配置\r\nmybatis:\r\n    # 搜索指定包别名\r\n    typeAliasesPackage: com.ruoyi.job.domain\r\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n    mapperLocations: classpath:mapper/**/*.xml\r\n\r\n# swagger配置\r\nswagger:\r\n  title: 定时任务接口文档\r\n  license: Powered By ruoyi\r\n  licenseUrl: https://ruoyi.vip\r\n', '8e325b28f4dfe963873503c83d302db1', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 63, 'ruoyi-file-dev.yml', 'DEFAULT_GROUP', '', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 64, 'sentinel-ruoyi-gateway', 'DEFAULT_GROUP', '', '[\n    {\n        \"resource\": \"ruoyi-auth\",\n        \"count\": 500,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-gen\",\n        \"count\": 200,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"ruoyi-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    }\n]', '411936d945573749e5956f2df0b04989', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 65, 'master-data-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.masterdata\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '6ae5ad80e52eb633d26dfb47c8424c70', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 66, 'wms-system-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 127.0.0.1:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.bosch.system\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 系统模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', 'e9521679b77e6af8dfda425204d61c97', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 67, 'wms-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wms-auth\n          uri: lb://wms-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: wms-system\n          uri: lb://wms-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wms-file\n          uri: lb://wms-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n         # 主数据服务\n        - id: master-data\n          uri: lb://master-data\n          predicates:\n            - Path=/masterdata/**\n          filters:\n            - StripPrefix=1\n         # 入库服务\n        - id: storage-in\n          uri: lb://storage-in\n          predicates:\n            - Path=/storagein/**\n          filters:\n            - StripPrefix=1\n\n\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /data/**\n      - /masterdata/*\n      - /supplierInfo/*\n', '16d983d3e6e64ba611e2299a8a073b98', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 68, 'wms-auth-dev.yml', 'DEFAULT_GROUP', '', 'spring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n', 'c7e1dd3db506d101aed2158d586b8034', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 69, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 70, 'storage-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.bosch.storagein\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '2e5d9facf35b5bea5ed7015c703ad008', '2022-10-10 13:20:28', '2022-10-10 05:20:28', NULL, '39.144.153.22', 'I', '', '');
INSERT INTO `his_config_info` VALUES (67, 71, 'storage-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.bosch.storagein\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '2e5d9facf35b5bea5ed7015c703ad008', '2022-10-12 13:58:58', '2022-10-12 05:58:59', 'nacos', '223.104.145.100', 'U', '', '');
INSERT INTO `his_config_info` VALUES (66, 72, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2022-10-18 13:51:06', '2022-10-18 05:51:06', NULL, '119.40.69.128', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 73, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', '3849c58609f5f45dccb459e4cd24d645', '2022-10-18 13:51:11', '2022-10-18 05:51:11', NULL, '119.40.69.128', 'I', '', '');
INSERT INTO `his_config_info` VALUES (69, 74, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://127.0.0.1:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', '3849c58609f5f45dccb459e4cd24d645', '2022-10-18 13:54:38', '2022-10-18 05:54:39', 'nacos', '223.104.148.99', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 75, 'bin-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\r\nspring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n  datasource:\r\n    druid:\r\n      stat-view-servlet:\r\n        enabled: true\r\n        loginUsername: admin\r\n        loginPassword: 123456\r\n    dynamic:\r\n      druid:\r\n        initial-size: 5\r\n        min-idle: 5\r\n        maxActive: 20\r\n        maxWait: 60000\r\n        timeBetweenEvictionRunsMillis: 60000\r\n        minEvictableIdleTimeMillis: 300000\r\n        validationQuery: SELECT 1 FROM DUAL\r\n        testWhileIdle: true\r\n        testOnBorrow: false\r\n        testOnReturn: false\r\n        poolPreparedStatements: true\r\n        maxPoolPreparedStatementPerConnectionSize: 20\r\n        filters: stat,slf4j\r\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\r\n      datasource:\r\n          # 主库数据源\r\n          master:\r\n            driver-class-name: com.mysql.cj.jdbc.Driver\r\n            url: jdbc:mysql://localhost:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\r\n            username: root\r\n            password: 123456\r\n          # 从库数据源\r\n          # slave:\r\n            # username: \r\n            # password: \r\n            # url: \r\n            # driver-class-name: \r\n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\r\n\r\n# seata配置\r\nseata:\r\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\r\n  enabled: false\r\n  # Seata 应用编号，默认为 ${spring.application.name}\r\n  application-id: ${spring.application.name}\r\n  # Seata 事务组编号，用于 TC 集群名\r\n  tx-service-group: ${spring.application.name}-group\r\n  # 关闭自动代理\r\n  enable-auto-data-source-proxy: false\r\n  # 服务配置项\r\n  service:\r\n    # 虚拟组和分组的映射\r\n    vgroup-mapping:\r\n      ruoyi-system-group: default\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      serverAddr: localhost:8848\r\n      group: SEATA_GROUP\r\n      namespace:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: localhost:8848\r\n      namespace:\r\n\r\n# mybatis配置\r\n# mybatis:\r\n#     # 搜索指定包别名\r\n#     typeAliasesPackage: com.bosch.storagein\r\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n#     mapperLocations: classpath:mapper/**/*.xml\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\r\n#  config-location: classpath:mybatis/mybatis-config.xml\r\n  #实体扫描，多个package用逗号或者分号分隔\r\n  typeAliasesPackage: com.bosch.binin\r\n  global-config:\r\n    #数据库相关配置\r\n    db-config:\r\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\r\n      id-type: AUTO\r\n      logic-delete-value: -1\r\n      logic-not-delete-value: 0\r\n    banner: false\r\n  #原生配置\r\n  configuration:\r\n    map-underscore-to-camel-case: true\r\n    cache-enabled: false\r\n    call-setters-on-nulls: true\r\n    jdbc-type-for-null: \'null\'\r\n# swagger配置\r\nswagger:\r\n  title: 主数据模块接口文档\r\n  license: Powered By ruoyi\r\n  licenseUrl: https://ruoyi.vip', '9a8e417772b049bf91cded85aeb52235', '2022-10-20 09:55:57', '2022-10-20 01:55:57', NULL, '39.144.153.122', 'I', '', '');
INSERT INTO `his_config_info` VALUES (71, 76, 'bin-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\r\nspring: \r\n  redis:\r\n    host: localhost\r\n    port: 6379\r\n  datasource:\r\n    druid:\r\n      stat-view-servlet:\r\n        enabled: true\r\n        loginUsername: admin\r\n        loginPassword: 123456\r\n    dynamic:\r\n      druid:\r\n        initial-size: 5\r\n        min-idle: 5\r\n        maxActive: 20\r\n        maxWait: 60000\r\n        timeBetweenEvictionRunsMillis: 60000\r\n        minEvictableIdleTimeMillis: 300000\r\n        validationQuery: SELECT 1 FROM DUAL\r\n        testWhileIdle: true\r\n        testOnBorrow: false\r\n        testOnReturn: false\r\n        poolPreparedStatements: true\r\n        maxPoolPreparedStatementPerConnectionSize: 20\r\n        filters: stat,slf4j\r\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\r\n      datasource:\r\n          # 主库数据源\r\n          master:\r\n            driver-class-name: com.mysql.cj.jdbc.Driver\r\n            url: jdbc:mysql://localhost:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\r\n            username: root\r\n            password: 123456\r\n          # 从库数据源\r\n          # slave:\r\n            # username: \r\n            # password: \r\n            # url: \r\n            # driver-class-name: \r\n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\r\n\r\n# seata配置\r\nseata:\r\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\r\n  enabled: false\r\n  # Seata 应用编号，默认为 ${spring.application.name}\r\n  application-id: ${spring.application.name}\r\n  # Seata 事务组编号，用于 TC 集群名\r\n  tx-service-group: ${spring.application.name}-group\r\n  # 关闭自动代理\r\n  enable-auto-data-source-proxy: false\r\n  # 服务配置项\r\n  service:\r\n    # 虚拟组和分组的映射\r\n    vgroup-mapping:\r\n      ruoyi-system-group: default\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      serverAddr: localhost:8848\r\n      group: SEATA_GROUP\r\n      namespace:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: localhost:8848\r\n      namespace:\r\n\r\n# mybatis配置\r\n# mybatis:\r\n#     # 搜索指定包别名\r\n#     typeAliasesPackage: com.bosch.storagein\r\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\r\n#     mapperLocations: classpath:mapper/**/*.xml\r\n\r\nmybatis-plus:\r\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\r\n#  config-location: classpath:mybatis/mybatis-config.xml\r\n  #实体扫描，多个package用逗号或者分号分隔\r\n  typeAliasesPackage: com.bosch.binin\r\n  global-config:\r\n    #数据库相关配置\r\n    db-config:\r\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\r\n      id-type: AUTO\r\n      logic-delete-value: -1\r\n      logic-not-delete-value: 0\r\n    banner: false\r\n  #原生配置\r\n  configuration:\r\n    map-underscore-to-camel-case: true\r\n    cache-enabled: false\r\n    call-setters-on-nulls: true\r\n    jdbc-type-for-null: \'null\'\r\n# swagger配置\r\nswagger:\r\n  title: 主数据模块接口文档\r\n  license: Powered By ruoyi\r\n  licenseUrl: https://ruoyi.vip', '9a8e417772b049bf91cded85aeb52235', '2022-10-20 09:59:14', '2022-10-20 01:59:14', 'nacos', '117.136.68.154', 'U', '', '');
INSERT INTO `his_config_info` VALUES (64, 77, 'wms-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wms-auth\n          uri: lb://wms-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: wms-system\n          uri: lb://wms-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wms-file\n          uri: lb://wms-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n         # 主数据服务\n        - id: master-data\n          uri: lb://master-data\n          predicates:\n            - Path=/masterdata/**\n          filters:\n            - StripPrefix=1\n         # 入库服务\n        - id: storage-in\n          uri: lb://storage-in\n          predicates:\n            - Path=/storagein/**\n          filters:\n            - StripPrefix=1\n\n\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /csrf\n      - /data/**\n      - /masterdata/*\n      - /supplierInfo/*\n', '16d983d3e6e64ba611e2299a8a073b98', '2022-10-24 14:21:52', '2022-10-24 06:21:52', 'nacos', '117.136.68.21', 'U', '', '');
INSERT INTO `his_config_info` VALUES (56, 78, 'application-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n', 'aaa73b809cfd4d0058893aa13da57806', '2022-10-24 15:12:58', '2022-10-24 07:12:58', 'nacos', '39.144.153.188', 'U', '', '');
INSERT INTO `his_config_info` VALUES (62, 79, 'master-data-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.masterdata\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '6ae5ad80e52eb633d26dfb47c8424c70', '2022-10-24 15:26:19', '2022-10-24 07:26:20', 'nacos', '39.144.153.188', 'U', '', '');
INSERT INTO `his_config_info` VALUES (67, 80, 'storage-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.storagein\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', 'e2fd84c31b3f4dade72fd2dfef4d4e69', '2022-10-24 15:31:09', '2022-10-24 07:31:10', 'nacos', '39.144.153.188', 'U', '', '');
INSERT INTO `his_config_info` VALUES (71, 81, 'bin-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.binin\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip', '0d7df6206b667b2b234e262e678a3e95', '2022-10-24 15:31:33', '2022-10-24 07:31:34', 'nacos', '39.144.153.188', 'U', '', '');
INSERT INTO `his_config_info` VALUES (69, 82, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://121.36.136.109:9889/\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', '3880b06397b69edcf0ef206f63914f8f', '2022-10-24 16:09:23', '2022-10-24 08:09:24', 'nacos', '117.136.68.21', 'U', '', '');
INSERT INTO `his_config_info` VALUES (69, 83, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://121.36.136.109:9800/\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', '56860f8029eb7f83a46b6f6db1138941', '2022-10-26 13:45:58', '2022-10-26 05:45:59', 'nacos', '117.136.67.208', 'U', '', '');
INSERT INTO `his_config_info` VALUES (67, 84, 'storage-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.storagein\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n\npagehelper: \n  pageSizeZero: true', '4e224086357674db1e8bb3516bf911aa', '2022-10-26 13:46:15', '2022-10-26 05:46:16', 'nacos', '117.136.67.208', 'U', '', '');
INSERT INTO `his_config_info` VALUES (62, 85, 'master-data-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.masterdata\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\npagehelper: \n  pageSizeZero: true', 'ba64353fd13152276a8cb762f09d4a50', '2022-10-26 13:46:34', '2022-10-26 05:46:35', 'nacos', '117.136.67.208', 'U', '', '');
INSERT INTO `his_config_info` VALUES (67, 86, 'storage-in-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.storagein\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\n\npagehelper: \n  pageSizeZero: true', 'bc2560f535ed50994f3aa66de87d0aeb', '2022-10-26 14:50:21', '2022-10-26 06:50:22', 'nacos', '117.136.67.208', 'U', '', '');
INSERT INTO `his_config_info` VALUES (69, 87, 'wms-file-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.storagein\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\n\n#mybatis\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n# 本地文件上传    \nfile:\n    domain: http://127.0.0.1:9300\n    path: D:/ruoyi/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://121.36.136.109:9800/\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: wms\n  downloadPath: /2022/10/16/\n\n', '1e5cc2edfdd2f54db8e7dfc6957e694e', '2022-10-26 14:50:38', '2022-10-26 06:50:38', 'nacos', '117.136.67.208', 'U', '', '');
INSERT INTO `his_config_info` VALUES (62, 88, 'master-data-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring: \n  redis:\n    host: 121.36.136.109\n    port: 6379\n    password: inst!\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: admin\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://121.36.136.109:3306/wms-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true\n            username: root\n            password: inst!\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: 121.36.136.109:8848\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: 121.36.136.109:8848\n      namespace:\n\n# # mybatis配置\n# mybatis:\n#     # 搜索指定包别名\n#     typeAliasesPackage: com.bosch.masterdata\n#     # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#     mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations: classpath*:mapper/**/*Mapper.xml\n#  config-location: classpath:mybatis/mybatis-config.xml\n  #实体扫描，多个package用逗号或者分号分隔\n  typeAliasesPackage: com.bosch.masterdata\n  global-config:\n    #数据库相关配置\n    db-config:\n      #主键类型  AUTO:\"数据库ID自增\", INPUT:\"用户输入ID\", ID_WORKER:\"全局唯一ID (数字类型唯一ID)\", UUID:\"全局唯一ID UUID\";\n      id-type: AUTO\n      logic-delete-value: -1\n      logic-not-delete-value: 0\n    banner: false\n  #原生配置\n  configuration:\n    map-underscore-to-camel-case: true\n    cache-enabled: false\n    call-setters-on-nulls: true\n    jdbc-type-for-null: \'null\'\n\n# swagger配置\nswagger:\n  title: 主数据模块接口文档\n  license: Powered By ruoyi\n  licenseUrl: https://ruoyi.vip\n\npagehelper: \n  pageSizeZero: true', '0349698ea382edd26b3172b22b133c71', '2022-10-26 14:51:13', '2022-10-26 06:51:13', 'nacos', '117.136.67.208', 'U', '', '');
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL,
  `resource` varchar(255) NOT NULL,
  `action` varchar(8) NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;







/*
 Navicat Premium Data Transfer

 Source Server         : 122.112.221.123
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 122.112.221.123:3306
 Source Schema         : wms-cloud

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 03/11/2022 13:04:37
*/

create database if not exists wms-cloud;

use wms-cloud;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bi_in
-- ----------------------------
DROP TABLE IF EXISTS `bi_in`;
CREATE TABLE `bi_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sscc_number` varchar(50) DEFAULT NULL COMMENT 'SSCC码',
  `material_nb` varchar(50) NOT NULL DEFAULT '' COMMENT '物料号',
  `batch_nb` varchar(50) NOT NULL DEFAULT '' COMMENT '批次号',
  `expire_date` date NOT NULL COMMENT '过期时间',
  `recommend_bin_code` varchar(50) DEFAULT NULL COMMENT '推荐库位',
  `recommend_frame_id` bigint(20) DEFAULT NULL COMMENT '推荐跨id',
  `recommend_frame_code` varchar(50) DEFAULT NULL COMMENT '推荐跨code',
  `actual_bin_code` varchar(50) DEFAULT NULL COMMENT '实际库位',
  `actual_frame_id` bigint(20) DEFAULT NULL COMMENT '实际跨id',
  `actual_frame_code` varchar(50) DEFAULT NULL COMMENT '实际跨code',
  `quantity` bigint(20) NOT NULL DEFAULT '0' COMMENT '数量',
  `ware_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `pallet_code` varchar(50) NOT NULL DEFAULT '' COMMENT '托盘编码',
  `pallet_type` varchar(50) NOT NULL DEFAULT '' COMMENT '托盘类型',
  `status` int(11) NOT NULL COMMENT '状态(0：待上架,1:已上架)',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `move_type` varchar(255) DEFAULT NULL COMMENT '移动类型',
  `delete_flag` int(11) DEFAULT '0' COMMENT '删除标记1：删除，0:可用',
  `parent_id` int(8) DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='上架表';

-- ----------------------------
-- Records of bi_in
-- ----------------------------
BEGIN;
INSERT INTO `bi_in` VALUES (14, '669006391110024585', '10302507', '2202141190', '2025-02-13', 'WA.1.041.6', 6, 'WA.1.001', NULL, 6, 'WA.1.001', 1000, NULL, 'A-001', 'A', 1, 'admin', 'admin', '2022-10-25 17:00:23', '2022-10-25 17:10:46', NULL, NULL, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for bi_stock
-- ----------------------------
DROP TABLE IF EXISTS `bi_stock`;
CREATE TABLE `bi_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sscc_number` varchar(50) DEFAULT NULL COMMENT 'sscc码',
  `ware_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `frame_code` varchar(255) DEFAULT NULL COMMENT '跨编码',
  `bin_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `material_nb` varchar(50) DEFAULT NULL COMMENT '物料号',
  `batch_nb` varchar(50) DEFAULT NULL COMMENT '批次号',
  `expire_date` date DEFAULT NULL COMMENT '过期时间',
  `total_stock` bigint(20) DEFAULT NULL COMMENT '总库存',
  `freeze_stock` bigint(11) DEFAULT NULL COMMENT '冻结库存',
  `available_stock` bigint(11) DEFAULT NULL COMMENT '可用库存',
  `bin_in_id` int(11) DEFAULT NULL COMMENT '入库id',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `from_purchase_order` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '来源PO号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='库存表';

-- ----------------------------
-- Records of bi_stock
-- ----------------------------
BEGIN;
INSERT INTO `bi_stock` VALUES (9, '669006391110024585', NULL, NULL, NULL, '10302507', '2202141190', '2025-02-13', 1000, NULL, NULL, 14, 'admin', NULL, '2022-10-25 17:10:46', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for bi_stock_log
-- ----------------------------
DROP TABLE IF EXISTS `bi_stock_log`;
CREATE TABLE `bi_stock_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sscc_number` varchar(50) DEFAULT NULL COMMENT 'sscc码',
  `ware_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `frame_code` varchar(255) DEFAULT NULL COMMENT '跨编码',
  `bin_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `material_nb` varchar(50) DEFAULT NULL COMMENT '物料号',
  `batch_nb` varchar(50) DEFAULT NULL COMMENT '批次号',
  `expire_date` date DEFAULT NULL COMMENT '过期时间',
  `total_stock` bigint(20) DEFAULT NULL COMMENT '总库存',
  `freeze_stock` bigint(11) DEFAULT NULL COMMENT '冻结库存',
  `available_stock` bigint(11) DEFAULT NULL COMMENT '可用库存',
  `bin_in_id` int(11) DEFAULT NULL COMMENT '入库id',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `from_purchase_order` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '来源PO号',
  `move_type` varchar(255) DEFAULT NULL COMMENT '移动类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='库存表';

-- ----------------------------
-- Records of bi_stock_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for file_upload
-- ----------------------------
DROP TABLE IF EXISTS `file_upload`;
CREATE TABLE `file_upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(500) NOT NULL DEFAULT '' COMMENT '文件名称',
  `file_url` varchar(500) NOT NULL DEFAULT '' COMMENT '文件存放在minio的路径',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `create_by` varchar(50) NOT NULL DEFAULT 'now()' COMMENT '上传人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COMMENT='文件上传记录表';

-- ----------------------------
-- Records of file_upload
-- ----------------------------
BEGIN;
INSERT INTO `file_upload` VALUES (1, '入库_20221014142139A002.csv', 'http://127.0.0.1:9000/wms/2022/10/14/入库_20221014142139A002.csv', 'MaterialReceive', '2022-10-14 14:21:53', 'admin');
INSERT INTO `file_upload` VALUES (3, '1', '2', '3', '2022-10-14 15:29:05', '1');
INSERT INTO `file_upload` VALUES (4, '入库_20221014160848A001.csv', 'http://127.0.0.1:9000/wms/2022/10/14/入库_20221014160848A001.csv', '', '2022-10-14 16:08:55', 'admin');
INSERT INTO `file_upload` VALUES (5, '入库_20221014161230A001.csv', 'http://127.0.0.1:9000/wms/2022/10/14/入库_20221014161230A001.csv', '', '2022-10-14 16:12:32', 'admin');
INSERT INTO `file_upload` VALUES (6, '入库_20221016153910A001.csv', 'http://127.0.0.1:9000/wms/2022/10/16/入库_20221016153910A001.csv', '', '2022-10-16 15:39:11', 'admin');
INSERT INTO `file_upload` VALUES (7, '入库_20221017133947A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017133947A001.csv', '', '2022-10-17 13:39:49', 'admin');
INSERT INTO `file_upload` VALUES (8, '入库_20221017134044A002.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017134044A002.csv', '', '2022-10-17 13:40:45', 'admin');
INSERT INTO `file_upload` VALUES (9, '入库_20221017134215A003.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017134215A003.csv', '', '2022-10-17 13:42:16', 'admin');
INSERT INTO `file_upload` VALUES (10, '入库_20221017134454A004.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017134454A004.csv', '', '2022-10-17 13:44:54', 'admin');
INSERT INTO `file_upload` VALUES (11, '入库_20221017134819A005.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017134819A005.csv', '', '2022-10-17 13:48:20', 'admin');
INSERT INTO `file_upload` VALUES (12, '入库_20221017134901A006.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017134901A006.csv', '', '2022-10-17 13:49:02', 'admin');
INSERT INTO `file_upload` VALUES (13, '入库_20221017135258A007.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017135258A007.csv', '', '2022-10-17 13:52:58', 'admin');
INSERT INTO `file_upload` VALUES (14, '入库_20221017135619A008.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017135619A008.csv', '', '2022-10-17 13:56:19', 'admin');
INSERT INTO `file_upload` VALUES (15, '入库_20221017135700A009.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017135700A009.csv', '', '2022-10-17 13:57:00', 'admin');
INSERT INTO `file_upload` VALUES (16, '入库_20221017161120A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017161120A001.csv', '', '2022-10-17 16:11:21', 'admin');
INSERT INTO `file_upload` VALUES (17, '入库_20221017162401A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017162401A001.csv', '', '2022-10-17 16:24:02', 'admin');
INSERT INTO `file_upload` VALUES (18, '入库_20221017162738A002.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017162738A002.csv', '', '2022-10-17 16:27:38', 'admin');
INSERT INTO `file_upload` VALUES (19, '入库_20221017164329A003.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017164329A003.csv', '', '2022-10-17 16:43:30', 'admin');
INSERT INTO `file_upload` VALUES (20, '入库_20221017165019A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017165019A001.csv', '', '2022-10-17 16:50:20', 'admin');
INSERT INTO `file_upload` VALUES (21, '入库_20221017165556A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017165556A001.csv', '', '2022-10-17 16:55:56', 'admin');
INSERT INTO `file_upload` VALUES (22, '入库_20221017165855A001.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017165855A001.csv', '', '2022-10-17 16:58:56', 'admin');
INSERT INTO `file_upload` VALUES (23, '入库_20221017165921A002.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017165921A002.csv', '', '2022-10-17 16:59:21', 'admin');
INSERT INTO `file_upload` VALUES (24, '入库_20221017170103A003.csv', 'http://127.0.0.1:9000/wms/2022/10/17/入库_20221017170103A003.csv', '', '2022-10-17 17:01:04', 'admin');
INSERT INTO `file_upload` VALUES (25, '入库_20221018091543A001.csv', 'http://127.0.0.1:9000/wms/2022/10/18/入库_20221018091543A001.csv', '', '2022-10-18 09:15:44', 'admin');
INSERT INTO `file_upload` VALUES (26, '入库_20221018092113A001.csv', 'http://127.0.0.1:9000/wms/2022/10/18/入库_20221018092113A001.csv', '', '2022-10-18 09:21:14', 'admin');
INSERT INTO `file_upload` VALUES (27, '供应商1_20221018151419A001.csv', 'http://127.0.0.1:9000/wms/2022/10/18/供应商1_20221018151419A001.csv', '', '2022-10-18 15:14:20', 'admin');
INSERT INTO `file_upload` VALUES (28, 'Tekdan收货下载模板_20221019131506A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019131506A001.csv', '', '2022-10-19 13:15:07', 'admin');
INSERT INTO `file_upload` VALUES (29, 'Tekdan收货下载模板_20221019131531A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019131531A002.csv', '', '2022-10-19 13:15:31', 'admin');
INSERT INTO `file_upload` VALUES (30, 'Tekdan收货下载模板_20221019131710A003.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019131710A003.csv', '', '2022-10-19 13:17:10', 'admin');
INSERT INTO `file_upload` VALUES (31, '入库_20221019131825A004.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019131825A004.csv', '', '2022-10-19 13:18:26', 'admin');
INSERT INTO `file_upload` VALUES (32, 'Tekdan收货下载模板_20221019132051A005.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019132051A005.csv', '', '2022-10-19 13:20:52', 'admin');
INSERT INTO `file_upload` VALUES (33, 'Tekdan收货下载模板_20221019133241A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019133241A001.csv', '', '2022-10-19 13:32:42', 'admin');
INSERT INTO `file_upload` VALUES (34, 'Tekdan收货下载模板_20221019133559A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019133559A001.csv', '', '2022-10-19 13:36:00', 'admin');
INSERT INTO `file_upload` VALUES (35, 'Tekdan收货下载模板_20221019134009A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019134009A001.csv', '', '2022-10-19 13:40:10', 'admin');
INSERT INTO `file_upload` VALUES (36, '入库_20221019134104A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019134104A002.csv', '', '2022-10-19 13:41:04', 'admin');
INSERT INTO `file_upload` VALUES (37, '入库_20221019134339A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019134339A001.csv', '', '2022-10-19 13:43:40', 'admin');
INSERT INTO `file_upload` VALUES (38, '入库_20221016153910A001_20221019134414A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221016153910A001_20221019134414A002.csv', '', '2022-10-19 13:44:15', 'admin');
INSERT INTO `file_upload` VALUES (39, '入库_20221019134947A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019134947A001.csv', '', '2022-10-19 13:49:47', 'admin');
INSERT INTO `file_upload` VALUES (40, '入库_20221019135055A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019135055A001.csv', '', '2022-10-19 13:50:55', 'admin');
INSERT INTO `file_upload` VALUES (41, '入库_20221019135619A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019135619A001.csv', '', '2022-10-19 13:56:20', 'admin');
INSERT INTO `file_upload` VALUES (42, 'Tekdan收货下载模板_20221019135643A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019135643A002.csv', '', '2022-10-19 13:56:44', 'admin');
INSERT INTO `file_upload` VALUES (43, 'Tekdan收货下载模板_20221019140046A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019140046A001.csv', '', '2022-10-19 14:00:47', 'admin');
INSERT INTO `file_upload` VALUES (44, 'Tekdan收货下载模板_20221019140624A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019140624A002.csv', '', '2022-10-19 14:06:25', 'admin');
INSERT INTO `file_upload` VALUES (45, 'Tekdan收货下载模板_20221019140758A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019140758A001.csv', '', '2022-10-19 14:07:59', 'admin');
INSERT INTO `file_upload` VALUES (46, 'Tekdan收货下载模板_20221019140831A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019140831A002.csv', '', '2022-10-19 14:08:31', 'admin');
INSERT INTO `file_upload` VALUES (47, 'Tekdan收货下载模板_20221019141032A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019141032A001.csv', '', '2022-10-19 14:10:33', 'admin');
INSERT INTO `file_upload` VALUES (48, 'Tekdan收货下载模板_20221019141509A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019141509A001.csv', '', '2022-10-19 14:15:09', 'admin');
INSERT INTO `file_upload` VALUES (49, 'Tekdan收货下载模板_20221019141806A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019141806A001.csv', '', '2022-10-19 14:18:07', 'admin');
INSERT INTO `file_upload` VALUES (50, 'Tekdan收货下载模板_20221019142407A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019142407A001.csv', '', '2022-10-19 14:24:08', 'admin');
INSERT INTO `file_upload` VALUES (51, 'Tekdan收货下载模板_20221019142604A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019142604A002.csv', '', '2022-10-19 14:26:04', 'admin');
INSERT INTO `file_upload` VALUES (52, 'Tekdan收货下载模板_20221019142738A003.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019142738A003.csv', '', '2022-10-19 14:27:38', 'admin');
INSERT INTO `file_upload` VALUES (53, 'Tekdan收货下载模板_20221019142807A004.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019142807A004.csv', '', '2022-10-19 14:28:07', 'admin');
INSERT INTO `file_upload` VALUES (54, 'Tekdan收货下载模板_20221019142911A005.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019142911A005.csv', '', '2022-10-19 14:29:11', 'admin');
INSERT INTO `file_upload` VALUES (55, 'Tekdan收货下载模板_20221019143528A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019143528A001.csv', '', '2022-10-19 14:35:29', 'admin');
INSERT INTO `file_upload` VALUES (56, 'Tekdan收货下载模板_20221019144048A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019144048A001.csv', '', '2022-10-19 14:40:49', 'admin');
INSERT INTO `file_upload` VALUES (57, 'Tekdan收货下载模板_20221019144332A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019144332A001.csv', '', '2022-10-19 14:43:33', 'admin');
INSERT INTO `file_upload` VALUES (58, 'Tekdan收货下载模板_20221019145327A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019145327A001.csv', '', '2022-10-19 14:53:28', 'admin');
INSERT INTO `file_upload` VALUES (59, 'Tekdan收货下载模板_20221019145428A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019145428A001.csv', '', '2022-10-19 14:54:29', 'admin');
INSERT INTO `file_upload` VALUES (60, 'Tekdan收货下载模板_20221019145453A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019145453A002.csv', '', '2022-10-19 14:54:54', 'admin');
INSERT INTO `file_upload` VALUES (61, 'Tekdan收货下载模板_20221019145817A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/Tekdan收货下载模板_20221019145817A001.csv', '', '2022-10-19 14:58:18', 'admin');
INSERT INTO `file_upload` VALUES (62, '入库_20221019152412A001.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019152412A001.csv', '', '2022-10-19 15:24:13', 'admin');
INSERT INTO `file_upload` VALUES (63, '入库_20221019152421A002.csv', 'http://127.0.0.1:9000/wms/2022/10/19/入库_20221019152421A002.csv', '', '2022-10-19 15:24:21', 'admin');
INSERT INTO `file_upload` VALUES (64, 'Tekdan收货下载模板1024_20221024162000A001.csv', 'http://121.36.136.109:9800//wms/2022/10/24/Tekdan收货下载模板1024_20221024162000A001.csv', '', '2022-10-24 16:20:01', 'admin');
INSERT INTO `file_upload` VALUES (65, '入库_20221024162556A002.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162556A002.csv', '', '2022-10-24 16:25:56', 'admin');
INSERT INTO `file_upload` VALUES (66, '入库_20221024162603A003.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162603A003.csv', '', '2022-10-24 16:26:04', 'admin');
INSERT INTO `file_upload` VALUES (67, '入库_20221024162607A004.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162607A004.csv', '', '2022-10-24 16:26:08', 'admin');
INSERT INTO `file_upload` VALUES (68, '入库_20221024162616A005.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162616A005.csv', '', '2022-10-24 16:26:17', 'admin');
INSERT INTO `file_upload` VALUES (69, '入库_20221024162619A006.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162619A006.csv', '', '2022-10-24 16:26:19', 'admin');
INSERT INTO `file_upload` VALUES (70, '入库_20221024162758A007.csv', 'http://121.36.136.109:9800//wms/2022/10/24/入库_20221024162758A007.csv', '', '2022-10-24 16:27:59', 'admin');
INSERT INTO `file_upload` VALUES (71, 'Tekdan 收货导入_20221024163354A008.csv', 'http://121.36.136.109:9800//wms/2022/10/24/Tekdan 收货导入_20221024163354A008.csv', '', '2022-10-24 16:33:54', 'admin');
INSERT INTO `file_upload` VALUES (72, 'Tekdan 收货导入_20221024163500A009.csv', 'http://121.36.136.109:9800//wms/2022/10/24/Tekdan 收货导入_20221024163500A009.csv', '', '2022-10-24 16:35:00', 'admin');
INSERT INTO `file_upload` VALUES (73, 'MovementLogList0906_20221025132147A003.xls', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906_20221025132147A003.xls', '', '2022-10-25 13:21:54', 'admin');
INSERT INTO `file_upload` VALUES (74, 'MovementLogList0906_20221025132221A004.xls', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906_20221025132221A004.xls', '', '2022-10-25 13:22:22', 'admin');
INSERT INTO `file_upload` VALUES (75, 'MovementLogList0906 (003) (version 1) (1)_20221025133712A010.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025133712A010.xlsx', '', '2022-10-25 13:37:14', 'admin');
INSERT INTO `file_upload` VALUES (76, 'MovementLogList0906 (003) (version 1) (1)_20221025133823A011.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025133823A011.xlsx', '', '2022-10-25 13:38:28', 'admin');
INSERT INTO `file_upload` VALUES (77, 'MovementLogList0906 (003) (version 1) (1)_20221025133837A012.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025133837A012.xlsx', '', '2022-10-25 13:38:38', 'admin');
INSERT INTO `file_upload` VALUES (78, 'MovementLogList0906 (003) (version 1) (1)_20221025133946A013.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025133946A013.xlsx', '', '2022-10-25 13:39:46', 'admin');
INSERT INTO `file_upload` VALUES (79, 'MovementLogList0906 (003) (version 1) (1)_20221025134002A014.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025134002A014.xlsx', '', '2022-10-25 13:40:02', 'admin');
INSERT INTO `file_upload` VALUES (80, 'MovementLogList0906 (003) (version 1) (1)_20221025134457A015.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025134457A015.xlsx', '', '2022-10-25 13:44:57', 'admin');
INSERT INTO `file_upload` VALUES (81, 'MovementLogList0906 (003) (version 1) (1)_20221025135549A016.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025135549A016.xlsx', '', '2022-10-25 13:55:50', 'admin');
INSERT INTO `file_upload` VALUES (82, 'MovementLogList0906 (003) (version 1) (1)_20221025135612A017.xlsx', 'http://127.0.0.1:9000/wms/2022/10/25/MovementLogList0906 (003) (version 1) (1)_20221025135612A017.xlsx', '', '2022-10-25 13:56:12', 'admin');
INSERT INTO `file_upload` VALUES (83, '入库_20221026102827A001.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026102827A001.xlsx', '', '2022-10-26 10:28:28', 'admin');
INSERT INTO `file_upload` VALUES (84, '入库_20221026102834A002.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026102834A002.xlsx', '', '2022-10-26 10:28:34', 'admin');
INSERT INTO `file_upload` VALUES (85, '入库_20221026103106A003.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026103106A003.xlsx', '', '2022-10-26 10:31:07', 'admin');
INSERT INTO `file_upload` VALUES (86, '入库_20221026112105A001.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026112105A001.xlsx', '', '2022-10-26 11:21:06', 'admin');
INSERT INTO `file_upload` VALUES (87, '入库_20221026112235A002.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026112235A002.xlsx', '', '2022-10-26 11:22:35', 'admin');
INSERT INTO `file_upload` VALUES (88, '入库_20221026133721A001.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026133721A001.xlsx', '', '2022-10-26 13:37:23', 'admin');
INSERT INTO `file_upload` VALUES (89, '入库_20221026133821A001.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026133821A001.xlsx', '', '2022-10-26 13:38:22', 'admin');
INSERT INTO `file_upload` VALUES (90, '入库_20221026152322A002.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026152322A002.xlsx', '', '2022-10-26 15:23:23', 'admin');
INSERT INTO `file_upload` VALUES (91, '入库_20221026153358A001.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026153358A001.xlsx', '', '2022-10-26 15:34:02', 'admin');
INSERT INTO `file_upload` VALUES (92, '入库_20221026153643A002.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026153643A002.xlsx', '', '2022-10-26 15:36:44', 'admin');
INSERT INTO `file_upload` VALUES (93, '入库_20221026153647A003.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026153647A003.xlsx', '', '2022-10-26 15:36:47', 'admin');
INSERT INTO `file_upload` VALUES (94, '入库_20221026153754A004.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026153754A004.xlsx', '', '2022-10-26 15:37:55', 'admin');
INSERT INTO `file_upload` VALUES (95, '入库_20221026153756A005.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026153756A005.xlsx', '', '2022-10-26 15:37:56', 'admin');
INSERT INTO `file_upload` VALUES (96, '入库_20221026155107A004.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026155107A004.xlsx', '', '2022-10-26 15:51:08', 'admin');
INSERT INTO `file_upload` VALUES (97, '入库_20221026155048A003.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026155048A003.xlsx', '', '2022-10-26 15:50:49', 'admin');
INSERT INTO `file_upload` VALUES (98, '入库_20221026155127A005.xlsx', 'http://127.0.0.1:9000/wms/2022/10/26/入库_20221026155127A005.xlsx', '', '2022-10-26 15:51:27', 'admin');
INSERT INTO `file_upload` VALUES (99, '入库_20221026155431A006.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026155431A006.xlsx', '', '2022-10-26 15:54:32', 'admin');
INSERT INTO `file_upload` VALUES (100, '入库_20221026155503A007.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026155503A007.xlsx', '', '2022-10-26 15:55:04', 'admin');
INSERT INTO `file_upload` VALUES (101, '入库_20221026155538A008.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026155538A008.xlsx', '', '2022-10-26 15:55:38', 'admin');
INSERT INTO `file_upload` VALUES (102, '入库_20221026155540A009.xlsx', 'http://121.36.136.109:9800//wms/2022/10/26/入库_20221026155540A009.xlsx', '', '2022-10-26 15:55:40', 'admin');
INSERT INTO `file_upload` VALUES (103, '入库导入_20221027132716A010.xlsx', 'http://121.36.136.109:9800//wms/2022/10/27/入库导入_20221027132716A010.xlsx', '', '2022-10-27 13:27:17', 'admin');
INSERT INTO `file_upload` VALUES (104, '入库导入_20221027133938A011.xlsx', 'http://121.36.136.109:9800//wms/2022/10/27/入库导入_20221027133938A011.xlsx', '', '2022-10-27 13:39:38', 'admin');
INSERT INTO `file_upload` VALUES (105, '入库导入_20221027134306A012.xlsx', 'http://121.36.136.109:9800//wms/2022/10/27/入库导入_20221027134306A012.xlsx', '', '2022-10-27 13:43:06', 'admin');
INSERT INTO `file_upload` VALUES (106, '入库导入_20221027153636A013.xlsx', 'http://121.36.136.109:9800//wms/2022/10/27/入库导入_20221027153636A013.xlsx', '', '2022-10-27 15:36:37', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
INSERT INTO `gen_table` VALUES (51, 'md_area', '区域表', NULL, NULL, 'Area', 'crud', 'com.bosch.masterdata', 'masterdata', 'area', '区域', 'xuhao', '0', '/', NULL, 'admin', '2022-09-26 10:37:06', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (52, 'md_bin', '库位表', NULL, NULL, 'Bin', 'crud', 'com.bosch.masterdata', 'masterdata', 'bin', '库位', 'xuhao', '0', '/', NULL, 'admin', '2022-09-26 10:37:08', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (53, 'md_frame', '跨表', NULL, NULL, 'Frame', 'crud', 'com.bosch.masterdata', 'masterdata', 'frame', '跨', 'xuhao', '0', '/', NULL, 'admin', '2022-09-26 10:37:09', '', NULL, NULL);
INSERT INTO `gen_table` VALUES (54, 'md_ware', '仓库表', NULL, NULL, 'Ware', 'crud', 'com.bosch.masterdata', 'masterdata', 'ware', '仓库', 'xuhao', '0', '/', NULL, 'admin', '2022-09-26 10:37:10', '', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=371 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
INSERT INTO `gen_table_column` VALUES (329, '51', 'id', 'id', 'int(10)', 'Integer', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (330, '51', 'code', '存储区编码', 'varchar(10)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (331, '51', 'name', '存储区名称', 'varchar(20)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (332, '51', 'ware_id', '仓库id', 'int(10)', 'Integer', 'wareId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (333, '51', 'create_by', '创建人', 'varchar(11)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (334, '51', 'update_by', '修改人', 'varchar(11)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (335, '51', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (336, '51', 'update_time', '修改时间', 'timestamp', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (337, '51', 'status', '状态（1：启用，0：停用）', 'int(11)', 'Long', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (338, '51', 'remark', '备注', 'varchar(50)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2022-09-26 10:37:07', '', NULL);
INSERT INTO `gen_table_column` VALUES (339, '52', 'id', 'id', 'int(11)', 'Long', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (340, '52', 'frame_id', '跨id', 'int(11)', 'Long', 'frameId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (341, '52', 'name', '库位名称', 'varchar(50)', 'String', 'name', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (342, '52', 'code', '库位编码', 'varchar(10)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (343, '52', 'create_by', '创建人', 'int(11)', 'Long', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (344, '52', 'update_by', '修改人', 'int(11)', 'Long', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (345, '52', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (346, '52', 'update_time', '修改时间', 'timestamp', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (347, '52', 'status', '状态（1：启用，0：停用）', 'int(11)', 'Long', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2022-09-26 10:37:08', '', NULL);
INSERT INTO `gen_table_column` VALUES (348, '52', 'remark', '备注', 'varchar(50)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (349, '53', 'id', 'id', 'int(11)', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (350, '53', 'area_id', '区域id', 'int(11)', 'Long', 'areaId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (351, '53', 'name', '跨名称', 'varchar(50)', 'String', 'name', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (352, '53', 'code', '跨编码', 'varchar(10)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (353, '53', 'width', '宽度', 'decimal(10,2)', 'BigDecimal', 'width', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (354, '53', 'bear_weight', '承重', 'decimal(10,2)', 'BigDecimal', 'bearWeight', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (355, '53', 'create_by', '创建人', 'int(11)', 'Long', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (356, '53', 'update_by', '修改人', 'int(11)', 'Long', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2022-09-26 10:37:09', '', NULL);
INSERT INTO `gen_table_column` VALUES (357, '53', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (358, '53', 'update_time', '修改时间', 'timestamp', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (359, '53', 'status', '状态（1：启用，0：停用）', 'int(11)', 'Long', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 11, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (360, '53', 'remark', '备注', 'varchar(50)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 12, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (361, '54', 'id', 'id', 'varchar(10)', 'String', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (362, '54', 'code', '仓库编码', 'varchar(10)', 'String', 'code', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (363, '54', 'name', '仓库名称', 'varchar(10)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 3, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (364, '54', 'location', '仓库地址', 'varchar(100)', 'String', 'location', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (365, '54', 'create_by', '创建人', 'varchar(11)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2022-09-26 10:37:10', '', NULL);
INSERT INTO `gen_table_column` VALUES (366, '54', 'update_by', '修改人', 'varchar(11)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2022-09-26 10:37:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (367, '54', 'create_time', '创建时间', 'timestamp', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2022-09-26 10:37:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (368, '54', 'update_time', '修改时间', 'timestamp', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2022-09-26 10:37:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (369, '54', 'status', '状态（1：启用，0：停用）', 'int(11)', 'Long', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2022-09-26 10:37:11', '', NULL);
INSERT INTO `gen_table_column` VALUES (370, '54', 'remark', '备注', 'varchar(50)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'input', '', 10, 'admin', '2022-09-26 10:37:11', '', NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_area
-- ----------------------------
DROP TABLE IF EXISTS `md_area`;
CREATE TABLE `md_area` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(10) NOT NULL COMMENT '存储区编码',
  `name` varchar(20) NOT NULL COMMENT '存储区名称',
  `ware_id` int(10) NOT NULL COMMENT '仓库id',
  `create_by` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='区域表';

-- ----------------------------
-- Records of md_area
-- ----------------------------
BEGIN;
INSERT INTO `md_area` VALUES (5, 'W101', 'W101存储区', 7, '', NULL, '2022-09-27 14:06:46', NULL, 1, NULL);
INSERT INTO `md_area` VALUES (6, 'W102', 'W102存储区', 7, '', NULL, '2022-09-27 14:07:02', NULL, 1, NULL);
INSERT INTO `md_area` VALUES (7, 'W103', 'W103存储区', 7, '', 'admin', '2022-09-27 14:07:19', '2022-10-18 15:56:50', 1, NULL);
INSERT INTO `md_area` VALUES (8, 'W104', 'W104存储区', 7, NULL, 'admin', NULL, '2022-10-18 15:56:50', 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_bin
-- ----------------------------
DROP TABLE IF EXISTS `md_bin`;
CREATE TABLE `md_bin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `frame_id` int(11) NOT NULL COMMENT '跨id',
  `name` varchar(50) DEFAULT NULL COMMENT '库位名称',
  `code` varchar(10) NOT NULL COMMENT '库位编码',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT NULL COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_location_info_frame_id` (`frame_id`) USING BTREE,
  CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='库位表';

-- ----------------------------
-- Records of md_bin
-- ----------------------------
BEGIN;
INSERT INTO `md_bin` VALUES (8, 6, 'WA.1.041.1库位', 'WA.1.041.6', 'admin', NULL, '2022-10-18 16:00:57', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_department
-- ----------------------------
DROP TABLE IF EXISTS `md_department`;
CREATE TABLE `md_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(20) NOT NULL COMMENT '部门名称',
  `description` varchar(50) DEFAULT NULL COMMENT '部门描述',
  `code` varchar(30) DEFAULT NULL COMMENT '部门编码',
  `create_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT NULL COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of md_department
-- ----------------------------
BEGIN;
INSERT INTO `md_department` VALUES (1, 'ECN', 'ECN', 'ECN', NULL, NULL, '2022-09-23 09:44:24', '2022-09-23 10:27:12', 1, NULL);
INSERT INTO `md_department` VALUES (2, 'FSMP', 'FSMP', 'FSMP', NULL, NULL, '2022-09-23 09:44:22', NULL, 1, NULL);
INSERT INTO `md_department` VALUES (3, 'NMD', 'NMD', 'NMD', NULL, NULL, '2022-09-23 09:44:59', NULL, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_frame
-- ----------------------------
DROP TABLE IF EXISTS `md_frame`;
CREATE TABLE `md_frame` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `area_id` int(11) DEFAULT NULL COMMENT '区域id',
  `name` varchar(50) DEFAULT NULL COMMENT '跨名称',
  `code` varchar(10) NOT NULL COMMENT '跨编码',
  `width` decimal(10,2) NOT NULL COMMENT '宽度',
  `bear_weight` decimal(10,2) NOT NULL COMMENT '承重',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `type_code` varchar(10) NOT NULL DEFAULT '' COMMENT '跨类型编码',
  `height` decimal(10,2) NOT NULL COMMENT '高度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='跨表';

-- ----------------------------
-- Records of md_frame
-- ----------------------------
BEGIN;
INSERT INTO `md_frame` VALUES (6, 5, 'WA.1.001跨', 'WA.1.001', 3.00, 100.00, '', NULL, '2022-09-27 14:08:18', NULL, 1, NULL, '', 0.00);
INSERT INTO `md_frame` VALUES (7, 5, 'WA.1.002跨', 'WA.1.002', 6.00, 200.00, '', NULL, '2022-09-27 14:08:55', NULL, 1, NULL, '', 0.00);
INSERT INTO `md_frame` VALUES (8, 5, 'WA.1.003跨', 'WA.1.003', 5.00, 300.00, '', NULL, '2022-09-27 14:09:14', NULL, 1, NULL, '', 0.00);
INSERT INTO `md_frame` VALUES (9, 5, 'WA.1.006跨', 'WA.1.006', 1.00, 2.00, NULL, NULL, NULL, NULL, 1, NULL, '', 0.00);
INSERT INTO `md_frame` VALUES (10, 5, 'WA.1.007跨', 'WA.1.007', 1.00, 2.00, 'admin', 'admin', '2022-10-10 16:43:04', '2022-10-18 16:03:24', 1, NULL, '', 0.00);
COMMIT;

-- ----------------------------
-- Table structure for md_material
-- ----------------------------
DROP TABLE IF EXISTS `md_material`;
CREATE TABLE `md_material` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(10) DEFAULT NULL COMMENT '物料类型代码',
  `name` varchar(20) DEFAULT NULL COMMENT '物料名称',
  `min_package_number` int(11) DEFAULT NULL COMMENT '最小包装数量',
  `material_type_id` int(11) DEFAULT NULL COMMENT '物料类型id',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `error_proofing_method` varchar(20) DEFAULT NULL COMMENT '物料防错方式',
  `has_pallet` int(1) DEFAULT '1' COMMENT '是否带托盘',
  `bind_pallet` int(1) DEFAULT '1' COMMENT '是否绑定托盘',
  `package_weight` decimal(20,6) DEFAULT NULL COMMENT '包装重量',
  `pallet_weight` decimal(20,6) DEFAULT NULL COMMENT '托盘重量',
  `total_weight` decimal(20,6) DEFAULT NULL COMMENT '来料总重量（每托）',
  `min_package_net_weight` decimal(10,2) DEFAULT NULL COMMENT '最小包装重量(净重)',
  `less_deviation_ratio` decimal(20,6) DEFAULT NULL COMMENT '最小允许值',
  `more_deviation_ratio` decimal(20,6) DEFAULT NULL COMMENT '最大允许值',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `transfer_weight_ratio` decimal(20,6) DEFAULT NULL COMMENT '转化重量参数',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_item_info_item_type_id` (`material_type_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1154 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物料信息表';

-- ----------------------------
-- Records of md_material
-- ----------------------------
BEGIN;
INSERT INTO `md_material` VALUES (1150, '10302507', '葡萄糖', 1000, 33, 'kg', '称重', 1, 1, 100.000000, 10.000000, 1000.000000, 99.00, 95.000000, 102.000000, 'admin', NULL, '2022-10-24 16:36:46', '2022-10-24 16:41:28', 1.200000, 1, 'sad ');
INSERT INTO `md_material` VALUES (1152, '10311042', '一次性注射器', 1000, 33, 'PCS', '数数', 1, 1, 100.000000, 10.000000, 1000.000000, 1.00, 195.000000, 202.000000, 'admin', NULL, '2022-10-25 10:35:37', '2022-10-25 10:35:38', 1.200000, 1, NULL);
INSERT INTO `md_material` VALUES (1153, '10311043', '面粉', 1000, 33, NULL, '免检', 1, 1, 100.000000, 10.000000, 1000.000000, 1.00, 200.000000, 250.000000, 'admin', NULL, '2022-10-25 11:19:24', '2022-10-25 11:19:26', 1.200000, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_material_frame
-- ----------------------------
DROP TABLE IF EXISTS `md_material_frame`;
CREATE TABLE `md_material_frame` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `material_id` int(11) NOT NULL COMMENT '物料id',
  `priority_level` int(11) DEFAULT NULL COMMENT '分配至该跨的优先级',
  `frame_type_code` varchar(50) DEFAULT NULL COMMENT '跨类型编码',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(1) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `material_code` varchar(50) DEFAULT NULL COMMENT '物料code',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_item_location_item_id` (`material_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物料跨分配策略表';

-- ----------------------------
-- Records of md_material_frame
-- ----------------------------
BEGIN;
INSERT INTO `md_material_frame` VALUES (7, 8, 10, NULL, '', NULL, '2022-09-27 16:31:51', NULL, 1, NULL, NULL);
INSERT INTO `md_material_frame` VALUES (8, 8, 20, NULL, '', NULL, '2022-09-27 16:32:07', NULL, 1, NULL, NULL);
INSERT INTO `md_material_frame` VALUES (9, 7, NULL, NULL, 'admin', 'admin', '2022-10-19 11:02:46', '2022-10-19 11:04:13', 1, NULL, '10096166');
INSERT INTO `md_material_frame` VALUES (10, 12, NULL, NULL, 'admin', 'admin', '2022-10-19 11:02:46', '2022-10-19 11:04:13', 1, NULL, 'm004');
COMMIT;

-- ----------------------------
-- Table structure for md_material_supplier
-- ----------------------------
DROP TABLE IF EXISTS `md_material_supplier`;
CREATE TABLE `md_material_supplier` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `material_id` int(11) NOT NULL COMMENT '物料id',
  `supplier_id` int(11) NOT NULL COMMENT '供应商id',
  `create_by` int(11) DEFAULT NULL COMMENT '创建人',
  `update_by` int(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT NULL COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_item_supplier_item_info_id` (`material_id`) USING BTREE,
  KEY `fk_item_supplier_supplier_info_id` (`supplier_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='供应商物料表';

-- ----------------------------
-- Records of md_material_supplier
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for md_material_type
-- ----------------------------
DROP TABLE IF EXISTS `md_material_type`;
CREATE TABLE `md_material_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(50) NOT NULL COMMENT '物料类型代码',
  `description` varchar(20) DEFAULT NULL COMMENT '物料类型描述',
  `department_id` int(11) NOT NULL COMMENT '所属部门',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物料类型表';

-- ----------------------------
-- Records of md_material_type
-- ----------------------------
BEGIN;
INSERT INTO `md_material_type` VALUES (20, 'FEE', '出口成品', 1, '', NULL, '2022-09-27 14:02:59', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (22, 'FNE', '出口成品', 2, '', '', '2022-09-27 14:03:36', '2022-09-27 16:31:36', 1, NULL);
INSERT INTO `md_material_type` VALUES (23, 'FEL', 'ECN 国内成品', 1, '', NULL, '2022-10-24 14:35:12', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (24, 'FNL', 'NMD国内成品', 3, '', NULL, '2022-10-24 14:35:58', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (25, 'FFE', 'FSMP出口成品', 2, '', NULL, '2022-10-24 14:36:12', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (26, 'FFL', 'FSMP国内成品', 2, '', NULL, '2022-10-24 14:36:23', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (27, 'REBA', 'ECN保税过敏原原材料', 1, '', NULL, '2022-10-24 14:36:42', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (28, 'RENA', 'ECN非保税过敏原原材料', 1, '', NULL, '2022-10-24 14:37:29', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (29, 'REBN', 'ECN保税非过敏原原材料', 1, '', NULL, '2022-10-24 14:37:40', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (30, 'RENN', 'ECN非保税非过敏原原材料', 1, '', NULL, '2022-10-24 14:37:53', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (31, 'RNB', 'NMD保税原材料', 3, '', NULL, '2022-10-24 14:38:07', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (32, 'RNN', 'NMD非保税原材料', 3, '', NULL, '2022-10-24 14:38:20', NULL, 1, NULL);
INSERT INTO `md_material_type` VALUES (33, 'RFBA', 'FSMP保税过敏原原材料', 2, '', NULL, '2022-10-24 14:38:34', NULL, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_move_type
-- ----------------------------
DROP TABLE IF EXISTS `md_move_type`;
CREATE TABLE `md_move_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(10) NOT NULL COMMENT '移动类型',
  `code` varchar(10) NOT NULL COMMENT '移动类型编码',
  `description` varchar(20) DEFAULT NULL COMMENT '移动类型描述',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='移动类型配置表';

-- ----------------------------
-- Records of md_move_type
-- ----------------------------
BEGIN;
INSERT INTO `md_move_type` VALUES (8, '出库', 'OUT', '出库', '', NULL, '2022-09-27 14:14:16', NULL, 1, NULL);
INSERT INTO `md_move_type` VALUES (9, '成品入库', 'IN', '成品入库', '', NULL, '2022-09-27 14:14:32', NULL, 1, NULL);
INSERT INTO `md_move_type` VALUES (10, '移库', 'MOVE', '移库', '', NULL, '2022-09-27 14:14:43', NULL, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_pallet
-- ----------------------------
DROP TABLE IF EXISTS `md_pallet`;
CREATE TABLE `md_pallet` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(10) NOT NULL COMMENT '托盘类型',
  `length` decimal(10,2) NOT NULL COMMENT '托盘长度',
  `width` decimal(10,2) NOT NULL COMMENT '托盘宽度',
  `height` decimal(10,2) NOT NULL COMMENT '托盘高度',
  `virtual_prefix_code` varchar(10) DEFAULT NULL COMMENT '虚拟托盘前缀编码',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT NULL COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='托盘表';

-- ----------------------------
-- Records of md_pallet
-- ----------------------------
BEGIN;
INSERT INTO `md_pallet` VALUES (5, 'A', 3.00, 2.00, 2.00, 'A001', '', NULL, '2022-09-27 14:11:05', NULL, NULL, NULL);
INSERT INTO `md_pallet` VALUES (6, 'B', 3.00, 2.00, 3.00, 'A002', '', NULL, '2022-09-27 14:11:21', NULL, NULL, NULL);
INSERT INTO `md_pallet` VALUES (7, 'C', 3.00, 3.00, 3.00, 'C001', '', NULL, '2022-09-27 14:11:35', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_supplier_info
-- ----------------------------
DROP TABLE IF EXISTS `md_supplier_info`;
CREATE TABLE `md_supplier_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(10) NOT NULL COMMENT '供应商编码',
  `name` varchar(20) NOT NULL COMMENT '供应商名称',
  `time_window` int(11) NOT NULL COMMENT '供应商时间窗口',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT NULL COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='供应商表';

-- ----------------------------
-- Records of md_supplier_info
-- ----------------------------
BEGIN;
INSERT INTO `md_supplier_info` VALUES (11, '004', '测试1', 60, 'admin', NULL, '2022-10-18 15:52:20', NULL, NULL, NULL);
INSERT INTO `md_supplier_info` VALUES (12, '005', '测试2', 61, 'admin', NULL, '2022-10-18 15:52:20', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_time_window
-- ----------------------------
DROP TABLE IF EXISTS `md_time_window`;
CREATE TABLE `md_time_window` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `start_time` varchar(10) NOT NULL COMMENT '开始时间',
  `end_time` varchar(10) NOT NULL COMMENT '结束时间',
  `window_code` varchar(30) DEFAULT NULL COMMENT '道口编码',
  `create_by` varchar(11) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='道口时间窗口';

-- ----------------------------
-- Records of md_time_window
-- ----------------------------
BEGIN;
INSERT INTO `md_time_window` VALUES (5, '09：00', '12：00', 'A01', NULL, NULL, NULL, NULL, 1, NULL);
INSERT INTO `md_time_window` VALUES (6, '13:00', '14:00', 'B01', NULL, NULL, NULL, NULL, 1, NULL);
INSERT INTO `md_time_window` VALUES (7, '16:00', '18:00', 'C01', NULL, NULL, NULL, NULL, 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for md_ware
-- ----------------------------
DROP TABLE IF EXISTS `md_ware`;
CREATE TABLE `md_ware` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(10) NOT NULL COMMENT '仓库编码',
  `name` varchar(10) NOT NULL COMMENT '仓库名称',
  `location` varchar(100) NOT NULL COMMENT '仓库地址',
  `create_by` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(11) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` int(11) DEFAULT '1' COMMENT '状态（1：启用，0：停用）',
  `remark` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `factory_code` varchar(20) DEFAULT NULL COMMENT '工厂编码',
  `factory_name` varchar(20) DEFAULT NULL COMMENT '工厂名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- ----------------------------
-- Records of md_ware
-- ----------------------------
BEGIN;
INSERT INTO `md_ware` VALUES (7, 'W01', '本地仓库', '纽迪希亚制药（无锡）制药公司', '', NULL, '2022-09-27 14:05:07', NULL, 1, NULL, NULL, NULL);
INSERT INTO `md_ware` VALUES (8, 'W02', '外部仓储中心', '苏南硕放国际机场', '', NULL, '2022-09-27 14:05:48', NULL, 1, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='Blob类型的触发器表';

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='日历信息表';

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='Cron类型的触发器表';

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', '0/10 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', '0/15 * * * * ?', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', '0/20 * * * * ?', 'Asia/Shanghai');
COMMIT;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='已触发的触发器表';

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务详细信息表';

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 'com.ruoyi.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001B636F6D2E72756F79692E6A6F622E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E00097872002B636F6D2E72756F79692E636F6D6D6F6E2E636F72652E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000018353869B1078707400007070707400013174000E302F3130202A202A202A202A203F74001172795461736B2E72794E6F506172616D7374000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000001740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E697A0E58F82EFBC8974000133740001317800);
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 'com.ruoyi.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001B636F6D2E72756F79692E6A6F622E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E00097872002B636F6D2E72756F79692E636F6D6D6F6E2E636F72652E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000018353869B1078707400007070707400013174000E302F3135202A202A202A202A203F74001572795461736B2E7279506172616D7328277279272974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000002740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E69C89E58F82EFBC8974000133740001317800);
INSERT INTO `qrtz_job_details` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 'com.ruoyi.job.util.QuartzDisallowConcurrentExecution', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000174000F5441534B5F50524F504552544945537372001B636F6D2E72756F79692E6A6F622E646F6D61696E2E5379734A6F6200000000000000010200084C000A636F6E63757272656E747400124C6A6176612F6C616E672F537472696E673B4C000E63726F6E45787072657373696F6E71007E00094C000C696E766F6B6554617267657471007E00094C00086A6F6247726F757071007E00094C00056A6F6249647400104C6A6176612F6C616E672F4C6F6E673B4C00076A6F624E616D6571007E00094C000D6D697366697265506F6C69637971007E00094C000673746174757371007E00097872002B636F6D2E72756F79692E636F6D6D6F6E2E636F72652E7765622E646F6D61696E2E42617365456E7469747900000000000000010200074C0008637265617465427971007E00094C000A63726561746554696D657400104C6A6176612F7574696C2F446174653B4C0006706172616D7371007E00034C000672656D61726B71007E00094C000B73656172636856616C756571007E00094C0008757064617465427971007E00094C000A75706461746554696D6571007E000C787074000561646D696E7372000E6A6176612E7574696C2E44617465686A81014B597419030000787077080000018353869B1078707400007070707400013174000E302F3230202A202A202A202A203F74003872795461736B2E72794D756C7469706C65506172616D7328277279272C20747275652C20323030304C2C203331362E3530442C203130302974000744454641554C547372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B02000078700000000000000003740018E7B3BBE7BB9FE9BB98E8AEA4EFBC88E5A49AE58F82EFBC8974000133740001317800);
COMMIT;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='存储的悲观锁信息表';

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'STATE_ACCESS');
INSERT INTO `qrtz_locks` VALUES ('RuoyiScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='暂停的触发器表';

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='调度器状态表';

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_scheduler_state` VALUES ('RuoyiScheduler', 'SZH-C-0058Y1663678430384', 1663678471647, 15000);
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='简单触发器的信息表';

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int(11) DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint(20) DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步机制的行锁表';

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='触发器详细信息表';

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
BEGIN;
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME1', 'DEFAULT', 'TASK_CLASS_NAME1', 'DEFAULT', NULL, 1663678440000, -1, 5, 'PAUSED', 'CRON', 1663678432000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME2', 'DEFAULT', 'TASK_CLASS_NAME2', 'DEFAULT', NULL, 1663678440000, -1, 5, 'PAUSED', 'CRON', 1663678432000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('RuoyiScheduler', 'TASK_CLASS_NAME3', 'DEFAULT', 'TASK_CLASS_NAME3', 'DEFAULT', NULL, 1663678440000, -1, 5, 'PAUSED', 'CRON', 1663678432000, 0, NULL, 2, '');
COMMIT;

-- ----------------------------
-- Table structure for si_material_in
-- ----------------------------
DROP TABLE IF EXISTS `si_material_in`;
CREATE TABLE `si_material_in` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `plant_nb` varchar(255) DEFAULT NULL COMMENT '工厂',
  `ware_code` varchar(255) DEFAULT NULL COMMENT '仓库编码',
  `sscc_number` varchar(255) DEFAULT NULL COMMENT 'SSCC码',
  `batch_nb` varchar(255) DEFAULT NULL COMMENT '批次号',
  `material_nb` varchar(255) DEFAULT NULL COMMENT '物料码',
  `check_type` int(1) DEFAULT NULL COMMENT '检验类型，0：称重，1：数数，2：免检，3：该批次已检',
  `check_quantity` int(11) DEFAULT NULL COMMENT '应检查数量',
  `min_standard` decimal(20,6) DEFAULT NULL COMMENT '最小标准',
  `max_standard` decimal(20,6) DEFAULT NULL COMMENT '最大标准',
  `actual_quantity` bigint(20) DEFAULT NULL COMMENT '实际件数',
  `actual_result` decimal(20,6) DEFAULT NULL COMMENT '实际（称重/数数）结果',
  `average_result` decimal(20,6) DEFAULT NULL COMMENT '实际平均结果',
  `virtual_bin_code` varchar(50) DEFAULT NULL COMMENT '虚拟库位',
  `operate_user` varchar(255) DEFAULT NULL COMMENT '操作人',
  `operate_time` timestamp NULL DEFAULT NULL COMMENT '操作时间',
  `original_pallet_quantity` int(11) DEFAULT NULL COMMENT '原托数',
  `quantity` int(10) DEFAULT NULL COMMENT '数量',
  `delete_flag` int(11) DEFAULT '0' COMMENT '删除标记1：删除，0:可用',
  `move_type` varchar(255) DEFAULT NULL COMMENT '移动类型',
  `from_purchase_order` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '来源PO号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8 COMMENT='原材料入库表';

-- ----------------------------
-- Records of si_material_in
-- ----------------------------
BEGIN;
INSERT INTO `si_material_in` VALUES (90, '7751', 'WA.01', '669006391110024753', '2203291127', '10311043', 2, NULL, NULL, NULL, NULL, NULL, NULL, 'V_001', 'admin', '2022-10-31 20:13:58', NULL, 50, 1, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for si_material_receive
-- ----------------------------
DROP TABLE IF EXISTS `si_material_receive`;
CREATE TABLE `si_material_receive` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `plant_nb` varchar(255) DEFAULT NULL COMMENT '仓库',
  `sscc_number` varchar(255) NOT NULL COMMENT 'SSCC码',
  `material_nb` varchar(255) DEFAULT NULL COMMENT '物料编码',
  `batch_nb` varchar(255) DEFAULT NULL COMMENT '批次号',
  `supplier_nb` varchar(255) DEFAULT NULL COMMENT '供应商',
  `storage_location` varchar(255) DEFAULT NULL COMMENT '存储区域',
  `expire_date` varchar(50) DEFAULT NULL COMMENT 'BBD(过期时间)',
  `quantity` bigint(20) DEFAULT NULL COMMENT '数量',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `from_area` varchar(255) DEFAULT NULL COMMENT '来源区域',
  `from_purchase_order` varchar(255) DEFAULT NULL COMMENT '来源PO号',
  `to_picking_area` varchar(255) DEFAULT NULL COMMENT '目的分拣区域',
  `po_number_item` int(11) DEFAULT NULL COMMENT 'PO行号',
  `upload_user` varchar(255) DEFAULT NULL COMMENT '上传人',
  `upload_time` timestamp NULL DEFAULT NULL COMMENT '上传时间',
  `update_user` varchar(255) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` int(11) DEFAULT '0' COMMENT '删除标记1：删除，0:可用',
  `status` int(1) DEFAULT '0' COMMENT '物料流转状态:0:待入库，1：已入库',
  `file_id` varchar(50) DEFAULT '0' COMMENT '关联文件id',
  `edit_flag` int(8) DEFAULT '0' COMMENT '是否可编辑0:可编辑，1：不可编辑',
  `move_type` varchar(255) DEFAULT NULL COMMENT '移动类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6285 DEFAULT CHARSET=utf8mb4 COMMENT='原材料收货表';

-- ----------------------------
-- Records of si_material_receive
-- ----------------------------
BEGIN;
INSERT INTO `si_material_receive` VALUES (6265, '7751', '669006391110024585', '10302507', '2202141190', '0020189577', '0103', '13/02/2025', 1000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-27 15:47:07', 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6266, '7751', '669006391110024592', '10302507', '2202141190', '0020189577', '0103', '15/02/2025', 5000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-27 15:47:07', 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6267, '7751', '669006391110024608', '10302507', '2202141190', '0020189577', '0103', '15/02/2025', 5000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-27 15:47:07', 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6268, '7751', '669006391110024646', '10302507', '2202141190', '0020189577', '0103', '19/12/2024', 500, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-27 15:47:07', 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6269, '7751', '669006391110024653', '10302507', '2203171192', '0020189577', '0103', '19/12/2024', 500, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6270, '7751', '669006391110024660', '10302507', '2203231193', '0020189577', '0103', '22/03/2025', 6000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6271, '7751', '669006391110024677', '10302507', '2203231193', '0020189577', '0103', '22/03/2025', 4000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6272, '7751', '669006391110024691', '10302507', '2203291194', '0020189577', '0103', '28/03/2025', 3000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6273, '7751', '669006391110024707', '10302507', '2203291194', '0020189577', '0103', '28/03/2025', 2000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6274, '7751', '669006391110024714', '10302507', '2203291195', '0020189577', '0103', '28/03/2025', 1000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6275, '7751', '669006391110024721', '10302507', '2203291196', '0020189577', '0103', '28/03/2025', 1, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6276, '7751', '669006391110024738', '10302507', '2203291200', '0020189577', '0103', '28/03/2025', 2000, 'PC', 'RCP3', '4500017418', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6277, '7751', '669006391110024752', '10311042', '2203291126', '0020137920', '0102', '22/03/2024', 60000, 'PC', 'RCP2', '4500017408', 'RCP2', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-27 15:52:08', 0, 1, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6278, '7751', '669006391110024769', '10302507', '2204221201', '0020189577', '0103', '21/04/2025', 48000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6279, '7751', '669006391110024776', '10302507', '2204221201', '0020189577', '0103', '21/04/2025', 48000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6280, '7751', '669006391110024783', '10302507', '2204221201', '0020189577', '0103', '21/04/2025', 4000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6281, '7751', '669006391110024790', '10302507', '2206221204', '0020189577', '0103', '21/06/2025', 48000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6282, '7751', '669006391110024806', '10302507', '2206221204', '0020189577', '0103', '21/06/2025', 48000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6283, '7751', '669006391110024813', '10302507', '2206221204', '0020189577', '0103', '21/06/2025', 4000, 'PC', 'RCP3', '4500029108', 'RCP3', 10, 'admin', '2022-10-27 15:36:37', NULL, NULL, 0, 0, '106', 0, NULL);
INSERT INTO `si_material_receive` VALUES (6284, '7751', '669006391110024753', '10311043', '2203291127', '0020137920', '0102', '22/03/2024', 50, 'PC', 'RCP2', '4500017409', 'RCP2', 10, 'admin', '2022-10-27 15:36:37', 'admin', '2022-10-31 20:13:58', 0, 1, '106', 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for si_weight
-- ----------------------------
DROP TABLE IF EXISTS `si_weight`;
CREATE TABLE `si_weight` (
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) DEFAULT NULL COMMENT '称编码',
  `weight` double DEFAULT NULL COMMENT '重量',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `status` int(11) DEFAULT '1' COMMENT '状态：0：已使用，1：未使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of si_weight
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='参数配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2022-09-19 10:14:34', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2022-09-19 10:14:34', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2022-09-19 10:14:34', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2022-09-19 10:14:34', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '停用状态');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2022-09-19 10:14:33', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2022-09-19 10:14:34', '', NULL, '登录状态列表');
COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2022-09-19 10:14:34', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2022-09-19 10:14:34', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2022-09-19 10:14:34', '', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='定时任务调度日志表';

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示信息',
  `access_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统访问记录';

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
BEGIN;
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-19 13:50:17');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-19 13:51:51');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-20 10:08:13');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-20 14:11:37');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-20 14:27:13');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-20 14:27:14');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-20 14:27:21');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-20 17:21:13');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-20 19:07:20');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-21 08:57:04');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-21 10:56:50');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 10:43:17');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 15:28:01');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-22 15:49:35');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:01:48');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-22 16:01:50');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:01:58');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-22 16:01:58');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:02:14');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-22 16:02:15');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-22 16:14:53');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:41:35');
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:54:33');
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:56:19');
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:56:40');
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 16:57:05');
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 17:05:54');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 17:06:35');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 17:07:05');
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-22 17:07:28');
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-23 08:42:29');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-23 09:47:56');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-23 09:48:17');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-23 09:50:51');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-23 09:50:56');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-23 09:57:43');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-09-23 09:58:09');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-23 13:19:48');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-24 11:43:37');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-26 08:37:55');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-26 09:04:25');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-26 09:28:49');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-26 13:36:49');
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-26 14:22:07');
INSERT INTO `sys_logininfor` VALUES (144, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-27 08:54:10');
INSERT INTO `sys_logininfor` VALUES (145, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-27 09:05:45');
INSERT INTO `sys_logininfor` VALUES (146, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-27 13:00:38');
INSERT INTO `sys_logininfor` VALUES (147, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-27 13:25:48');
INSERT INTO `sys_logininfor` VALUES (148, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-27 13:46:09');
INSERT INTO `sys_logininfor` VALUES (149, 'admin', '39.144.153.24', '0', '登录成功', '2022-09-27 15:30:00');
INSERT INTO `sys_logininfor` VALUES (150, 'admin', '103.4.125.25', '0', '登录成功', '2022-09-27 15:31:57');
INSERT INTO `sys_logininfor` VALUES (151, 'admin', '39.144.153.24', '0', '登录成功', '2022-09-27 15:39:17');
INSERT INTO `sys_logininfor` VALUES (152, 'admin', '119.40.64.9', '0', '登录成功', '2022-09-27 15:41:40');
INSERT INTO `sys_logininfor` VALUES (153, 'admin', '103.4.125.25', '0', '登录成功', '2022-09-27 15:46:37');
INSERT INTO `sys_logininfor` VALUES (154, 'admin', '119.40.64.9', '0', '登录成功', '2022-09-28 08:32:33');
INSERT INTO `sys_logininfor` VALUES (155, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-28 09:16:42');
INSERT INTO `sys_logininfor` VALUES (156, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-28 09:39:52');
INSERT INTO `sys_logininfor` VALUES (157, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-28 09:41:20');
INSERT INTO `sys_logininfor` VALUES (158, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-28 09:54:04');
INSERT INTO `sys_logininfor` VALUES (159, 'admin', '103.4.125.25', '0', '登录成功', '2022-09-28 15:01:51');
INSERT INTO `sys_logininfor` VALUES (160, 'admin', '119.40.64.9', '0', '登录成功', '2022-09-28 15:02:44');
INSERT INTO `sys_logininfor` VALUES (161, 'admin', '119.40.64.9', '0', '登录成功', '2022-09-28 17:21:47');
INSERT INTO `sys_logininfor` VALUES (162, 'admin', '119.40.64.9', '0', '登录成功', '2022-09-28 17:23:45');
INSERT INTO `sys_logininfor` VALUES (163, 'admin', '117.136.68.181', '0', '登录成功', '2022-09-29 08:58:11');
INSERT INTO `sys_logininfor` VALUES (164, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-29 10:52:40');
INSERT INTO `sys_logininfor` VALUES (165, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-29 10:52:41');
INSERT INTO `sys_logininfor` VALUES (166, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-29 10:52:46');
INSERT INTO `sys_logininfor` VALUES (167, 'admin', '127.0.0.1', '0', '退出成功', '2022-09-29 13:12:13');
INSERT INTO `sys_logininfor` VALUES (168, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-29 13:12:19');
INSERT INTO `sys_logininfor` VALUES (169, 'admin', '39.144.153.132', '0', '登录成功', '2022-09-29 16:38:49');
INSERT INTO `sys_logininfor` VALUES (170, 'admin', '39.144.153.32', '0', '登录成功', '2022-09-30 09:08:26');
INSERT INTO `sys_logininfor` VALUES (171, 'admin', '127.0.0.1', '0', '登录成功', '2022-09-30 10:09:29');
INSERT INTO `sys_logininfor` VALUES (172, 'admin', '223.104.211.218', '0', '登录成功', '2022-09-30 10:11:33');
INSERT INTO `sys_logininfor` VALUES (173, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-07 21:05:12');
INSERT INTO `sys_logininfor` VALUES (174, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-07 21:49:21');
INSERT INTO `sys_logininfor` VALUES (175, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-08 08:50:00');
INSERT INTO `sys_logininfor` VALUES (176, 'admin', '39.144.153.38', '0', '登录成功', '2022-10-08 09:16:22');
INSERT INTO `sys_logininfor` VALUES (177, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-09 08:42:05');
INSERT INTO `sys_logininfor` VALUES (178, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-09 10:50:57');
INSERT INTO `sys_logininfor` VALUES (179, 'admin', '49.93.165.1', '0', '登录成功', '2022-10-09 13:33:32');
INSERT INTO `sys_logininfor` VALUES (180, 'admin', '39.144.153.179', '0', '登录成功', '2022-10-09 15:51:57');
INSERT INTO `sys_logininfor` VALUES (181, 'admin', '39.144.153.22', '0', '登录成功', '2022-10-10 08:36:44');
INSERT INTO `sys_logininfor` VALUES (182, 'admin', '39.144.153.22', '0', '登录成功', '2022-10-10 08:52:09');
INSERT INTO `sys_logininfor` VALUES (183, 'admin', '39.144.153.22', '0', '登录成功', '2022-10-10 08:57:32');
INSERT INTO `sys_logininfor` VALUES (184, 'admin', '39.144.153.22', '0', '登录成功', '2022-10-10 10:19:25');
INSERT INTO `sys_logininfor` VALUES (185, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-10 12:53:34');
INSERT INTO `sys_logininfor` VALUES (186, 'admin', '39.144.153.22', '0', '登录成功', '2022-10-10 13:23:41');
INSERT INTO `sys_logininfor` VALUES (187, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-10 13:27:48');
INSERT INTO `sys_logininfor` VALUES (188, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:11:19');
INSERT INTO `sys_logininfor` VALUES (189, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:12:46');
INSERT INTO `sys_logininfor` VALUES (190, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:13:45');
INSERT INTO `sys_logininfor` VALUES (191, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:15:22');
INSERT INTO `sys_logininfor` VALUES (192, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:17:22');
INSERT INTO `sys_logininfor` VALUES (193, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:17:37');
INSERT INTO `sys_logininfor` VALUES (194, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:18:10');
INSERT INTO `sys_logininfor` VALUES (195, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:19:53');
INSERT INTO `sys_logininfor` VALUES (196, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 14:28:31');
INSERT INTO `sys_logininfor` VALUES (197, '123', '192.168.196.112', '1', '用户密码不在指定范围', '2022-10-10 15:55:53');
INSERT INTO `sys_logininfor` VALUES (198, 'admin', '192.168.196.112', '0', '登录成功', '2022-10-10 15:56:06');
INSERT INTO `sys_logininfor` VALUES (199, 'admin', '39.144.153.118', '0', '登录成功', '2022-10-11 09:35:44');
INSERT INTO `sys_logininfor` VALUES (200, 'admin', '192.168.40.112', '0', '登录成功', '2022-10-11 09:44:58');
INSERT INTO `sys_logininfor` VALUES (201, 'admin', '192.168.40.112', '0', '登录成功', '2022-10-11 09:44:58');
INSERT INTO `sys_logininfor` VALUES (202, 'admin', '192.168.40.112', '0', '登录成功', '2022-10-11 15:15:39');
INSERT INTO `sys_logininfor` VALUES (203, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-11 16:04:11');
INSERT INTO `sys_logininfor` VALUES (204, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-11 16:04:17');
INSERT INTO `sys_logininfor` VALUES (205, 'admin', '192.168.40.112', '0', '登录成功', '2022-10-11 16:07:14');
INSERT INTO `sys_logininfor` VALUES (206, 'admin', '192.168.40.112', '0', '登录成功', '2022-10-11 16:16:48');
INSERT INTO `sys_logininfor` VALUES (207, 'admin', '192.168.40.173', '0', '登录成功', '2022-10-11 17:03:38');
INSERT INTO `sys_logininfor` VALUES (208, 'admin', '192.168.40.173', '0', '登录成功', '2022-10-11 17:03:38');
INSERT INTO `sys_logininfor` VALUES (209, 'admin', '192.168.40.173', '0', '登录成功', '2022-10-11 17:03:38');
INSERT INTO `sys_logininfor` VALUES (210, 'admin', '192.168.40.173', '0', '登录成功', '2022-10-11 17:06:45');
INSERT INTO `sys_logininfor` VALUES (211, 'admin', '192.168.40.173', '0', '登录成功', '2022-10-11 17:09:22');
INSERT INTO `sys_logininfor` VALUES (212, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-12 09:17:10');
INSERT INTO `sys_logininfor` VALUES (213, 'admin', '39.144.153.153', '1', '登录用户不存在', '2022-10-12 09:34:59');
INSERT INTO `sys_logininfor` VALUES (214, 'admin', '39.144.153.153', '0', '登录成功', '2022-10-12 09:35:05');
INSERT INTO `sys_logininfor` VALUES (215, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-12 09:35:35');
INSERT INTO `sys_logininfor` VALUES (216, 'admin', '192.168.96.112', '0', '登录成功', '2022-10-12 10:02:58');
INSERT INTO `sys_logininfor` VALUES (217, 'admin', '39.144.153.153', '0', '登录成功', '2022-10-12 11:10:40');
INSERT INTO `sys_logininfor` VALUES (218, 'admin', '192.168.96.112', '0', '登录成功', '2022-10-12 13:06:27');
INSERT INTO `sys_logininfor` VALUES (219, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-12 13:20:28');
INSERT INTO `sys_logininfor` VALUES (220, 'admin', '192.168.96.112', '0', '登录成功', '2022-10-12 14:20:28');
INSERT INTO `sys_logininfor` VALUES (221, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-12 16:01:51');
INSERT INTO `sys_logininfor` VALUES (222, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-13 08:30:55');
INSERT INTO `sys_logininfor` VALUES (223, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-14 10:38:42');
INSERT INTO `sys_logininfor` VALUES (224, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-14 13:29:50');
INSERT INTO `sys_logininfor` VALUES (225, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-14 13:30:12');
INSERT INTO `sys_logininfor` VALUES (226, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-16 15:20:46');
INSERT INTO `sys_logininfor` VALUES (227, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-16 15:20:52');
INSERT INTO `sys_logininfor` VALUES (228, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-17 09:03:40');
INSERT INTO `sys_logininfor` VALUES (229, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-17 09:03:44');
INSERT INTO `sys_logininfor` VALUES (230, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-17 10:42:07');
INSERT INTO `sys_logininfor` VALUES (231, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-18 09:02:02');
INSERT INTO `sys_logininfor` VALUES (232, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-18 09:02:08');
INSERT INTO `sys_logininfor` VALUES (233, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-18 10:34:37');
INSERT INTO `sys_logininfor` VALUES (234, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 16:38:21');
INSERT INTO `sys_logininfor` VALUES (235, 'admin', '192.168.51.173', '1', '密码输入错误1次', '2022-10-18 16:42:25');
INSERT INTO `sys_logininfor` VALUES (236, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 16:42:34');
INSERT INTO `sys_logininfor` VALUES (237, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 16:56:28');
INSERT INTO `sys_logininfor` VALUES (238, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:02:40');
INSERT INTO `sys_logininfor` VALUES (239, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:04:39');
INSERT INTO `sys_logininfor` VALUES (240, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:11:30');
INSERT INTO `sys_logininfor` VALUES (241, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:28:14');
INSERT INTO `sys_logininfor` VALUES (242, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:29:32');
INSERT INTO `sys_logininfor` VALUES (243, 'admin', '192.168.51.173', '0', '登录成功', '2022-10-18 17:31:23');
INSERT INTO `sys_logininfor` VALUES (244, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-19 10:38:42');
INSERT INTO `sys_logininfor` VALUES (245, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 09:27:09');
INSERT INTO `sys_logininfor` VALUES (246, 'admin', '192.168.51.112', '1', '登录用户不存在', '2022-10-21 10:41:57');
INSERT INTO `sys_logininfor` VALUES (247, 'admin', '192.168.51.112', '0', '登录成功', '2022-10-21 10:49:17');
INSERT INTO `sys_logininfor` VALUES (248, 'admin', '192.168.51.112', '0', '登录成功', '2022-10-21 10:49:18');
INSERT INTO `sys_logininfor` VALUES (249, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-21 14:05:57');
INSERT INTO `sys_logininfor` VALUES (250, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 14:09:09');
INSERT INTO `sys_logininfor` VALUES (251, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-21 14:59:49');
INSERT INTO `sys_logininfor` VALUES (252, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 15:01:57');
INSERT INTO `sys_logininfor` VALUES (253, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 15:03:16');
INSERT INTO `sys_logininfor` VALUES (254, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 15:03:22');
INSERT INTO `sys_logininfor` VALUES (255, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 15:03:30');
INSERT INTO `sys_logininfor` VALUES (256, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-21 15:04:12');
INSERT INTO `sys_logininfor` VALUES (257, 'admin', '192.168.51.112', '0', '登录成功', '2022-10-24 09:26:23');
INSERT INTO `sys_logininfor` VALUES (258, 'admin', '117.136.68.21', '0', '登录成功', '2022-10-24 14:16:03');
INSERT INTO `sys_logininfor` VALUES (259, 'admin', '117.136.68.21', '0', '登录成功', '2022-10-24 14:20:43');
INSERT INTO `sys_logininfor` VALUES (260, 'admin', '117.136.68.21', '0', '登录成功', '2022-10-24 14:22:17');
INSERT INTO `sys_logininfor` VALUES (261, 'admin', '117.136.68.21', '0', '登录成功', '2022-10-24 14:25:45');
INSERT INTO `sys_logininfor` VALUES (262, 'admin', '117.136.68.21', '0', '登录成功', '2022-10-24 14:26:45');
INSERT INTO `sys_logininfor` VALUES (263, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 14:32:53');
INSERT INTO `sys_logininfor` VALUES (264, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 14:50:47');
INSERT INTO `sys_logininfor` VALUES (265, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 14:57:46');
INSERT INTO `sys_logininfor` VALUES (266, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-24 15:28:28');
INSERT INTO `sys_logininfor` VALUES (267, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-24 15:28:28');
INSERT INTO `sys_logininfor` VALUES (268, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-24 15:28:41');
INSERT INTO `sys_logininfor` VALUES (269, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-24 15:28:59');
INSERT INTO `sys_logininfor` VALUES (270, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 15:29:57');
INSERT INTO `sys_logininfor` VALUES (271, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 15:30:25');
INSERT INTO `sys_logininfor` VALUES (272, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 15:30:53');
INSERT INTO `sys_logininfor` VALUES (273, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-24 16:25:46');
INSERT INTO `sys_logininfor` VALUES (274, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-24 16:50:11');
INSERT INTO `sys_logininfor` VALUES (275, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-24 17:11:51');
INSERT INTO `sys_logininfor` VALUES (276, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-24 17:12:08');
INSERT INTO `sys_logininfor` VALUES (277, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-24 17:13:48');
INSERT INTO `sys_logininfor` VALUES (278, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 10:29:09');
INSERT INTO `sys_logininfor` VALUES (279, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-25 10:44:13');
INSERT INTO `sys_logininfor` VALUES (280, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 10:44:18');
INSERT INTO `sys_logininfor` VALUES (281, 'admin', '39.144.153.160', '0', '登录成功', '2022-10-25 10:59:10');
INSERT INTO `sys_logininfor` VALUES (282, 'admin', '39.144.153.160', '0', '登录成功', '2022-10-25 11:00:17');
INSERT INTO `sys_logininfor` VALUES (283, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 13:21:04');
INSERT INTO `sys_logininfor` VALUES (284, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-25 13:46:38');
INSERT INTO `sys_logininfor` VALUES (285, 'admin', '127.0.0.1', '1', '登录用户不存在', '2022-10-25 13:46:41');
INSERT INTO `sys_logininfor` VALUES (286, 'admin', '127.0.0.1', '1', '密码输入错误1次', '2022-10-25 13:55:56');
INSERT INTO `sys_logininfor` VALUES (287, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 13:56:23');
INSERT INTO `sys_logininfor` VALUES (288, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 15:06:47');
INSERT INTO `sys_logininfor` VALUES (289, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 15:50:29');
INSERT INTO `sys_logininfor` VALUES (290, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-25 15:56:27');
INSERT INTO `sys_logininfor` VALUES (291, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 09:35:36');
INSERT INTO `sys_logininfor` VALUES (292, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 09:51:02');
INSERT INTO `sys_logininfor` VALUES (293, 'admin', '127.0.0.1', '0', '退出成功', '2022-10-26 10:23:50');
INSERT INTO `sys_logininfor` VALUES (294, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 10:23:54');
INSERT INTO `sys_logininfor` VALUES (295, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 13:29:57');
INSERT INTO `sys_logininfor` VALUES (296, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 13:36:49');
INSERT INTO `sys_logininfor` VALUES (297, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-26 13:55:14');
INSERT INTO `sys_logininfor` VALUES (298, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 14:23:50');
INSERT INTO `sys_logininfor` VALUES (299, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 14:37:14');
INSERT INTO `sys_logininfor` VALUES (300, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 14:55:58');
INSERT INTO `sys_logininfor` VALUES (301, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 15:01:28');
INSERT INTO `sys_logininfor` VALUES (302, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 15:02:16');
INSERT INTO `sys_logininfor` VALUES (303, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-26 15:08:26');
INSERT INTO `sys_logininfor` VALUES (304, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-26 15:11:21');
INSERT INTO `sys_logininfor` VALUES (305, 'admin', '117.136.67.208', '0', '登录成功', '2022-10-26 15:12:22');
INSERT INTO `sys_logininfor` VALUES (306, 'admin', '39.144.153.138', '0', '登录成功', '2022-10-26 16:48:05');
INSERT INTO `sys_logininfor` VALUES (307, 'admin', '39.144.153.138', '0', '登录成功', '2022-10-26 16:48:46');
INSERT INTO `sys_logininfor` VALUES (308, 'admin', '39.144.153.138', '0', '登录成功', '2022-10-26 16:50:28');
INSERT INTO `sys_logininfor` VALUES (309, 'admin', '39.144.153.84', '0', '登录成功', '2022-10-27 09:12:11');
INSERT INTO `sys_logininfor` VALUES (310, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 09:15:32');
INSERT INTO `sys_logininfor` VALUES (311, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 10:14:21');
INSERT INTO `sys_logininfor` VALUES (312, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 10:15:29');
INSERT INTO `sys_logininfor` VALUES (313, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 10:16:33');
INSERT INTO `sys_logininfor` VALUES (314, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 10:47:13');
INSERT INTO `sys_logininfor` VALUES (315, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 10:51:15');
INSERT INTO `sys_logininfor` VALUES (316, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-27 10:51:33');
INSERT INTO `sys_logininfor` VALUES (317, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 11:01:05');
INSERT INTO `sys_logininfor` VALUES (318, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 11:02:16');
INSERT INTO `sys_logininfor` VALUES (319, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 11:04:42');
INSERT INTO `sys_logininfor` VALUES (320, 'admin', '117.136.67.202', '1', '密码输入错误1次', '2022-10-27 11:27:40');
INSERT INTO `sys_logininfor` VALUES (321, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 11:27:48');
INSERT INTO `sys_logininfor` VALUES (322, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 11:29:15');
INSERT INTO `sys_logininfor` VALUES (323, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 12:59:34');
INSERT INTO `sys_logininfor` VALUES (324, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 13:27:34');
INSERT INTO `sys_logininfor` VALUES (325, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 13:38:49');
INSERT INTO `sys_logininfor` VALUES (326, 'admin', '119.40.69.128', '1', '密码输入错误1次', '2022-10-27 13:39:55');
INSERT INTO `sys_logininfor` VALUES (327, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 13:39:58');
INSERT INTO `sys_logininfor` VALUES (328, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 13:46:59');
INSERT INTO `sys_logininfor` VALUES (329, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 13:49:12');
INSERT INTO `sys_logininfor` VALUES (330, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 14:14:16');
INSERT INTO `sys_logininfor` VALUES (331, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 14:26:22');
INSERT INTO `sys_logininfor` VALUES (332, 'admin', '117.136.67.202', '0', '登录成功', '2022-10-27 14:55:56');
INSERT INTO `sys_logininfor` VALUES (333, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 15:33:21');
INSERT INTO `sys_logininfor` VALUES (334, 'admin', '119.40.69.128', '0', '登录成功', '2022-10-27 15:41:22');
INSERT INTO `sys_logininfor` VALUES (335, 'admin', '119.40.69.129', '0', '登录成功', '2022-10-27 17:29:58');
INSERT INTO `sys_logininfor` VALUES (336, 'admin', '127.0.0.1', '0', '登录成功', '2022-10-31 20:08:28');
INSERT INTO `sys_logininfor` VALUES (337, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-01 09:46:04');
INSERT INTO `sys_logininfor` VALUES (338, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-01 09:57:35');
INSERT INTO `sys_logininfor` VALUES (339, 'admin', '192.168.106.112', '0', '登录成功', '2022-11-01 10:44:54');
INSERT INTO `sys_logininfor` VALUES (340, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-02 08:52:41');
INSERT INTO `sys_logininfor` VALUES (341, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-02 10:08:58');
INSERT INTO `sys_logininfor` VALUES (342, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-02 16:13:26');
INSERT INTO `sys_logininfor` VALUES (343, 'admin', '127.0.0.1', '0', '登录成功', '2022-11-03 13:02:46');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(11) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(11) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1061 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2022-09-19 10:14:33', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2022-09-19 10:14:33', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2022-09-19 10:14:33', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, '若依官网', 0, 4, 'http://ruoyi.vip', NULL, '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', '2022-09-19 10:14:33', '', NULL, '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2022-09-19 10:14:33', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2022-09-19 10:14:33', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2022-09-19 10:14:33', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2022-09-19 10:14:33', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2022-09-19 10:14:33', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2022-09-19 10:14:33', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2022-09-19 10:14:33', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2022-09-19 10:14:33', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2022-09-19 10:14:33', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2022-09-19 10:14:33', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2022-09-19 10:14:33', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8718', '', '', 0, 0, 'C', '0', '0', 'monitor:sentinel:list', 'sentinel', 'admin', '2022-09-19 10:14:33', '', NULL, '流量控制菜单');
INSERT INTO `sys_menu` VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', 0, 0, 'C', '0', '0', 'monitor:nacos:list', 'nacos', 'admin', '2022-09-19 10:14:33', '', NULL, '服务治理菜单');
INSERT INTO `sys_menu` VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9100/login', '', '', 0, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2022-09-19 10:14:33', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2022-09-19 10:14:33', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2022-09-19 10:14:33', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'http://localhost:8080/swagger-ui/index.html', '', '', 0, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2022-09-19 10:14:33', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', 1, 0, 'C', '0', '0', 'system:operlog:list', 'form', 'admin', '2022-09-19 10:14:33', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', 1, 0, 'C', '0', '0', 'system:logininfor:list', 'logininfor', 'admin', '2022-09-19 10:14:33', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2022-09-19 10:14:33', '', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='通知公告表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
BEGIN;
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2022-09-19 10:14:34', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2022-09-19 10:14:34', '', NULL, '管理员');
COMMIT;

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(11) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `method_return_code` int(11) DEFAULT NULL COMMENT '方法返回状态码',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=590 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='操作日志记录';

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
BEGIN;
INSERT INTO `sys_oper_log` VALUES (328, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"average\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-09 15:34:14');
INSERT INTO `sys_oper_log` VALUES (329, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"average\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-09 15:34:17');
INSERT INTO `sys_oper_log` VALUES (330, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '条形码有误 20170826669006391110000015100961661611251128000060', '2022-10-10 14:02:29');
INSERT INTO `sys_oper_log` VALUES (331, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '条形码有误 20170826669006391110000015100961661611251128000060', '2022-10-10 14:02:34');
INSERT INTO `sys_oper_log` VALUES (332, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 14:03:50');
INSERT INTO `sys_oper_log` VALUES (333, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T14:29:29.334+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 14:12:46');
INSERT INTO `sys_oper_log` VALUES (334, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '条形码有误 20170826669006391110000015100961661611251128000060', '2022-10-10 14:34:03');
INSERT INTO `sys_oper_log` VALUES (335, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'opearte_time\' in \'field list\'\r\n### The error may exist in file [C:\\Users\\UWH4SZH\\Documents\\projects\\wms-cloud\\wms-modules\\storage-in\\target\\classes\\mapper\\storagein\\MaterialInMapper.xml]\r\n### The error may involve com.bosch.storagein.mapper.MaterialInMapper.insertMaterialIn-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into si_material_in          ( sscc_number,             batch_nb,             material_nb,             check_type,              check_quantity,             min_standard,             max_standard,             actual_quantity,             actual_result,             operate_user,             opearte_time,             average_result )           values ( ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ?,             ? )\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'opearte_time\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'opearte_time\' in \'field list\'', '2022-10-10 14:40:41');
INSERT INTO `sys_oper_log` VALUES (336, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T14:59:48.55+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 14:43:06');
INSERT INTO `sys_oper_log` VALUES (337, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'virtualBinCode\' not found. Available parameters are [arg1, arg0, param1, param2]', '2022-10-10 14:57:19');
INSERT INTO `sys_oper_log` VALUES (338, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '\r\n### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'virtual_bin_code\' in \'field list\'\r\n### The error may exist in file [C:\\Users\\UWH4SZH\\Documents\\projects\\wms-cloud\\wms-modules\\storage-in\\target\\classes\\mapper\\storagein\\MaterialInMapper.xml]\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: update si_material_in         set virtual_bin_code = ?         where sscc_number = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'virtual_bin_code\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'virtual_bin_code\' in \'field list\'', '2022-10-10 15:48:23');
INSERT INTO `sys_oper_log` VALUES (339, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T16:05:36.611+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 15:48:53');
INSERT INTO `sys_oper_log` VALUES (340, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T16:20:07.117+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 16:03:24');
INSERT INTO `sys_oper_log` VALUES (341, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T16:21:00.193+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 16:04:17');
INSERT INTO `sys_oper_log` VALUES (342, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'sncc_number\' in \'where clause\'\r\n### The error may exist in file [C:\\Users\\UWH4SZH\\Documents\\projects\\wms-cloud\\wms-modules\\storage-in\\target\\classes\\mapper\\storagein\\MaterialInMapper.xml]\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: select id,                sscc_number,                batch_nb,                material_nb,                check_type,                check_quantity,                min_standard,                max_standard,                actual_quantity,                actual_result,                average_result,                operate_user,                operate_time         from si_material_in               where sncc_number = ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'sncc_number\' in \'where clause\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'sncc_number\' in \'where clause\'', '2022-10-10 16:06:10');
INSERT INTO `sys_oper_log` VALUES (343, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '请勿重复入库', '2022-10-10 16:07:16');
INSERT INTO `sys_oper_log` VALUES (344, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":601,\"msg\":\"重复入库\"}', 0, 601, NULL, '2022-10-10 16:14:05');
INSERT INTO `sys_oper_log` VALUES (345, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":293.1666666666667,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":6,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-10T16:37:29.326+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-10 16:20:46');
INSERT INTO `sys_oper_log` VALUES (346, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":601,\"msg\":\"重复入库\"}', 0, 601, NULL, '2022-10-10 16:20:49');
INSERT INTO `sys_oper_log` VALUES (347, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":601,\"msg\":\"重复入库\"}', 0, 601, NULL, '2022-10-10 17:06:06');
INSERT INTO `sys_oper_log` VALUES (348, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 13:36:03');
INSERT INTO `sys_oper_log` VALUES (349, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 14:04:27');
INSERT INTO `sys_oper_log` VALUES (350, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0}', NULL, 1, NULL, '', '2022-10-11 15:02:04');
INSERT INTO `sys_oper_log` VALUES (351, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0}', NULL, 1, NULL, '', '2022-10-11 15:02:05');
INSERT INTO `sys_oper_log` VALUES (352, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:38');
INSERT INTO `sys_oper_log` VALUES (353, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:40');
INSERT INTO `sys_oper_log` VALUES (354, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:42');
INSERT INTO `sys_oper_log` VALUES (355, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:43');
INSERT INTO `sys_oper_log` VALUES (356, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:43');
INSERT INTO `sys_oper_log` VALUES (357, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:43');
INSERT INTO `sys_oper_log` VALUES (358, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:43');
INSERT INTO `sys_oper_log` VALUES (359, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:43');
INSERT INTO `sys_oper_log` VALUES (360, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:44');
INSERT INTO `sys_oper_log` VALUES (361, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 15:02:46');
INSERT INTO `sys_oper_log` VALUES (362, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-11T16:32:34.808+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-11 16:15:50');
INSERT INTO `sys_oper_log` VALUES (363, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":123,\"actualResult\":123.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-39.40650406504065,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-11T16:33:53.286+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-11 16:17:08');
INSERT INTO `sys_oper_log` VALUES (364, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.40.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-11T16:36:58.635+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-11 16:20:14');
INSERT INTO `sys_oper_log` VALUES (365, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":300,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":300,\"actualResult\":100000.0,\"averageResult\":243.33333333333331,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-11T16:40:37.304+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-11 16:23:53');
INSERT INTO `sys_oper_log` VALUES (366, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkType\":3,\"checkTypeDesc\":\"该批次已检\",\"materialNb\":\"10096166\",\"operateTime\":\"2022-10-11T16:41:54.629+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110000015\"}}', 0, 200, NULL, '2022-10-11 16:25:10');
INSERT INTO `sys_oper_log` VALUES (367, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-11 17:02:08');
INSERT INTO `sys_oper_log` VALUES (368, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-11 17:02:25');
INSERT INTO `sys_oper_log` VALUES (369, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-11 17:05:17');
INSERT INTO `sys_oper_log` VALUES (370, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-11 17:07:26');
INSERT INTO `sys_oper_log` VALUES (371, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"抽样件数不符合\"}', 0, 602, NULL, '2022-10-11 17:08:18');
INSERT INTO `sys_oper_log` VALUES (372, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 09:35:57');
INSERT INTO `sys_oper_log` VALUES (373, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":12,\"actualResult\":100000.0,\"averageResult\":8243.333333333334,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-12T09:57:03.082+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 09:40:28');
INSERT INTO `sys_oper_log` VALUES (374, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":12,\"actualResult\":100000.0,\"averageResult\":8243.333333333334,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-12T10:00:30.652+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 10:02:57');
INSERT INTO `sys_oper_log` VALUES (375, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":12,\"actualResult\":12.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.0,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"minStandard\":10.0,\"operateTime\":\"2022-10-12T10:27:11.309+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 10:10:26');
INSERT INTO `sys_oper_log` VALUES (376, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 10:16:41');
INSERT INTO `sys_oper_log` VALUES (377, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 10:16:49');
INSERT INTO `sys_oper_log` VALUES (378, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 10:27:44');
INSERT INTO `sys_oper_log` VALUES (379, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'ssccNumber\' not found. Available parameters are [arg0, collection, list]', '2022-10-12 10:42:40');
INSERT INTO `sys_oper_log` VALUES (380, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'ssccNumber\' not found. Available parameters are [list, param1]', '2022-10-12 10:43:52');
INSERT INTO `sys_oper_log` VALUES (381, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":12,\"actualResult\":100000.0,\"averageResult\":8243.333333333334,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T11:06:55.891+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 10:50:12');
INSERT INTO `sys_oper_log` VALUES (382, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 10:55:25');
INSERT INTO `sys_oper_log` VALUES (383, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'status\' not found. Available parameters are [code, materialNb, batchNb, param3, param1, param2]', '2022-10-12 10:55:53');
INSERT INTO `sys_oper_log` VALUES (384, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":12,\"actualResult\":100000.0,\"averageResult\":8243.333333333334,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T11:14:33.963+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 10:57:48');
INSERT INTO `sys_oper_log` VALUES (385, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":123,\"actualResult\":123.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.0,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T11:28:21.206+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 11:11:36');
INSERT INTO `sys_oper_log` VALUES (386, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":12,\"actualResult\":100000.0,\"averageResult\":8243.333333333334,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T11:33:59.288+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 11:17:14');
INSERT INTO `sys_oper_log` VALUES (387, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":12,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 11:17:47');
INSERT INTO `sys_oper_log` VALUES (388, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 14:38:12');
INSERT INTO `sys_oper_log` VALUES (389, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 14:38:19');
INSERT INTO `sys_oper_log` VALUES (390, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 14:38:25');
INSERT INTO `sys_oper_log` VALUES (391, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 14:43:20');
INSERT INTO `sys_oper_log` VALUES (392, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 14:45:51');
INSERT INTO `sys_oper_log` VALUES (393, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 14:47:06');
INSERT INTO `sys_oper_log` VALUES (394, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 14:47:13');
INSERT INTO `sys_oper_log` VALUES (395, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:17:02');
INSERT INTO `sys_oper_log` VALUES (396, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:17:17');
INSERT INTO `sys_oper_log` VALUES (397, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:17:35');
INSERT INTO `sys_oper_log` VALUES (398, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:17:47');
INSERT INTO `sys_oper_log` VALUES (399, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:22:43');
INSERT INTO `sys_oper_log` VALUES (400, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:40:26.174+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:23:40');
INSERT INTO `sys_oper_log` VALUES (401, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:42:29.015+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:25:43');
INSERT INTO `sys_oper_log` VALUES (402, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:44:22.552+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:27:37');
INSERT INTO `sys_oper_log` VALUES (403, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 15:29:51');
INSERT INTO `sys_oper_log` VALUES (404, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:46:42.946+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:29:57');
INSERT INTO `sys_oper_log` VALUES (405, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1.0,\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:47:58.306+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:31:12');
INSERT INTO `sys_oper_log` VALUES (406, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1.0,\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:50:10.481+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:33:24');
INSERT INTO `sys_oper_log` VALUES (407, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1.0,\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T15:51:24.133+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:34:38');
INSERT INTO `sys_oper_log` VALUES (408, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'originPalletQuantity\' not found. Available parameters are [list, param1]', '2022-10-12 15:36:47');
INSERT INTO `sys_oper_log` VALUES (409, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'originPalletQuantity\' not found. Available parameters are [list, param1]', '2022-10-12 15:36:54');
INSERT INTO `sys_oper_log` VALUES (410, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'originPalletQuantity\' not found. Available parameters are [list, param1]', '2022-10-12 15:37:29');
INSERT INTO `sys_oper_log` VALUES (411, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 15:40:09');
INSERT INTO `sys_oper_log` VALUES (412, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.reflection.ReflectionException: There is no setter for property named \'originPalletQuantity\' in \'class com.bosch.storagein.api.domain.vo.MaterialInVO\'', '2022-10-12 15:40:11');
INSERT INTO `sys_oper_log` VALUES (413, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:41:29');
INSERT INTO `sys_oper_log` VALUES (414, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:41:37');
INSERT INTO `sys_oper_log` VALUES (415, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 15:41:58');
INSERT INTO `sys_oper_log` VALUES (416, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:42:00');
INSERT INTO `sys_oper_log` VALUES (417, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:42:02');
INSERT INTO `sys_oper_log` VALUES (418, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:42:15');
INSERT INTO `sys_oper_log` VALUES (419, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:43:10');
INSERT INTO `sys_oper_log` VALUES (420, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:43:13');
INSERT INTO `sys_oper_log` VALUES (421, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 15:44:22');
INSERT INTO `sys_oper_log` VALUES (422, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:44:25');
INSERT INTO `sys_oper_log` VALUES (423, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:44:27');
INSERT INTO `sys_oper_log` VALUES (424, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 15:45:39');
INSERT INTO `sys_oper_log` VALUES (425, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:46:31');
INSERT INTO `sys_oper_log` VALUES (426, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 15:46:33');
INSERT INTO `sys_oper_log` VALUES (427, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '', '2022-10-12 15:50:16');
INSERT INTO `sys_oper_log` VALUES (428, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":10000.0,\"averageResult\":910.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T16:08:43.983+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 15:51:58');
INSERT INTO `sys_oper_log` VALUES (429, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', NULL, 1, NULL, '/ by zero', '2022-10-12 16:27:38');
INSERT INTO `sys_oper_log` VALUES (430, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":100000.0,\"averageResult\":9910.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T16:45:55.963+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 16:29:10');
INSERT INTO `sys_oper_log` VALUES (431, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":100000.0,\"averageResult\":9910.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T16:54:39.742+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 16:37:53');
INSERT INTO `sys_oper_log` VALUES (432, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":602,\"msg\":\"重复入库\"}', 0, 602, NULL, '2022-10-12 16:44:39');
INSERT INTO `sys_oper_log` VALUES (433, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":100000.0,\"averageResult\":9910.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:01:37.648+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 16:44:51');
INSERT INTO `sys_oper_log` VALUES (434, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":22,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":22,\"actualResult\":1.0,\"averageResult\":-89.95454545454545,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:28:30.229+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 17:11:45');
INSERT INTO `sys_oper_log` VALUES (435, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 17:14:49');
INSERT INTO `sys_oper_log` VALUES (436, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1.0,\"averageResult\":-89.9,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:31:47.443+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 17:15:01');
INSERT INTO `sys_oper_log` VALUES (437, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":100000.0,\"averageResult\":9910.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:32:32.7+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-12 17:15:47');
INSERT INTO `sys_oper_log` VALUES (438, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":100000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 17:16:40');
INSERT INTO `sys_oper_log` VALUES (439, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"averageResult\":1000.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:36:36.842+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-12 17:19:51');
INSERT INTO `sys_oper_log` VALUES (440, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":1,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-12 17:23:20');
INSERT INTO `sys_oper_log` VALUES (441, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":1.0,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:40:27.181+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-12 17:23:41');
INSERT INTO `sys_oper_log` VALUES (442, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.96.112', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\"}', '{\"code\":200,\"data\":{\"averageResult\":1000.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-12T17:41:46.407+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-12 17:25:00');
INSERT INTO `sys_oper_log` VALUES (443, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":1.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1.0,\"averageResult\":1.0,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-13T08:48:13.759+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-13 08:31:26');
INSERT INTO `sys_oper_log` VALUES (444, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10000,\"actualResult\":10.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":10000,\"actualResult\":10.0,\"averageResult\":1.0,\"batchNb\":\"1611251128\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-13T08:49:03.1+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":3,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-13 08:32:15');
INSERT INTO `sys_oper_log` VALUES (445, '供应商', 3, 'com.bosch.masterdata.controller.SupplierInfoController.remove()', 'DELETE', 1, 'admin', NULL, '/supplierInfo/10', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:35:00');
INSERT INTO `sys_oper_log` VALUES (446, '供应商', 3, 'com.bosch.masterdata.controller.SupplierInfoController.remove()', 'DELETE', 1, 'admin', NULL, '/supplierInfo/9', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:35:03');
INSERT INTO `sys_oper_log` VALUES (447, '供应商', 3, 'com.bosch.masterdata.controller.SupplierInfoController.remove()', 'DELETE', 1, 'admin', NULL, '/supplierInfo/6', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:35:07');
INSERT INTO `sys_oper_log` VALUES (448, '供应商', 3, 'com.bosch.masterdata.controller.SupplierInfoController.remove()', 'DELETE', 1, 'admin', NULL, '/supplierInfo/7', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:35:11');
INSERT INTO `sys_oper_log` VALUES (449, '供应商', 3, 'com.bosch.masterdata.controller.SupplierInfoController.remove()', 'DELETE', 1, 'admin', NULL, '/supplierInfo/8', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:35:15');
INSERT INTO `sys_oper_log` VALUES (450, '库位', 3, 'com.bosch.masterdata.controller.BinController.remove()', 'DELETE', 1, 'admin', NULL, '/bin/4', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:42:54');
INSERT INTO `sys_oper_log` VALUES (451, '库位', 3, 'com.bosch.masterdata.controller.BinController.remove()', 'DELETE', 1, 'admin', NULL, '/bin/6', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:42:57');
INSERT INTO `sys_oper_log` VALUES (452, '库位', 3, 'com.bosch.masterdata.controller.BinController.remove()', 'DELETE', 1, 'admin', NULL, '/bin/7', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:43:02');
INSERT INTO `sys_oper_log` VALUES (453, '库位', 3, 'com.bosch.masterdata.controller.BinController.remove()', 'DELETE', 1, 'admin', NULL, '/bin/5', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-18 15:43:05');
INSERT INTO `sys_oper_log` VALUES (454, '跨', 3, 'com.bosch.masterdata.controller.FrameController.remove()', 'DELETE', 1, 'admin', NULL, '/frame/6', '127.0.0.1', '', NULL, NULL, 1, NULL, '\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/master-data/target/classes/mapper/masterdata/FrameMapper.xml]\n### The error may involve com.bosch.masterdata.mapper.FrameMapper.deleteFrameByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from md_frame where id in           (               ?          )\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))\n; Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`)); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))', '2022-10-18 15:46:10');
INSERT INTO `sys_oper_log` VALUES (455, '跨', 3, 'com.bosch.masterdata.controller.FrameController.remove()', 'DELETE', 1, 'admin', NULL, '/frame/6', '127.0.0.1', '', NULL, NULL, 1, NULL, '\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/master-data/target/classes/mapper/masterdata/FrameMapper.xml]\n### The error may involve com.bosch.masterdata.mapper.FrameMapper.deleteFrameByIds-Inline\n### The error occurred while setting parameters\n### SQL: delete from md_frame where id in           (               ?          )\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))\n; Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`)); nested exception is java.sql.SQLIntegrityConstraintViolationException: Cannot delete or update a parent row: a foreign key constraint fails (`wms-cloud`.`md_bin`, CONSTRAINT `fk_location_info_frame_id` FOREIGN KEY (`frame_id`) REFERENCES `md_frame` (`id`))', '2022-10-18 15:46:13');
INSERT INTO `sys_oper_log` VALUES (456, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', NULL, 1, NULL, '\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/storage-in/target/classes/mapper/storagein/MaterialInMapper.xml]\n### The error may involve defaultParameterMap\n### The error occurred while setting parameters\n### SQL: select id,                sscc_number,                batch_nb,                material_nb,                check_type,                check_quantity,                quantity,                min_standard,                max_standard,                actual_quantity,                actual_result,                average_result,                operate_user,                operate_time,                origin_pallet_quantity         from si_material_in               where sscc_number = ?         limit 1\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'', '2022-10-24 10:14:15');
INSERT INTO `sys_oper_log` VALUES (457, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', NULL, 1, NULL, '\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/storage-in/target/classes/mapper/storagein/MaterialInMapper.xml]\n### The error may involve defaultParameterMap\n### The error occurred while setting parameters\n### SQL: select id,                sscc_number,                batch_nb,                material_nb,                check_type,                check_quantity,                quantity,                min_standard,                max_standard,                actual_quantity,                actual_result,                average_result,                operate_user,                operate_time,                origin_pallet_quantity         from si_material_in               where sscc_number = ?         limit 1\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'quantity\' in \'field list\'', '2022-10-24 10:14:38');
INSERT INTO `sys_oper_log` VALUES (458, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', NULL, 1, NULL, '', '2022-10-24 10:15:44');
INSERT INTO `sys_oper_log` VALUES (459, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"originPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":10000.0,\"averageResult\":91.0,\"batchNb\":\"1611251128\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500029108\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10096166\",\"maxStandard\":9999999.0,\"mesBarCode\":\"20170826669006391110000015100961661611251128000060\",\"minStandard\":10.0,\"operateTime\":\"2022-10-24T10:16:53.945+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110000015\",\"totalPallet\":15,\"unit\":\"PC\"}}', 0, 200, NULL, '2022-10-24 10:16:47');
INSERT INTO `sys_oper_log` VALUES (460, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '192.168.51.112', '', '{\"mesBarCode\":\"20170826669006391110000050100961671611251129000060\"}', '{\"code\":200,\"data\":{\"batchNb\":\"1611251129\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialNb\":\"10096167\",\"mesBarCode\":\"20170826669006391110000050100961671611251129000060\",\"operateTime\":\"2022-10-24T10:52:34.264+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110000050\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-24 10:54:27');
INSERT INTO `sys_oper_log` VALUES (461, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1144', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:17');
INSERT INTO `sys_oper_log` VALUES (462, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/8', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:19');
INSERT INTO `sys_oper_log` VALUES (463, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/9', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:20');
INSERT INTO `sys_oper_log` VALUES (464, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/7', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:22');
INSERT INTO `sys_oper_log` VALUES (465, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/10', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:25');
INSERT INTO `sys_oper_log` VALUES (466, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/11', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:26');
INSERT INTO `sys_oper_log` VALUES (467, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/12', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:28');
INSERT INTO `sys_oper_log` VALUES (468, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/15', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:29');
INSERT INTO `sys_oper_log` VALUES (469, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1142', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:33:31');
INSERT INTO `sys_oper_log` VALUES (470, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"FEL\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:35:11.862+08:00\",\"departmentId\":1,\"description\":\"ECN 国内成品\",\"id\":23,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:35:11');
INSERT INTO `sys_oper_log` VALUES (471, '物料类型', 3, 'com.bosch.masterdata.controller.MaterialTypeController.remove()', 'DELETE', 1, 'admin', NULL, '/materialType/21', '119.40.69.129', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:35:27');
INSERT INTO `sys_oper_log` VALUES (472, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"FNL\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:35:57.793+08:00\",\"departmentId\":3,\"description\":\"NMD国内成品\",\"id\":24,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:35:57');
INSERT INTO `sys_oper_log` VALUES (473, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"FFE\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:36:11.81+08:00\",\"departmentId\":2,\"description\":\"FSMP出口成品\",\"id\":25,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:36:11');
INSERT INTO `sys_oper_log` VALUES (474, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"FFL\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:36:22.74+08:00\",\"departmentId\":2,\"description\":\"FSMP国内成品\",\"id\":26,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:36:22');
INSERT INTO `sys_oper_log` VALUES (475, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"REBA\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:36:41.925+08:00\",\"departmentId\":1,\"description\":\"ECN保税过敏原原材料\",\"id\":27,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:36:41');
INSERT INTO `sys_oper_log` VALUES (476, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"RENA\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:37:29.387+08:00\",\"departmentId\":1,\"description\":\"ECN非保税过敏原原材料\",\"id\":28,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:37:29');
INSERT INTO `sys_oper_log` VALUES (477, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"REBN\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:37:39.844+08:00\",\"departmentId\":1,\"description\":\"ECN保税非过敏原原材料\",\"id\":29,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:37:39');
INSERT INTO `sys_oper_log` VALUES (478, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"RENN\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:37:53.285+08:00\",\"departmentId\":1,\"description\":\"ECN非保税非过敏原原材料\",\"id\":30,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:37:53');
INSERT INTO `sys_oper_log` VALUES (479, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"RNB\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:38:06.827+08:00\",\"departmentId\":3,\"description\":\"NMD保税原材料\",\"id\":31,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:38:06');
INSERT INTO `sys_oper_log` VALUES (480, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"RNN\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:38:20.119+08:00\",\"departmentId\":3,\"description\":\"NMD非保税原材料\",\"id\":32,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:38:20');
INSERT INTO `sys_oper_log` VALUES (481, '物料类型', 1, 'com.bosch.masterdata.controller.MaterialTypeController.add()', 'POST', 1, NULL, NULL, '/materialType', '119.40.69.129', '', '{\"code\":\"RFBA\",\"createBy\":\"\",\"createTime\":\"2022-10-24T14:38:34.058+08:00\",\"departmentId\":2,\"description\":\"FSMP保税过敏原原材料\",\"id\":33,\"params\":{}}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 14:38:34');
INSERT INTO `sys_oper_log` VALUES (482, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1148', '39.144.153.188', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 15:17:53');
INSERT INTO `sys_oper_log` VALUES (483, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1145', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 15:19:25');
INSERT INTO `sys_oper_log` VALUES (484, '物料信息', 1, 'com.bosch.masterdata.controller.MaterialController.addMaterial()', 'POST', 1, 'admin', NULL, '/material/addMaterial', '119.40.69.129', '', '{\"bindPallet\":1,\"code\":\"10302507\",\"errorProofingMethod\":\"称重\",\"hasPallet\":1,\"isAsc\":\"asc\",\"lessDeviationRatio\":11,\"materialTypeId\":33,\"minPackageNetWeight\":1111,\"minPackageNumber\":111,\"name\":\"aaaaa\",\"orderBy\":\"\",\"packageWeight\":100,\"palletWeight\":11111,\"reasonable\":true,\"remark\":\"sad \",\"totalWeight\":1111,\"unit\":\"kg\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 16:36:45');
INSERT INTO `sys_oper_log` VALUES (485, '物料信息', 2, 'com.bosch.masterdata.controller.MaterialController.edit()', 'PUT', 1, 'admin', NULL, '/material/1150', '119.40.69.129', '', '1150 {\"bindPallet\":1,\"code\":\"10302507\",\"errorProofingMethod\":\"称重\",\"hasPallet\":1,\"id\":1150,\"isAsc\":\"asc\",\"lessDeviationRatio\":11,\"materialTypeId\":33,\"minPackageNetWeight\":1111,\"minPackageNumber\":111,\"name\":\"aaaaa\",\"orderBy\":\"\",\"packageWeight\":100,\"palletWeight\":11111,\"reasonable\":true,\"remark\":\"sad \",\"totalWeight\":1111,\"unit\":\"kg\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-24 16:41:28');
INSERT INTO `sys_oper_log` VALUES (486, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1151', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-25 10:30:02');
INSERT INTO `sys_oper_log` VALUES (487, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1149', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-25 10:30:05');
INSERT INTO `sys_oper_log` VALUES (488, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1147', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-25 10:30:09');
INSERT INTO `sys_oper_log` VALUES (489, '物料信息', 3, 'com.bosch.masterdata.controller.MaterialController.remove()', 'DELETE', 1, 'admin', NULL, '/material/1145', '127.0.0.1', '', NULL, '{\"msg\":\"操作成功\",\"code\":200}', 0, 200, NULL, '2022-10-25 10:30:11');
INSERT INTO `sys_oper_log` VALUES (490, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10}', NULL, 1, NULL, '', '2022-10-25 11:00:33');
INSERT INTO `sys_oper_log` VALUES (491, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10}', NULL, 1, NULL, '', '2022-10-25 11:03:10');
INSERT INTO `sys_oper_log` VALUES (492, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10,\"weightTimes\":10}', NULL, 1, NULL, '', '2022-10-25 11:03:44');
INSERT INTO `sys_oper_log` VALUES (493, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10,\"weightTimes\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":10000.0,\"averageResult\":73.490990990991,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T11:05:20.872+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 11:05:24');
INSERT INTO `sys_oper_log` VALUES (494, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":20000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10,\"weightTimes\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":20000.0,\"averageResult\":148.56606606606607,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T11:06:25.292+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 11:06:15');
INSERT INTO `sys_oper_log` VALUES (495, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":15000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10,\"weightTimes\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":15000.0,\"averageResult\":111.02852852852854,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T11:06:42.88+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 11:06:32');
INSERT INTO `sys_oper_log` VALUES (496, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":13000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originPalletQuantity\":10,\"weightTimes\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":13000.0,\"averageResult\":96.01351351351352,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T11:06:53.769+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 11:06:44');
INSERT INTO `sys_oper_log` VALUES (497, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":13000.0,\"mesBarCode\":\"20240322669006391110024752103110432203291127000050\",\"originPalletQuantity\":10,\"weightTimes\":10}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024752103110432203291127000050\",\"operateTime\":\"2022-10-25T13:48:38.817+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":2}}', 0, 200, NULL, '2022-10-25 13:48:29');
INSERT INTO `sys_oper_log` VALUES (498, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\"}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-25T15:07:42.158+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-25 15:07:32');
INSERT INTO `sys_oper_log` VALUES (499, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":100.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\"}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":100.0,\"averageResult\":10.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-25T15:08:33.408+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-25 15:08:23');
INSERT INTO `sys_oper_log` VALUES (500, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":200,\"actualResult\":2000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\"}', '{\"code\":200,\"data\":{\"actualQuantity\":200,\"actualResult\":2000.0,\"averageResult\":10.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-25T15:08:55.617+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-25 15:08:45');
INSERT INTO `sys_oper_log` VALUES (501, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\"}', '{\"code\":200,\"data\":{\"actualQuantity\":1,\"actualResult\":200.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-25T15:09:23.298+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-25 15:09:13');
INSERT INTO `sys_oper_log` VALUES (502, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":111,\"actualResult\":10.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":111,\"actualResult\":10.0,\"averageResult\":-0.8333333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T15:28:27.931+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 15:28:19');
INSERT INTO `sys_oper_log` VALUES (503, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":30,\"actualResult\":30.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-25 15:28:36');
INSERT INTO `sys_oper_log` VALUES (504, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":30.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":30.0,\"averageResult\":-0.5,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T15:28:53.303+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 15:28:43');
INSERT INTO `sys_oper_log` VALUES (505, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":13000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":13000.0,\"averageResult\":215.66666666666669,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T15:29:12.862+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 15:29:02');
INSERT INTO `sys_oper_log` VALUES (506, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-25T15:29:53.584+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-25 15:29:44');
INSERT INTO `sys_oper_log` VALUES (507, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\"}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-26T09:56:03.412+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-26 09:55:54');
INSERT INTO `sys_oper_log` VALUES (508, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":40,\"actualResult\":30.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-26 09:59:19');
INSERT INTO `sys_oper_log` VALUES (509, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":30.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":30.0,\"averageResult\":-0.5,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T09:59:35.589+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 09:59:25');
INSERT INTO `sys_oper_log` VALUES (510, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":13000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":13000.0,\"averageResult\":215.66666666666669,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:00:15.283+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:00:04');
INSERT INTO `sys_oper_log` VALUES (511, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6500.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6500.0,\"averageResult\":107.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:00:31.651+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:00:21');
INSERT INTO `sys_oper_log` VALUES (512, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":40,\"actualResult\":5000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-26 10:00:46');
INSERT INTO `sys_oper_log` VALUES (513, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":5000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":5000.0,\"averageResult\":82.33333333333333,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:01:02.313+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:00:51');
INSERT INTO `sys_oper_log` VALUES (514, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":8000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":8000.0,\"averageResult\":132.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:01:30.422+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:01:19');
INSERT INTO `sys_oper_log` VALUES (515, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":5000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":5000.0,\"averageResult\":82.33333333333333,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:01:45.814+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:01:35');
INSERT INTO `sys_oper_log` VALUES (516, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6500.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6500.0,\"averageResult\":107.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:01:58.556+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:01:48');
INSERT INTO `sys_oper_log` VALUES (517, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:02:12.151+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:02:02');
INSERT INTO `sys_oper_log` VALUES (518, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":30,\"actualResult\":30.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\"}', '{\"code\":200,\"data\":{\"actualQuantity\":30,\"actualResult\":30.0,\"averageResult\":1.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T10:08:19.773+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 10:08:09');
INSERT INTO `sys_oper_log` VALUES (519, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\"}', '{\"code\":200,\"data\":{\"actualQuantity\":1,\"actualResult\":200.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T10:10:23.062+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 10:10:12');
INSERT INTO `sys_oper_log` VALUES (520, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":20}', NULL, 1, NULL, '\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/storage-in/target/classes/mapper/storagein/MaterialInMapper.xml]\n### The error may involve defaultParameterMap\n### The error occurred while setting parameters\n### SQL: select id,                sscc_number,                batch_nb,                material_nb,                check_type,                check_quantity,                quantity,                min_standard,                max_standard,                actual_quantity,                actual_result,                average_result,                operate_user,                operate_time,                origin_pallet_quantity         from si_material_in               where sscc_number = ?         limit 1\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'', '2022-10-26 10:23:51');
INSERT INTO `sys_oper_log` VALUES (521, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":20}', NULL, 1, NULL, '\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'\n### The error may exist in file [/Users/xuhao/IdeaProjects/wms-cloud/wms-modules/storage-in/target/classes/mapper/storagein/MaterialInMapper.xml]\n### The error may involve defaultParameterMap\n### The error occurred while setting parameters\n### SQL: select id,                sscc_number,                batch_nb,                material_nb,                check_type,                check_quantity,                quantity,                min_standard,                max_standard,                actual_quantity,                actual_result,                average_result,                operate_user,                operate_time,                origin_pallet_quantity         from si_material_in               where sscc_number = ?         limit 1\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Unknown column \'origin_pallet_quantity\' in \'field list\'', '2022-10-26 10:26:04');
INSERT INTO `sys_oper_log` VALUES (522, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":20}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'originalPalletQuantity\' not found. Available parameters are [list, param1]', '2022-10-26 10:28:34');
INSERT INTO `sys_oper_log` VALUES (523, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":20}', NULL, 1, NULL, 'nested exception is org.apache.ibatis.binding.BindingException: Parameter \'originalPalletQuantity\' not found. Available parameters are [list, param1]', '2022-10-26 10:29:11');
INSERT INTO `sys_oper_log` VALUES (524, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":20}', '{\"code\":200,\"data\":{\"actualQuantity\":1,\"actualResult\":200.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T10:33:32.743+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 10:33:22');
INSERT INTO `sys_oper_log` VALUES (525, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":20}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-26T10:48:11.487+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-26 10:48:01');
INSERT INTO `sys_oper_log` VALUES (526, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":80}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-26T10:51:52.886+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-26 10:51:42');
INSERT INTO `sys_oper_log` VALUES (527, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6500.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":30,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6500.0,\"averageResult\":107.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:55:20.816+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:55:09');
INSERT INTO `sys_oper_log` VALUES (528, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":50,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T10:55:40.814+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 10:55:29');
INSERT INTO `sys_oper_log` VALUES (529, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":40,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-26 13:20:16');
INSERT INTO `sys_oper_log` VALUES (530, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":40,\"actualResult\":8000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-26 13:21:17');
INSERT INTO `sys_oper_log` VALUES (531, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":8000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":8000.0,\"averageResult\":132.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T13:21:34.858+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 13:21:23');
INSERT INTO `sys_oper_log` VALUES (532, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T13:22:13.774+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 13:22:02');
INSERT INTO `sys_oper_log` VALUES (533, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":10.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":10.0,\"averageResult\":1.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T13:25:15.311+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 13:25:03');
INSERT INTO `sys_oper_log` VALUES (534, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":1,\"actualResult\":200.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T13:26:37.516+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 13:26:26');
INSERT INTO `sys_oper_log` VALUES (535, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-26T13:54:29.143+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-26 13:54:18');
INSERT INTO `sys_oper_log` VALUES (536, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-26T13:54:50.284+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":3,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-26 13:54:38');
INSERT INTO `sys_oper_log` VALUES (537, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":1,\"actualResult\":200.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":1,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T13:55:16.07+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 13:55:04');
INSERT INTO `sys_oper_log` VALUES (538, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-26 13:59:03');
INSERT INTO `sys_oper_log` VALUES (539, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":13,\"actualResult\":1000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":1000.0,\"averageResult\":77.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T14:00:11.94+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 14:00:00');
INSERT INTO `sys_oper_log` VALUES (540, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":13,\"actualResult\":20000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":20000.0,\"averageResult\":1539.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T14:00:58.245+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 14:00:46');
INSERT INTO `sys_oper_log` VALUES (541, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":13,\"actualResult\":2000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":2000.0,\"averageResult\":154.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T14:01:16.518+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 14:01:04');
INSERT INTO `sys_oper_log` VALUES (542, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":13,\"actualResult\":3000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":3000.0,\"averageResult\":231.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T14:01:30.258+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 14:01:18');
INSERT INTO `sys_oper_log` VALUES (543, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":13,\"actualResult\":2600.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":2600.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-26T14:01:41.756+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-26 14:01:30');
INSERT INTO `sys_oper_log` VALUES (544, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":1}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-27T10:16:21.442+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-27 10:16:21');
INSERT INTO `sys_oper_log` VALUES (545, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":40,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":1,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 10:18:30');
INSERT INTO `sys_oper_log` VALUES (546, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":50,\"actualResult\":8000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":1,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":8000.0,\"averageResult\":132.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T10:18:43.837+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 10:18:43');
INSERT INTO `sys_oper_log` VALUES (547, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T10:19:11.423+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 10:19:11');
INSERT INTO `sys_oper_log` VALUES (548, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 10:20:09');
INSERT INTO `sys_oper_log` VALUES (549, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":13,\"actualResult\":1000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":1000.0,\"averageResult\":77.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T10:20:17.18+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 10:20:17');
INSERT INTO `sys_oper_log` VALUES (550, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":13,\"actualResult\":2600.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":2600.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T10:20:31.434+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 10:20:31');
INSERT INTO `sys_oper_log` VALUES (551, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":50,\"actualResult\":8000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":8000.0,\"averageResult\":132.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:04:18.984+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:04:19');
INSERT INTO `sys_oper_log` VALUES (552, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":50,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":50,\"actualResult\":6000.0,\"averageResult\":99.0,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:04:28.336+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":1,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:04:28');
INSERT INTO `sys_oper_log` VALUES (553, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":59,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":50,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":59,\"actualResult\":6000.0,\"averageResult\":83.77118644067798,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":50,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:07:31.239+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:07:31');
INSERT INTO `sys_oper_log` VALUES (554, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":9,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 11:32:44');
INSERT INTO `sys_oper_log` VALUES (555, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":1200.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1200.0,\"averageResult\":98.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:32:59.414+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:32:47');
INSERT INTO `sys_oper_log` VALUES (556, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":300.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":300.0,\"averageResult\":23.333333333333336,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:32:04.517+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:32:54');
INSERT INTO `sys_oper_log` VALUES (557, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1000.0,\"averageResult\":81.66666666666667,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:32:36.531+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:33:06');
INSERT INTO `sys_oper_log` VALUES (558, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"actualQuantity\":10,\"actualResult\":6000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":6000.0,\"averageResult\":498.33333333333337,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T11:31:32.259+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 11:33:46');
INSERT INTO `sys_oper_log` VALUES (559, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":9,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:28:07');
INSERT INTO `sys_oper_log` VALUES (560, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":10,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1000.0,\"averageResult\":81.66666666666667,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:28:16.504+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:28:16');
INSERT INTO `sys_oper_log` VALUES (561, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":10,\"actualResult\":1200.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1200.0,\"averageResult\":98.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:28:27.503+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:28:27');
INSERT INTO `sys_oper_log` VALUES (562, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-27T13:28:55.135+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-27 13:28:55');
INSERT INTO `sys_oper_log` VALUES (563, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:29:35');
INSERT INTO `sys_oper_log` VALUES (564, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":13,\"actualResult\":1000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":1000.0,\"averageResult\":77.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T13:29:54.164+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 13:29:54');
INSERT INTO `sys_oper_log` VALUES (565, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '117.136.67.202', '', '{\"actualQuantity\":13,\"actualResult\":2600.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":2600.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T13:30:02.984+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 13:30:03');
INSERT INTO `sys_oper_log` VALUES (566, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-27T13:40:36.501+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-27 13:40:36');
INSERT INTO `sys_oper_log` VALUES (567, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":9,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:41:31');
INSERT INTO `sys_oper_log` VALUES (568, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":0,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:41:34');
INSERT INTO `sys_oper_log` VALUES (569, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":0,\"actualResult\":0.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:41:38');
INSERT INTO `sys_oper_log` VALUES (570, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":0,\"actualResult\":-1.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:41:41');
INSERT INTO `sys_oper_log` VALUES (571, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":291,\"actualResult\":-1.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":291,\"actualResult\":-1.0,\"averageResult\":-0.8648339060710196,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:41:45.75+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:41:45');
INSERT INTO `sys_oper_log` VALUES (572, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":2121,\"actualResult\":0.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":112,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":2121,\"actualResult\":0.0,\"averageResult\":-0.8372622976583374,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:41:55.314+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:41:55');
INSERT INTO `sys_oper_log` VALUES (573, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":10,\"actualResult\":10000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":121,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":10000.0,\"averageResult\":831.6666666666667,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:42:26.098+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:42:26');
INSERT INTO `sys_oper_log` VALUES (574, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":10,\"actualResult\":1200.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1200.0,\"averageResult\":98.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T13:42:41.365+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 13:42:41');
INSERT INTO `sys_oper_log` VALUES (575, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":1,\"actualResult\":200.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":12}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 13:49:34');
INSERT INTO `sys_oper_log` VALUES (576, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":13,\"actualResult\":1000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":1000.0,\"averageResult\":77.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T13:49:44.79+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 13:49:44');
INSERT INTO `sys_oper_log` VALUES (577, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":13,\"actualResult\":2600.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":2600.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T13:49:58.563+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 13:49:58');
INSERT INTO `sys_oper_log` VALUES (578, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"originalPalletQuantity\":1}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-27T15:44:48.539+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-27 15:44:48');
INSERT INTO `sys_oper_log` VALUES (579, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":9,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 15:46:02');
INSERT INTO `sys_oper_log` VALUES (580, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":9,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 15:46:07');
INSERT INTO `sys_oper_log` VALUES (581, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":10,\"actualResult\":1000.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1000.0,\"averageResult\":81.66666666666667,\"batchNb\":\"2202141190\",\"checkFlag\":false,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T15:46:37.225+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 15:46:37');
INSERT INTO `sys_oper_log` VALUES (582, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":10,\"actualResult\":1200.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"originalPalletQuantity\":10,\"weightTimes\":1}', '{\"code\":200,\"data\":{\"actualQuantity\":10,\"actualResult\":1200.0,\"averageResult\":98.33333333333334,\"batchNb\":\"2202141190\",\"checkFlag\":true,\"checkQuantity\":10,\"checkType\":0,\"checkTypeDesc\":\"称重\",\"fromPurchaseOrder\":\"4500017418\",\"materialName\":\"葡萄糖\",\"materialNb\":\"10302507\",\"maxStandard\":102.0,\"mesBarCode\":\"20250213669006391110024585103025072202141190001000\",\"minStandard\":95.0,\"operateTime\":\"2022-10-27T15:47:07.201+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024585\",\"totalPallet\":4,\"unit\":\"kg\"}}', 0, 200, NULL, '2022-10-27 15:47:07');
INSERT INTO `sys_oper_log` VALUES (583, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":1,\"actualResult\":100.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 15:50:53');
INSERT INTO `sys_oper_log` VALUES (584, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":2,\"actualResult\":100.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":603,\"msg\":\"抽样件数不符合\"}', 0, 603, NULL, '2022-10-27 15:51:00');
INSERT INTO `sys_oper_log` VALUES (585, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":14,\"actualResult\":100.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":14,\"actualResult\":100.0,\"averageResult\":8.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T15:51:17.337+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 15:51:17');
INSERT INTO `sys_oper_log` VALUES (586, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":13,\"actualResult\":1000.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":13,\"actualResult\":1000.0,\"averageResult\":77.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T15:51:39.485+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 15:51:39');
INSERT INTO `sys_oper_log` VALUES (587, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":14,\"actualResult\":1400.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":14,\"actualResult\":1400.0,\"averageResult\":100.0,\"batchNb\":\"2203291126\",\"checkFlag\":false,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T15:51:53.491+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 15:51:53');
INSERT INTO `sys_oper_log` VALUES (588, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '119.40.69.128', '', '{\"actualQuantity\":14,\"actualResult\":2800.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"originalPalletQuantity\":10}', '{\"code\":200,\"data\":{\"actualQuantity\":14,\"actualResult\":2800.0,\"averageResult\":200.0,\"batchNb\":\"2203291126\",\"checkFlag\":true,\"checkQuantity\":13,\"checkType\":1,\"checkTypeDesc\":\"数数\",\"fromPurchaseOrder\":\"4500017408\",\"materialName\":\"一次性注射器\",\"materialNb\":\"10311042\",\"maxStandard\":202.0,\"mesBarCode\":\"20240322669006391110024752103110422203291126000050\",\"minStandard\":195.0,\"operateTime\":\"2022-10-27T15:52:08.308+08:00\",\"operateUser\":\"admin\",\"poNumberItem\":\"10\",\"ssccNumber\":\"669006391110024752\",\"totalPallet\":1,\"unit\":\"PCS\"}}', 0, 200, NULL, '2022-10-27 15:52:08');
INSERT INTO `sys_oper_log` VALUES (589, '入库校验', 1, 'com.bosch.storagein.controller.MaterialInController.check()', 'POST', 1, 'admin', NULL, '/material-in/check', '127.0.0.1', '', '{\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\"}', '{\"code\":200,\"data\":{\"batchNb\":\"2203291127\",\"checkFlag\":true,\"checkType\":2,\"checkTypeDesc\":\"免检\",\"materialName\":\"面粉\",\"materialNb\":\"10311043\",\"mesBarCode\":\"20240322669006391110024753103110432203291127000050\",\"operateTime\":\"2022-10-31T20:13:58.068+08:00\",\"operateUser\":\"admin\",\"ssccNumber\":\"669006391110024753\",\"totalPallet\":1}}', 0, 200, NULL, '2022-10-31 20:13:59');
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2022-09-19 10:14:33', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2022-09-19 10:14:33', '', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2022-09-19 10:14:33', '', NULL, '普通角色');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色和部门关联表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-09-19 10:14:33', 'admin', '2022-09-19 10:14:33', '', NULL, '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2022-09-19 10:14:33', 'admin', '2022-09-19 10:14:33', '', NULL, '测试员');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
COMMIT;

-- ----------------------------
-- Table structure for wms-stock
-- ----------------------------
DROP TABLE IF EXISTS `wms-stock`;
CREATE TABLE `wms-stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bin_id` int(11) DEFAULT NULL,
  `bin_code` int(11) DEFAULT NULL,
  `material_nb` int(11) DEFAULT NULL,
  `batch_nb` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `create_time` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- ----------------------------
-- Records of wms-stock
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;






