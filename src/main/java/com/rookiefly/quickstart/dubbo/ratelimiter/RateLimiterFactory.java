package com.rookiefly.quickstart.dubbo.ratelimiter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterFactory {

    private StringRedisTemplate stringRedisTemplate;

    private RedisScript<Boolean> rateLimiterClientLua;

    private String appName;

    private static final ConcurrentHashMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    public RateLimiterFactory(StringRedisTemplate stringRedisTemplate, String appName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.rateLimiterClientLua = rateLimiterLua();
        this.appName = appName;
    }

    private DefaultRedisScript<Boolean> rateLimiterLua() {
        DefaultRedisScript<Boolean> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setLocation(new ClassPathResource("redis_rate_limiter.lua"));
        defaultRedisScript.setResultType(Boolean.class);
        return defaultRedisScript;
    }

    public RateLimiter getRateLimiter(String resourceName, Integer maxQps, Integer timeWindow) {
        if (RATE_LIMITER_CACHE.get(resourceName) == null) {
            RateLimiter rateLimiter = new RateLimiter(stringRedisTemplate, rateLimiterClientLua, appName, resourceName, maxQps, timeWindow);
            RATE_LIMITER_CACHE.put(resourceName, rateLimiter);
            return rateLimiter;
        }
        return RATE_LIMITER_CACHE.get(resourceName);
    }
}