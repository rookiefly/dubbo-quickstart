package com.rookiefly.quickstart.dubbo.config;

import com.rookiefly.quickstart.dubbo.ratelimiter.RateLimiterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RateLimiterConfig {

    @Value("${spring.application.name}")
    private String appName;

    private StringRedisTemplate stringRedisTemplate;

    public RateLimiterConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = "rateLimiterFactory")
    public RateLimiterFactory rateLimiterFactory() {
        return new RateLimiterFactory(stringRedisTemplate, appName);
    }
}
