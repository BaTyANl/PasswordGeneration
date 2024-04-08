package com.example.passwordgeneration.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * CustomLinkedHashMap class for inMemory cache.
 */

public class CustomLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

  private final int maxSize;

  public CustomLinkedHashMap(int maxSize) {
    super(maxSize, 0.75f, true);
    this.maxSize = maxSize;
  }

  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }

  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if (this == o) {
      result = true;
    } else if (o instanceof CustomLinkedHashMap<?, ?> that) {
      if (super.equals(o)) {
        result = Objects.equals(maxSize, that.maxSize);
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    return  Objects.hash(super.hashCode(), maxSize);
  }
}
