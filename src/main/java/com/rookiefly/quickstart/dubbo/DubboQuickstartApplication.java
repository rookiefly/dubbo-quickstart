package com.rookiefly.quickstart.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession
public class DubboQuickstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboQuickstartApplication.class, args);
    }

}
