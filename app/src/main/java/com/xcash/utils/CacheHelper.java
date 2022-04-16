 /*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xcash.utils;

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
