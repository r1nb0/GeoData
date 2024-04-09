package com.example.geodata.aspects;

import com.example.geodata.service.CounterService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(com.example.geodata.controller..*)"
            + " || within(com.example.geodata.service..*)"
            + " || within(com.example.geodata.cache..*)")
    public void allMethods() {

    }

    @Pointcut("within(com.example.geodata.service..*)")
    public void serviceMethods() {

    }

    @Pointcut("@annotation(AspectAnnotation)")
    public void methodsWithAspectAnnotation() {

    }

    @Before("serviceMethods()")
    public void logCounterService(final JoinPoint joinPoint) {
        int requestCounter = CounterService.increment();
        log.info("Increment counter from {}.{}()."
                        + " Current value of counter is {}", joinPoint
                        .getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), requestCounter);
    }

    @Around("methodsWithAspectAnnotation()")
    public Object logEnteringAPI(final ProceedingJoinPoint joinPoint)
            throws Throwable {
        log.info("Enter: {}.{}() with argument[s] = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit: {}.{}() with result = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }

    @AfterThrowing(pointcut = "allMethods()", throwing = "exception")
    public void logsExceptionsFromAnyLocation(final JoinPoint joinPoint,
                                              final Throwable exception) {
        log.error("Exception in : {}.{}() cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), exception.getMessage());
    }

}
