package com.ruoyi.common.core.utils;

import com.ruoyi.common.core.exception.ServiceException;
import io.jsonwebtoken.lang.Strings;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 成品二维码解析工具
 * @author: xuhao
 * @create: 2023-03-28 09:20
 **/
public class ProductQRCodeUtil {


    private static String[] splitQRCode(String qrCode){
        String[] split = qrCode.split("\\r?\\n");
        if (ArrayUtils.isEmpty(split)||split.length!=3){
            throw new ServiceException("成品QR CODE 格式错误");
        }
        if (split[0].length()!=19){
            throw new ServiceException("成品QR CODE 格式错误");
        }
        if (split[1].length()!=32){
            throw new ServiceException("成品QR CODE 格式错误");
        }
        if (split[2].length()!=20){
            throw new ServiceException("成品QR CODE 格式错误");
        }
        return split;
    }
    public static Date getProductionDate(String qrCode){
        String[] strings = splitQRCode(qrCode);

        String date = "";
        try {
            date =strings[0].substring(2,8);
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
        return splitQRCode(qrCode)[0].substring(10);
    }

    public static String getSSCC(String qrCode){
        return splitQRCode(qrCode)[2].substring(9);
    }

    public static void main(String[] args) {
        System.out.println(getBatchNb("1122102510101235866\n" +
                "02087169005687631725102337000096\n" +
                "00369006391113419554\n"));
    }


}
