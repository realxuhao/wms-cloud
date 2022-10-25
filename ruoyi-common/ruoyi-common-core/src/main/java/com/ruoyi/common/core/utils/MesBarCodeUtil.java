package com.ruoyi.common.core.utils;


import java.util.Date;

public class MesBarCodeUtil {

//    public static String getMaterialCode(String mesBarCode) {
//        return mesBarCode.split();
//    }
    public static Date getExpireDate(String mesBarCode) {
        String date = mesBarCode.substring(0, 8);
        Date parseDate = DateUtils.parseDate(date);

        return parseDate;
    }

    public static String getSSCC(String mesBarCode){
        return mesBarCode.substring(8,26);
    }

    public static String getMaterialNb(String mesBarCode){
        return mesBarCode.substring(26,34);
    }

    public static String getBatchNb(String mesBarCode){
        return mesBarCode.substring(34,44);
    }

    public static String getQuantity(String mesBarCode){
        return mesBarCode.substring(44,50);
    }

    public static void main(String[] args) {
        String mesBarCode="20240322669006391110024752103110422203291126000050";
        System.out.println(getExpireDate(mesBarCode));
        System.out.println(getSSCC(mesBarCode));
        System.out.println(getMaterialNb(mesBarCode));
        System.out.println(getBatchNb(mesBarCode));
        System.out.println(getQuantity(mesBarCode));
    }

//    20240322669006391110024752103110422203291126000050
    //20250213669006391110024585103025072202141190001000

//20240322669006391110024753103110432203291127000050


}
