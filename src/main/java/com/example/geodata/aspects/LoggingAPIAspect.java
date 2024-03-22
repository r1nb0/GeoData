package com.example.geodata.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAPIAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAPIAspect.class);

    private final String apiPointcut = "execution(* com.example.geodata.controller.*.*(..))";

    private final String exceptionPointcut = "execution(* com.example.geodata.*.*.*(..))";

    @Pointcut(apiPointcut)
    public void loggingController(){

    }

    @Before("loggingController()")
    public void logEnteringAPI(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String args = Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        logger.info("Received call :: {} - {}. Args : [{}]", request.getMethod(), request.getServletPath(), args);
    }

    @AfterThrowing(value = exceptionPointcut, throwing = "exception")
    public void logsExceptionsFromAnyLocation(JoinPoint joinPoint, Throwable exception) {
        logger.error("Error in the method : {}. Error message : {}", joinPoint.getSignature(), exception.getMessage());
    }

}
