package com.example.geodata.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingCachingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingCachingAspect.class);

    @Pointcut("execution(* com.example.geodata.cache.impl.*.*(..))")
    public void loggingCache(){

    }

    @Before("loggingCache()")
    public void beginTransaction(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Caching method: {}. Args: [{}]", joinPoint, args);
    }

    @AfterReturning(pointcut = "loggingCache()", returning = "obj")
    public Object afterReturningCallAt(Optional<?> obj){
        if (obj.isEmpty()){
            logger.info("Cache is not contains this object. " +
                    "The object will be retrieved from the repository.");
        }else{
            logger.info("Object retrieved from cache.");
        }
        return obj;
    }

}
