package com.example.config.multitenant;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class Aop {
    @Before("execution(public void com.example.config.multitenant.database.DataSourceBasedMultiTenantConnectionProviderImpl.scheduleTaskWithReloadDataSource())")
    public static void logStart(JoinPoint joinPoint){
        //获取到目标方法运行是使用的参数
        Object[] args = joinPoint.getArgs();
        //获取到方法签名
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("【"+name+"】方法开始执行，用的参数列表【"+ Arrays.asList(args)+"】");
    }
}
