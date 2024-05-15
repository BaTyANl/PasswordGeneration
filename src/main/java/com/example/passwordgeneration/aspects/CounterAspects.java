package com.example.passwordgeneration.aspects;

import com.example.passwordgeneration.counter.Counter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Getter
@Setter
@Slf4j
@Component
public class CounterAspects {
    private final Counter counter = new Counter();

    @Pointcut("execution(* com.example.passwordgeneration.service.impl.*.*(..)) ")
    public void allMethods() {
    }

    @Before("allMethods()")
    public void logCounter(){
        counter.increment();
        log.info("Number of service call: {}.", counter.getCounter());
    }
}
