package com.billpayment.billpaydemo.aop.customAnnotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogMethodAspect {

    @Around("@annotation(com.billpayment.billpaydemo.aop.customAnnotation.LogMethod)")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("::::::::: Executing {}", MethodSignature.class.cast(joinPoint.getSignature()).getMethod().getName());
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        log.info("::::::::::: Executed {}: with parameters : {} : executed in {} ms",
                MethodSignature.class.cast(joinPoint.getSignature()).getMethod().getName(),
                joinPoint.getArgs(),
                System.currentTimeMillis() - start);
        return result;
    }
}
