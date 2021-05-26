package com.rookiefly.quickstart.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * <p>Add the JVM parameter to connect to the dashboard:</p>
 * {@code -Dcsp.sentinel.dashboard.server=127.0.0.1:8080 -Dproject.name=sentinel-dubbo-quickstart}
 */
@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession
public class DubboQuickstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboQuickstartApplication.class, args);
    }

}
