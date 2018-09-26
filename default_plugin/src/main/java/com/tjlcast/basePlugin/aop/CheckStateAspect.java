package com.tjlcast.basePlugin.aop;

import com.tjlcast.basePlugin.service.DefaultService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckStateAspect {

    @Autowired
    DefaultService service;

    @Pointcut("@annotation(com.tjlcast.basePlugin.aop.ConfirmActive)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object before(ProceedingJoinPoint point) throws Throwable {

        if(service.getState().equals("ACTIVE")){
            return point.proceed();
        }
        else{
            return new AsyncResult<String>("插件暂停中");
        }

    }
}
