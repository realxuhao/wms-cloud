package com.bosch.weight.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.bosch.weight.dto.WeightDTO;
import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;


/**
 * @program: broadcasttest
 * @description:
 * @author: xuhao
 * @create: 2023-02-13 10:02
 **/

public class ReceiveUtil {

    private static Logger logger = Logger.getLogger(ReceiveUtil.class);
    private static Map headerMap = new HashMap<>();


    public static void main(String[] args) throws IOException, InterruptedException {


////        // 1.创建接受对象
//        DatagramSocket socket1 = new DatagramSocket(6666);
//
////        listenPort(socket1);
//
//        String[] s = new String[]{"53", "54", "41", "54", "45", "3A", "20", "32", "33", "2D", "30", "35", "2D", "30", "38", "20", "31", "36", "3A", "34", "31", "3A", "30", "33", "00", "8E", "04", "01", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "24", "00", "00", "00", "8D", "7B", "34", "44", "D2", "02", "00", "00", "00", "00", "00", "00", "D2", "02", "00", "00", "02", "66", "96", "5F", "45", "02", "00", "D8", "97", "45", "02", "00", "80", "18", "45", "02", "00", "10", "E1", "44", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "00", "0E", "77"};
//        while (true) {
//            if (validMsg(s)) {
//                logger.info("校验成功");
//
//                Double totalWeight = getTotalWeight(s, 48, 51);
//                WeightDTO weightDTO = new WeightDTO("localhost", 8888, totalWeight);
//                System.out.println(JSONUtil.toJsonStr(weightDTO));
//                //称重>0的时候，进行请求
////            if (weightDTO.getTotalWeight() > 0) {
//                logger.info("收到有效称重数据:" + JSONUtil.toJsonStr(weightDTO));
//                //查看缓存，如果两分钟内，如果实现相同数据则不进行上传
//                if (!Objects.isNull(CacheUtil.get(weightDTO.getIp() + ":" + weightDTO.getPort()))
//                        && CacheUtil.get(weightDTO.getIp() + ":" + weightDTO.getPort()).equals(weightDTO.getTotalWeight())) {
//                    return;
//                } else {
//                    //不存在缓存，那么就上传数据。
//                    FlexibleThreadPool.submitTask(() -> {
//                        String body = HttpUtil.createPost(Constants.uploadUrl)
//                                .contentType("application/json")
//                                .body(JSONUtil.toJsonStr(weightDTO)).execute().body();
//                        System.out.println(body);
//                    });
//                    //数据放到缓存
//                    CacheUtil.put(weightDTO.getIp() + ":" + weightDTO.getPort(), weightDTO.getTotalWeight());
//                }
////            }
//
//            }
//            Thread.sleep(1000);
//        }


    }

    public static void listenPort(DatagramPacket packet, byte[] buffer) throws IOException, InterruptedException {


        // 4.取出数据
        int len = packet.getLength();
        byte[] data = Arrays.copyOfRange(buffer, 0, len);


        String rs = getBufHexStr(data);
        logger.info("收到消息将16进制的byte数组转换成字符串：" + rs);
        task(rs, packet);

//            FlexibleThreadPool.submitTask(() -> {
//                System.out.println("#############" + packet.getPort() + "*********************");
//
//                try {
//                    task(rs, packet);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });

    }

    //将16进制的byte数组转换成字符串
    public static String getBufHexStr(byte[] raw) {
        String HEXES = "0123456789ABCDEF";
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
            hex.append(" ");
        }
        return hex.toString();
    }


    private static void task(String rs, DatagramPacket packet) throws IOException, InterruptedException {
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");


        String[] s = rs.split(" ");

        if (validMsg(s)) {
            logger.info("校验成功");

            String hostAddress = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            Double totalWeight = getTotalWeight(s, 48, 51);
            WeightDTO weightDTO = new WeightDTO(hostAddress, port, totalWeight);
            //称重>0的时候，进行请求
//            if (weightDTO.getTotalWeight() > 0) {
            logger.info("收到有效称重数据:" + JSONUtil.toJsonStr(weightDTO));
            //查看缓存，如果两分钟内，如果实现相同数据则不进行上传
            if (!Objects.isNull(CacheUtil.get(weightDTO.getIp() + ":" + weightDTO.getPort())) &&
                    CacheUtil.get(weightDTO.getIp() + ":" + weightDTO.getPort()).equals(weightDTO.getTotalWeight())) {
                return;
            } else {
                //不存在缓存，那么就上传数据。
                int maxRetries = 3; // 最大重试次数
                int retryCount = 0; // 当前重试次数
                boolean success = false; // 是否请求成功
                Map headerMap = new HashMap<>();
                headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");

                while (retryCount < maxRetries && !success) {
                    try {
                        // 发送请求
                        HttpResponse response = HttpRequest.post(Constants.uploadUrl).body(JSONUtil.toJsonStr(weightDTO)).addHeaders(headerMap).execute();

                        // 检查响应状态码
                        int statusCode = response.getStatus();
                        if (statusCode == 200) {
                            success = true; // 请求成功
                            String result = response.body();
                            logger.info(result);
                        } else {
                            // 请求失败，进行重试
                            retryCount++;
                            logger.info("Request failed. Retrying... Retry Count: " + retryCount);
                        }
                    } catch (Exception e) {
                        // 请求异常，进行重试
                        retryCount++;
                        logger.error("Request failed with exception. Retrying... Retry Count: " + retryCount);
                    }
                }

                if (!success) {
                    logger.error("Request failed after maximum retries.");
                }

                //数据放到缓存
                CacheUtil.put(weightDTO.getIp() + ":" + weightDTO.getPort(), weightDTO.getTotalWeight());
            }
//            }

        } else {
            logger.info("valid failed");
        }

    }

    private static Double getTotalWeight(String[] s, int fromIndex, int toIndex) {

        List<String> list = Arrays.asList(s);
        List<String> strings = list.subList(fromIndex, toIndex);
        Collections.reverse(strings);
        String join = String.join("", strings);
        int reversedDecimalValue = Integer.parseInt(join, 16);
        if (reversedDecimalValue > 32767) {
            reversedDecimalValue -= 65536;
        }

        //小数点位数
        int pointNumber = Integer.parseUnsignedInt(s[27], 16);

        return new BigDecimal((float) reversedDecimalValue / Math.pow(10, pointNumber)).setScale(pointNumber, BigDecimal.ROUND_HALF_UP).doubleValue();


    }

    private static Boolean validMsg(String[] s) {
        logger.info("start valid data :" + s.length);
        //长度校验
        if (s.length != 142) {
            return false;
        }
        String res = "0";

        //校验是否符合协议
        for (int i = 0; i < s.length - 2; i++) {
            res = hexAdd(res, s[i]);
        }
        long l = Long.parseLong(res, 16);
        long temp1 = (l >> 8) & 0x00ff;
        long temp2 = l & 0x00ff;

        long target1 = Long.parseLong(s[140], 16);
        long target2 = Long.parseLong(s[141], 16);

        if (temp1 != target1 || target2 != temp2) {
            return false;
        }

        //校验称重数据是否有效
        String string = Long.toBinaryString(Long.parseLong(s[40], 16));
        char[] chars = string.toCharArray();


        if (chars.length < 5 || chars[chars.length - 5 - 1] != '1') {
            return false;
        } else if (chars[chars.length - 2 - 1] != '1') {
            return false;
        }
        return true;
    }


    private static String hexAdd(String s1, String s2) {
        return Long.toHexString(Long.parseLong(s1, 16) + Long.parseLong(s2, 16));
    }

}


