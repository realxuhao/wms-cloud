package com.ruoyi.common.core.utils;

import java.math.BigDecimal;

public class DoubleMathUtil {
    /**
     * double类型计算，避免精度丢失。
     *
     * @param d1
     * @param d2
     * @param operatorType,目前支持“+”、“-”、“*”、“/”
     * @return
     */
    public static double doubleMathCalculation(double d1, double d2, String operatorType) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(d2));
        double resultValue = 0.0;
        switch (operatorType) {
            case "+":
                resultValue = bigDecimal1.add(bigDecimal2).doubleValue();
                break;
            case "-":
                resultValue = bigDecimal1.subtract(bigDecimal2).doubleValue();
                break;
            case "*":
                resultValue = bigDecimal1.multiply(bigDecimal2).doubleValue();
                break;
            case "/":
                // 向上取整，四舍五入
                resultValue = bigDecimal1.divide(bigDecimal2,3,  BigDecimal.ROUND_HALF_UP).doubleValue();
                break;
            default:
                break;
        }
        return resultValue;
    }
}
