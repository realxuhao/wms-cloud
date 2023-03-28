package com.ruoyi.common.core.utils;

import com.ruoyi.common.core.exception.ServiceException;
import io.jsonwebtoken.lang.Strings;

import java.util.Date;

/**
 * @program: wms-cloud
 * @description: 成品二维码解析工具
 * @author: xuhao
 * @create: 2023-03-28 09:20
 **/
public class ProductQRCodeUtil {


    private static String[] splitQRCode(String qrCode){
        return qrCode.split("\\r?\\n");
    }
    public static Date getProductionDate(String qrCode){
        String[] strings = splitQRCode(qrCode);

        String date = "";
        try {
            date =strings[0].substring(2,8);
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
}
