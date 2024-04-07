package com.example.passwordgeneration.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class InMemoryCache {
    private final Map<String, Object> cache = new CustomLinkedHashMap<>(20);
    public void put(String key, Object value){
        cache.put(key, value);
    }
    public Object get(String key){
        return cache.get(key);
    }
    public void remove(String key){
        cache.remove(key);
    }
}
