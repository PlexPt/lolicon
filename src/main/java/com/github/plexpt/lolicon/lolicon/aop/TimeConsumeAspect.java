package com.github.plexpt.lolicon.lolicon.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : wh
 * @date : 2020/7/24 10:47
 * 方法执行时间切面
 */
@Aspect
@Component
@Order(4)
@Slf4j
public class TimeConsumeAspect {
    private static final ThreadLocal<Long> firstThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    // 解决方法调用见清除 threadLocal 空指针
    private static final ThreadLocal<Integer> countThreadLocal = new ThreadLocal<>();

    public TimeConsumeAspect() {
        log.info("TimeConsumeAspect inited.....................................");
    }

    @Pointcut("@annotation(com.github.plexpt.lolicon.lolicon.aop.TimeLog)")
    public void fun() {

    }

//    @Around("fun()")
//    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        TimeLog timeLog = method.getAnnotation(TimeLog.class);
//        long startTime = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        Long endTime = System.currentTimeMillis();
//        Long costTime = endTime - startTime;
//        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
//        String methodDesc = timeLog.methodDesc();
//        log.info("methodName: {}, methodDesc: {} ==> 花费时间 {}ms", methodName, methodDesc,
//        costTime);
//        return result;
//    }

    @Before("fun()")
    public void before(JoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法上的 TimeConsumeLog注解
        TimeLog timeConsumeLog =
                methodSignature.getMethod().getAnnotation(TimeLog.class);
        // 如果有内部方法调用
        Integer methodCount = countThreadLocal.get();
        if (methodCount != null) {
            int i = methodCount + 1;
            countThreadLocal.set(i);
        } else {
            countThreadLocal.set(0);
        }
        if (timeConsumeLog != null && countThreadLocal.get() == 0) {
            firstThreadLocal.set(System.currentTimeMillis());
        } else {
            threadLocal.set(System.currentTimeMillis());
        }


    }

    @After(("fun()"))
    public void after(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        TimeLog timeConsumeLog = method.getAnnotation(TimeLog.class);
        Long startTime;
        Integer methodCount = countThreadLocal.get();
        if (timeConsumeLog != null && methodCount == 0) {
            startTime = firstThreadLocal.get();

        } else {
            startTime = threadLocal.get();
            threadLocal.remove();

        }

        Long endTime = System.currentTimeMillis();
        Long costTime = endTime - startTime;

//            String requestUri=method.getAnnotation(RequestMapping.class).value()[0];
        String methodName = method.getDeclaringClass().getName() + "." + method.getName();
        String methodDesc = timeConsumeLog.methodDesc();
        log.info("methodName: {}, methodDesc: {} ==> 花费时间 {}ms", methodName, methodDesc,
                costTime);
        if (methodCount != 0) {
            int i = methodCount - 1;
            countThreadLocal.set(i);
        } else {
            firstThreadLocal.remove();
            countThreadLocal.remove();
        }
    }


}
