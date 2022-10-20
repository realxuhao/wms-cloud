package com.bosch.binin.api.util;

public class MesBarCodeUtil {

//    public static String getMaterialCode(String mesBarCode) {
//        return mesBarCode.split();
//    }
    public static String getExpireDate(String mesBarCode) {
        return mesBarCode.substring(0,8);
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
        String mesBarCode="20170826669006391110000015100961661611251128000060";
        System.out.println(getExpireDate(mesBarCode));
        System.out.println(getSSCC(mesBarCode));
        System.out.println(getMaterialNb(mesBarCode));
        System.out.println(getBatchNb(mesBarCode));
        System.out.println(getQuantity(mesBarCode));
    }



}
