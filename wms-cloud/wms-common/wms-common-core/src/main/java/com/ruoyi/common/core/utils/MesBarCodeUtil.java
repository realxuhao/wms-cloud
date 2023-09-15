package com.ruoyi.common.core.utils;


import com.ruoyi.common.core.exception.ServiceException;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class MesBarCodeUtil {

    private static List<String> getRandomNonConsecutive(List<String> list) {
        List<String> result = new ArrayList<>();
        list = list.stream().sorted().collect(Collectors.toList());
        if (list.size() < 3) {
            return result;
        }

        for (int i = 0; i < list.size() - 2; i++) {
            result.add(list.get(i));
            result.add(list.get(i + 1));
            result.add(list.get(i + 2));
            break;
        }
        return result;
    }
    public static String generateMesBarCode(Date date, String sscc, String materialNb, String batchNb, Double quantity) {
        String dateToStr = DateUtils.parseDateToStr("yyyyMMdd", date);
        String quantityStr = String.valueOf(quantity.intValue());
        String reverse = StringUtils.reverse(quantityStr);
        int len = 6 - reverse.length();
        for (int i = 0; i < len; i++) {
            reverse = reverse + "0";
        }
        String temp = StringUtils.reverse(reverse);
        return dateToStr + sscc + materialNb + batchNb + temp;
    }

    public static Date getExpireDate(String mesBarCode) {
        String date = "";
        try {
            date = mesBarCode.substring(0, 8);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        Date parseDate = null;
        String format = "yyyy-MM-dd";

        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, format, "yyyyMMdd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseDate;
    }

    public static String getSSCC(String mesBarCode) {
        String sscc = "";
        try {
            sscc = mesBarCode.substring(8, 26);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        return sscc;
    }

    public static String getMaterialNb(String mesBarCode) {
        String materialNb = "";
        try {
            materialNb = mesBarCode.substring(26, 34);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        return materialNb;
    }

    public static String getBatchNb(String mesBarCode) {
        String batchNb = "";
        try {
            batchNb = mesBarCode.substring(34, 44);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }

        return batchNb;
    }

    public static String getQuantity(String mesBarCode) {
        String quantity = "";
        try {
            quantity = mesBarCode.substring(44);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        return Double.valueOf(quantity).toString();
    }

    public static void main(String[] args) {
        String mesBarCode = "202403226690063911100247521031104222032911260050.2666666";
        System.out.println(mesBarCode.length());
        System.out.println(getExpireDate(mesBarCode));
        System.out.println(getSSCC(mesBarCode));
        System.out.println(getMaterialNb(mesBarCode));
        System.out.println(getBatchNb(mesBarCode));
        System.out.println(getQuantity(mesBarCode));
        double ceil = Math.ceil(Double.valueOf(50) / Double.valueOf(1000));
        System.out.println(ceil);
        double ceil1 = Math.ceil(50 / 1000);
        System.out.println(ceil1);

        String date = DateUtils.parseDateToStr("yyyyMMdd", new Date());
        String barCode = generateMesBarCode(new Date(), "669006391114463779", "10305017", "2309081222", Double.valueOf(760));
        System.out.println(barCode);

        System.out.println("202408316690063911132813671045604522061011139.65".length());

        System.out.println(mesBarCode.length());

        System.out.println(Long.valueOf("669006391113925957"));


        System.out.println("Nisdjhsai\nFnaisdofjsadionidsafn\nNini");

        String quantityStr = String.valueOf(Double.valueOf(4.8));
        System.out.println(quantityStr);

        System.out.println(!DateUtils.parseDate("17/04/2022").before(new Date()));


        System.out.println("20230613669006391114227739103024112306021136".length());

        Double aDouble = Double.valueOf(760);
        Double aDouble1 = Double.valueOf(0.6);
        System.out.println(aDouble-aDouble1);


        ArrayList<String> list = new ArrayList<>();
        list.add("669006391113925951");
        list.add("669006391113925953");
        list.add("669006391113925954");
        list.add("669006391113925955");
        list.add("669006391113925957");
        List<String> randomNonConsecutive = getRandomNonConsecutive(list);
        System.out.println(randomNonConsecutive);


        Double n=new Double(0);
        System.out.println(n==0);

    }

//    20240322669006391110024752103110422203291126000050
    //20250213669006391110024585103025072202141190001000
    //20221208669006391110024585103025072202141190001000

//20240322669006391110024753103110432203291127000050


}
