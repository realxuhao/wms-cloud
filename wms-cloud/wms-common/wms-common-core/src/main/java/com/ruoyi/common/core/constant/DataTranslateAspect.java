package com.ruoyi.common.core.constant;

import com.ruoyi.common.core.domain.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

@Aspect
@Component
public class DataTranslateAspect {
    @Pointcut("execution(public * com.bosch.c.controller..*.*(..))")//切入点描述 这个是controller包的切入点
    public void controller(){}//签名，可以理解成这个切入点的一个名称


    @Around("controller()")
    public Object dataTranslate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed();
        R body= (R)o;
        //Field[] declaredFields = proceed.getClass().getDeclaredFields();
//        if(body instanceof String){
//            return R.ok(body);
//        }

        if(body instanceof R){
            if(((R<?>) body).isSuccess()){
                return ((R<?>) body).getData();
            }
            return ((R<?>) body).getData();
        }
        return R.ok(body);
    }
}
