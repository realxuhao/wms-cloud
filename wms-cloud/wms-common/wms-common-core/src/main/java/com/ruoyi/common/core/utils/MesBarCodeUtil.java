package com.ruoyi.common.core.utils;


import com.ruoyi.common.core.exception.ServiceException;

import java.text.ParseException;
import java.util.Date;

public class MesBarCodeUtil {


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
            quantity = mesBarCode.substring(44, 50);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        ;
        return Double.valueOf(quantity).toString();
    }

    public static void main(String[] args) {
        String mesBarCode = "20240322669006391110024752103110422203291126000050";
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
        String barCode = generateMesBarCode(new Date(), "669006391113695967", "10310967", "2202141190", Double.valueOf(11200));
        System.out.println(barCode);

    }

//    20240322669006391110024752103110422203291126000050
    //20250213669006391110024585103025072202141190001000
    //20221208669006391110024585103025072202141190001000

//20240322669006391110024753103110432203291127000050


}
