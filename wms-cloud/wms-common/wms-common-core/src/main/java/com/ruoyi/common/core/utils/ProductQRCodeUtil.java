package com.ruoyi.common.core.utils;


import com.ruoyi.common.core.exception.ServiceException;
import org.apache.commons.lang3.ArrayUtils;


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
//        String[] split = qrCode.split("\\r?\\n");
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
        return Arrays.asList(qrCode.substring(0,19),qrCode.substring(19,51),qrCode.substring(51,71));
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

    public static String getBatchNb(String qrCode){
        return splitQRCode(qrCode).get(0).substring(10);
    }

    public static String getSSCC(String qrCode){
        return splitQRCode(qrCode).get(2).substring(2);
    }

    public static void main(String[] args) {
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
        String batchNb = "101235866";
        String expireDate = "240709";
        String quantity = "000992";
        String sscc="369006391113669874";


        String s1="11"+productDate+"10"+batchNb;
        String s2 = "020871690056876317"+expireDate+"37"+quantity;
        String s3="00"+sscc;


        System.out.println(s1+"\n"+s2+"\n"+s3);







    }


}
