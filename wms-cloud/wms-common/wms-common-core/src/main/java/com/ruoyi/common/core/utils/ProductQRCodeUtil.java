package com.ruoyi.common.core.utils;


import com.ruoyi.common.core.exception.ServiceException;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @program: wms-cloud
 * @description: 成品二维码解析工具
 * @author: xuhao
 * @create: 2023-03-28 09:20
 **/
public class ProductQRCodeUtil {


    private static List<String> splitQRCode(String qrCode){
        String[] split = qrCode.split("\\r?\\n");
//        if (ArrayUtils.isEmpty(split)||split.length!=3){
//            throw new ServiceException("成品QR CODE 格式错误");
//        }
//        if (split[0].length()!=19){
//            throw new ServiceException("成品QR CODE 格式错误");
//        }
//        if (split[1].length()!=32){
//            throw new ServiceException("成品QR CODE 格式错误");
//        }
//        if (split[2].length()!=20){
//            throw new ServiceException("成品QR CODE 格式错误");
//        }

        qrCode = qrCode.replace("\\r\\n", "").replace("(","").replace(")","");


        return Arrays.asList(qrCode.substring(0,19),qrCode.substring(19,51),qrCode.substring(51));
    }
    public static Date getProductionDate(String qrCode){
        List<String> strings = splitQRCode(qrCode);

        String date = "";
        try {
            date =strings.get(0).substring(2,8);
        } catch (Exception e) {
            throw new ServiceException("成品QR CODE 格式错误");
        }
        Date parseDate = null;
        String format = "yy-MM-dd";

        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, format, "yyMMdd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseDate;
    }

//    public static String getBatchNb(String qrCode){
//        return splitQRCode(qrCode).get(0).substring(10);
//    }

    public static String getBatchNb(String qrCode){
        Date expireDate = getExpireDate(qrCode);
        return DateUtils.parseDateToStr("yyyy-MM-dd",expireDate);
    }

    //生产订单号
    public static String getLot(String qrCode){
        return splitQRCode(qrCode).get(0).substring(10);
    }



    public static Date getExpireDate(String qrCode){
        String date = "";
        try {
            date = qrCode.substring(37, 43);
        } catch (Exception e) {
            throw new ServiceException("mesBarCode格式错误");
        }
        Date parseDate = null;
        String format = "yy-MM-dd";

        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, format, "yyMMdd");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parseDate;
    }




    public static String getSSCC(String qrCode){
        String s = splitQRCode(qrCode).get(2);
        return s.substring(2);
    }

    public static void main(String[] args) {

        System.out.println(getSSCC("1123070710101326242\\r\\n02069200639400721724100537000060\\r\\n00369006391113830793"));


        System.out.println("11221025101012358660208716900568763172407093700006000369006391113669850".length());
        System.out.println(getSSCC("1122102510101235866\n" +
                "02087169005687631724070937000060\n" +
                "00369006391113669850"));
        System.out.println(getProductionDate("1122102510101235866\n" +
                "02087169005687631724070937000060\n" +
                "00369006391113669850"));
        System.out.println(getBatchNb("1122102510101235866\n" +
                "02087169005687631724070937000060\n" +
                "00369006391113669850"));

        List<String> strings = splitQRCode("11221025101012358660208716900568763172407093700006000369006391113669850");
        System.out.println(strings);


        String productDate = "221025";
        String batchNb = "101207530";
        String expireDate = "240709";
        String quantity = "000060";
        String sscc="369006391113888435";


        String s1="11"+productDate+"10"+batchNb;
        String s2 = "020871690056876317"+expireDate+"37"+quantity;
        String s3="00"+sscc;


        System.out.println((s1+s2+s3).length());

        System.out.println(s1+s2+s3);

        String qrCode = s1+s2+s3;

        System.out.println(getBatchNb(qrCode));

        System.out.println("11221025101012075300208716900568763172407093700006000369006391113888435".length());

        System.out.println("11221025101012075300208716900568763172407093700006000369006391113872175".substring(37,43));



        List<String> ssss= new ArrayList<>();
        ssss.add("aaa");
        ssss.add("bbb");

        System.out.println("sasasa"+ssss);


    }


}
