package com.example.passwordgeneration.aspects;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Logging aspects.
 */
@Aspect
@Slf4j
@Component
public class LoggingAspects {
  @Pointcut("execution(* com.example.passwordgeneration.service.impl.*.*(..)) ")
  public void allMethods() {
  }

  /**
   * Before execution include method their signature will be logged.
   */
  @Before("allMethods()")
  public void logBefore(JoinPoint joinPoint) {
    if (joinPoint.getArgs().length != 0) {
      log.info("Started method " + joinPoint.getSignature().getName() + " with arguments "
          + Arrays.stream(joinPoint.getArgs())
          .map(Object::toString)
          .collect(Collectors.joining(", ")));
    } else {
      log.info("Started method " + joinPoint.getSignature().getName() + " with no arguments");
    }
  }

  @AfterReturning("allMethods()")
  public void logAfterExecuting(JoinPoint joinPoint) {
    log.info("Method " + joinPoint.getSignature().getName() + " executed.");
  }

  /*@AfterThrowing(pointcut = "allMethods()", throwing = "exception")
  public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
    log.info("Method " + joinPoint.getSignature().getName() + " threw an exception "
        + exception.getClass().getSimpleName() + " with message " + exception.getMessage());
  }*/

  @Pointcut("execution(* com.example.passwordgeneration.exceptions.ExceptionsHandler.*(..))")
  public void exceptionMethods() {

  }

  /**
   * Before throwing exception.
   */
  @Before("exceptionMethods()")
  public void logBeforeException(JoinPoint joinPoint) {
    log.error("Threw exception " + joinPoint.getSignature().getName());
  }
}
