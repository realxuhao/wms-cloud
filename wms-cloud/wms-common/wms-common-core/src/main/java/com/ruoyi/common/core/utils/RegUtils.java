package com.ruoyi.common.core.utils;

import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.exception.ServiceException;

import java.util.regex.Pattern;

public class RegUtils {

    /**
     * 正则-密码强度-基础要求, 必须字母+数字，长度检测不在此处
     */
    public static final String REGEX_PASSWORD_STRENGTH_BASIC = "^(?=.*[0-9]).+$";
    /**
     * 正则-密码强度-至少一位大写字母
     */
    public static final String REGEX_PASSWORD_STRENGTH_UPPER_CHAR = "^(?=.*[A-Z]).+$";
    /**
     * 正则-密码强度-至少一位小写字母
     */
    public static final String REGEX_PASSWORD_STRENGTH_LOWER_CHAR = "^(?=.*[a-z]).+$";

    /**
     * 正则-密码强度-至少一位符号
     */
    public static final String REGEX_PASSWORD_STRENGTH_CHARS = "^(?=.*[`~!@#$%^&*()_\\-+=<>?:\"{}|,./;" +
            "'\\[\\]·~！@#￥%……&*（）——\\-+={}|《》？：“”【】、；‘'，。、]).+$";

    //密码强度检查
    public static void passwordStrengthCheck(String password) {

        try {
            //长度检查
            if (password.length() < UserConstants.PASSWORD_MIN_LENGTH) {
                throw new Exception("密码长度至少需要8位");
            }
            //密码检查，字母和数字、大写字符、特殊符号
            boolean basicMatches = Pattern.compile(REGEX_PASSWORD_STRENGTH_BASIC).matcher(password).matches();
            boolean upperCharMatches = Pattern.compile(REGEX_PASSWORD_STRENGTH_UPPER_CHAR).matcher(password).matches();
            boolean charsMatches = Pattern.compile(REGEX_PASSWORD_STRENGTH_CHARS).matcher(password).matches();
            boolean lowCharMatches = Pattern.compile(REGEX_PASSWORD_STRENGTH_LOWER_CHAR).matcher(password).matches();



            //强检查，字母和数字、大写字母、特殊符号
            if (!basicMatches) {
                throw new Exception("密码必须包含数字");
            }
            if (!upperCharMatches&&!lowCharMatches) {
                throw new Exception("密码必须包含至少一个字母");
            }
//            if (!upperCharMatches) {
//                throw new Exception("密码必须包含至少一个大写字母");
//            }
//
//            if (!lowCharMatches){
//                throw new Exception("密码必须包含至少一个小写字母");
//            }
//            if (!charsMatches) {
//                throw new Exception("密码必须包含至少一个特殊符号");
//            }
        } catch (Exception e) {

            throw new ServiceException(e.getMessage());
        }
    }
}
