package com.example.passwordgeneration.counter;

import org.springframework.stereotype.Component;

@Component
public class Counter {
    private  int count = 0;
    public synchronized void increment(){
        this.count++;
    }

    public synchronized int getCounter(){
        return this.count;
    }
}
