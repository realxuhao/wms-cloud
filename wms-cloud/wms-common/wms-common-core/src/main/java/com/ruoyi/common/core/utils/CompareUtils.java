package com.ruoyi.common.core.utils;

import java.math.BigDecimal;

public class CompareUtils {
    /**
     * 判断两个数字是否相等
     * @param num1 数字1
     * @param num2 数字2
     * @return 是否相等
     */
    public static boolean isEqual(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 0;
    }

    /**
     * 判断数字和字符串是否相等
     * @param num 数字
     * @param str 字符串
     * @return 是否相等
     */
    public static boolean isEqual(BigDecimal num, String str) {
        BigDecimal decimal = new BigDecimal(str);
        return num.compareTo(decimal) == 0;
    }

    /**
     * 判断两个字符串是否相等
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 是否相等
     */
    public static boolean isEqual(String str1, String str2) {
        BigDecimal decimal1 = new BigDecimal(str1);
        BigDecimal decimal2 = new BigDecimal(str2);
        return decimal1.compareTo(decimal2) == 0;
    }
}

