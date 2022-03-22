package com.he.annotation;

import com.alibaba.fastjson.JSON;
import com.he.utils.HttpContextUtils;
import com.he.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志切面
 */
@Component
@Slf4j
@Aspect//切面，定义了通知和切点的关系
public class LogAspect {
    //切点 注解加到哪哪就是切点
    @Pointcut("@annotation(com.he.annotation.LogAnnotation)")
    public void logPointCut() {

    }

    //环绕通知
    @Around("logPointCut()")//加入到的切入点
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //保存日志
        recordLog(point, time);
        return result;
    }

    //日志的方法
    private void recordLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        log.info("=====================log start================================");
        log.info("module:{}", logAnnotation.module());
        log.info("operation:{}", logAnnotation.operater());

        //请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("request method:{}", className + "." + methodName + "()");
//        //请求的参数
        Object[] args = point.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}", params);

        //获取request 设置IP地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IpUtils.getIpAddr(request));


        log.info("excute time : {} ms", time);
        log.info("=====================log end================================");
    }
}



