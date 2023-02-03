package com.ruoyi.common.log.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @date
 * @remark
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WMSLog {
    //指定参数
    public String moveType() default "";

    String[] ssccArray() default "";
}

