/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheHelper<T> {

    private final LinkedHashMap<String, T> linkedHashMapCache = new LinkedHashMap<>();

    private int max;

    public CacheHelper(int max) {
        if (max < 2) {
            max = 2;
        }
        this.max = max;
    }

    public void putCache(String key, T value) {
        if (key == null || value == null) {
            return;
        }
        synchronized (linkedHashMapCache) {
            if (linkedHashMapCache.size() >= max) {
                int needRemove = max / 2;
                List<String> needRemoveKeys = new ArrayList<>();
                Iterator<Map.Entry<String, T>> iterator = linkedHashMapCache.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, T> entry = iterator.next();
                    needRemoveKeys.add(entry.getKey());
                    if (needRemoveKeys.size() >= needRemove) {
                        break;
                    }
                }
                for (int i = 0; i < needRemoveKeys.size(); i++) {
                    linkedHashMapCache.remove(needRemoveKeys.get(i));
                }
            }
            linkedHashMapCache.put(key, value);
        }
    }

    public void removeCache(String key) {
        if (key == null) {
            return;
        }
        synchronized (linkedHashMapCache) {
            linkedHashMapCache.remove(key);
        }
    }

    public T getCache(String key) {
        if (key == null) {
            return null;
        }
        synchronized (linkedHashMapCache) {
            return linkedHashMapCache.get(key);
        }
    }

}
