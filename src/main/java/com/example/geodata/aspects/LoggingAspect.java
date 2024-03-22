package com.example.geodata.aspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

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
        logger.info("Error in the method : {}. Error message : {}", joinPoint, exception.getMessage());
    }

}
