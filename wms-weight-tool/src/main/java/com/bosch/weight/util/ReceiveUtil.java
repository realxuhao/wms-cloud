package com.bosch.weight.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.bosch.weight.dto.WeightDTO;
import org.apache.log4j.Logger;

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


    public static void main(String[] args) throws IOException {

//        // 1.创建接受对象
        DatagramSocket socket1 = new DatagramSocket(6666);

//        listenPort(socket1);


    }

    public static void listenPort(DatagramPacket packet, byte[] buffer) throws IOException {


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

    private static void task(String rs, DatagramPacket packet) throws IOException {


        String[] s = rs.split(" ");

        if (validMsg(s)) {
            logger.info("校验成功");

            String hostAddress = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            Double totalWeight = getTotalWeight(s, 48, 51);
            WeightDTO weightDTO = new WeightDTO(hostAddress, port, totalWeight);
            System.out.println(JSONUtil.toJsonStr(weightDTO));
            //称重>0的时候，进行请求
            if (weightDTO.getTotalWeight() > 0) {
                logger.info("收到有效称重数据:" + JSONUtil.toJsonStr(weightDTO));
                //查看缓存，如果两分钟内，如果实现相同数据则不进行上传
                if (!Objects.isNull(CacheUtil.get(weightDTO.getIp() + ":" + weightDTO.getPort()))) {
                    return;
                } else {
                    //不存在缓存，那么就上传数据。
                    FlexibleThreadPool.submitTask(() -> {
                        HttpUtil.createPost(Constants.uploadUrl)
                                .contentType("application/json")
                                .body(JSONUtil.toJsonStr(weightDTO)).execute().body();
                    });
                    //数据放到缓存
                    CacheUtil.put(weightDTO.getIp() + ":" + weightDTO.getPort(), JSONUtil.toJsonStr(weightDTO));
                }
            }

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
        logger.info("start valid data :" + JSONUtil.toJsonStr(s));
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


