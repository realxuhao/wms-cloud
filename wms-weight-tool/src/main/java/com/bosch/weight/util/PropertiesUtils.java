package com.bosch.weight.util;

/**
 * @author xuhao
 * @date 2021年08月18日 8:39
 */

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {
    private static Logger logger = Logger.getLogger(PropertiesUtils.class);


    private final static PropertiesUtils propertiesUtils = new PropertiesUtils();


    /**
     * 采用静态方法
     */
    public static Properties readProperties(String filePath) {

        Properties info = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            info.load(fileInputStream);
        } catch (Exception e) {
            System.out.println("配置读取失败：" + e);
            System.exit(0);
        }

        return info;
    }

    /**
     * 读取属性文件中相应键的值
     *
     * @param key 主键
     * @return String
     */
    public static String getKeyValue(String key, String filePath) {
        Properties props = readProperties(filePath);
        return props.getProperty(key);
    }

    /**
     * 根据主键key读取主键的值value
     *
     * @param key 键名
     */
    public static String readValue(String key, String filePath) {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream(filePath);

            props.load(in);
            String value = props.getProperty(key);
//            System.out.println(key +"键的值是："+ value);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新properties文件的键值对
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     *
     * @param keyName 键名
     * @param value   键值
     */
    public static void updateProperties(String keyName, String value, String filePath) {

        Properties props = readProperties(filePath);
        try {
            props.load(new FileInputStream(filePath));

            // 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
            // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
            OutputStream fos = new FileOutputStream(filePath);
            props.setProperty(keyName, value);
            // 以适合使用 load 方法加载到 Properties 表中的格式，
            // 将此 Properties 表中的属性列表（键和元素对）写入输出流
            props.store(fos, null);
            fos.close();
        } catch (IOException e) {
            System.err.println("属性文件更新错误");
        }
    }

    public static void printProperties(String filePath) {
        Properties properties = readProperties(filePath);
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------开始打印" + filePath + "配置项的值 --------------------------" + "\n");
        for (String name : properties.stringPropertyNames()) {
            sb.append(name + ": " + properties.getProperty(name) + "\n");
        }
        sb.append("--------------------打印" + filePath + "配置项的值结束 --------------------------");
        logger.info(sb.toString());
    }

    public static void main(String[] args) {
        Properties properties = readProperties(PropertiesConstants.CONFIG_PATH);
        for (String name : properties.stringPropertyNames()) {
            System.out.println(name + ": " + properties.getProperty(name));
        }
    }
}


