package com.bosch.weight.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheUtil {
    private static final int MAXIMUM_SIZE = 1000;
    private static final long EXPIRE_AFTER_ACCESS_MINUTES = 2;

    private static Cache<Object, Object> cache = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterAccess(EXPIRE_AFTER_ACCESS_MINUTES, TimeUnit.MINUTES)
            .build();

    public static void put(Object key, Object value) {
        cache.put(key, value);
    }

    public static Object get(Object key) {
        return cache.getIfPresent(key);
    }

    public static void remove(Object key) {
        cache.invalidate(key);
    }
}
