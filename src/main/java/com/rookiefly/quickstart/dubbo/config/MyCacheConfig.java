package com.rookiefly.quickstart.dubbo.config;

import com.rookiefly.quickstart.dubbo.utils.CacheKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyCacheConfig {

    @Bean("myKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CacheKeyGenerator();
    }
}