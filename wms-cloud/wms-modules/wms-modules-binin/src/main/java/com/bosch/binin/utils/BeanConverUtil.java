package com.bosch.binin.utils;

import com.ruoyi.common.core.utils.JwtUtils;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库查询 转换 对外接口展示类
 *
 */
@Slf4j
public class BeanConverUtil {


    /**
     * 单个类转换
     *
     * @param sourceObj
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T conver(Object sourceObj, Class<T> targetClass) {
        if (sourceObj == null || targetClass == null) {
            return null;
        }
        T targetObj = null;
        try {
            targetObj = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("sourceObj:{},targetClass:{}", sourceObj, targetClass, e);
            return null;
        }
        BeanUtils.copyProperties(sourceObj, targetObj);
        return targetObj;
    }

    /**
     * List之间转换
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> converList(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList) || targetClass == null) {
            return Collections.emptyList();
        }
        return sourceList.stream().map(sourceObj -> conver(sourceObj, targetClass)).collect(Collectors.toList());
    }


    public static void main(String[] args) {
        String s= "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVU0VSSUQiOiI0ZGM2YjgxMS0wODQ0LTQzYWMtYTkxOS0zMDFlMGI1ZGNkZTAiLCJQRVJTT05OQU1FIjoiYWRtaW4iLCJST0xFTkFNRSI6InZpZXcsYmFzZSxyZXBvcnQiLCJuYmYiOjE2ODM4NjkyNTksImV4cCI6MTY4Mzg4MzY1OSwiaXNzIjoiYm9zY2ggY29yZSBpZGVudGl0eSIsImF1ZCI6ImJvc2NoIHByZXNlbnRhdGlvbiJ9.Z9PkScx4X14-4hGnMlvNSLyU49vAyc8MzyECxJqk8Fw";
        Claims body = Jwts.parser().setSigningKey("abcdefghijklmn1234567890".getBytes()).parseClaimsJws(s).getBody();
        System.out.println(body.toString());

    }

}

