package com.ruoyi.common.log.aspect;

import com.bosch.system.api.domain.SysMoveLog;
import com.ruoyi.common.log.annotation.WMSLog;
import com.ruoyi.common.log.service.AsyncLogService;
import com.ruoyi.common.log.service.WMSLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Aspect
@Component
public class WMSLogAspect {

    @Autowired
    private WMSLogService wmsLogService;
    /**
     * 声明AOP签名
     */
    @Pointcut("@annotation(com.ruoyi.common.log.annotation.WMSLog)")
    public void pointcut() {
    }
    // 使用@Before，需要先引入上面@Pointcut注解的方法名，在加上@annotation，
    // @annotation中的值，需要和action方法中的参数名称相同（必须相同，但是名称任意）
    @Before("pointcut() && @annotation(wmsLog)")
    public void before(WMSLog wmsLog)throws IOException {
        System.out.println("Annotation value : " + wmsLog.moveType()+wmsLog.ssccArray().toString());

    }
    @After("pointcut()")
    public void after(JoinPoint joinPoint)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
//获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入方法的对象
        Method method = signature.getMethod();
        //获取方法上的Aop注解
        WMSLog annotation = method.getAnnotation(WMSLog.class);
        //获取注解上的值如 : @MyAnnotation(key = "'param id is ' + #id")
        String keyEl = annotation.moveType();
        //将注解的值中的El表达式部分进行替换
        //创建解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        //获取表达式
        Expression expression = parser.parseExpression(keyEl);
        //设置解析上下文(有哪些占位符，以及每种占位符的值)
        EvaluationContext context = new StandardEvaluationContext();
        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取运行时参数的名称
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i],args[i].toString());
        }
        //解析,获取替换后的结果
        String result = expression.getValue(context).toString();

        System.out.println(result);


        SysMoveLog sysMoveLog=new SysMoveLog();

        wmsLogService.saveMoveLog(sysMoveLog);

    }

}
