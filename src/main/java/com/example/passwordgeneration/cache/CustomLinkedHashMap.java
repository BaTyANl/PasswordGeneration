package com.example.passwordgeneration.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CustomLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public CustomLinkedHashMap(int maxSize){
        super(maxSize, 0.75f, true);
        this.maxSize = maxSize;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest){
        return size() > maxSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomLinkedHashMap<?,?> that)){
            return false;
        }
        if(!super.equals(o)){
            return false;
        }
        return Objects.equals(maxSize, that.maxSize);
    }

    @Override
    public int hashCode(){
        return  Objects.hash(super.hashCode(), maxSize);
    }
}
