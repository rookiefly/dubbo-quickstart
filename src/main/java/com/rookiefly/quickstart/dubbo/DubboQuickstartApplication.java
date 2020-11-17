package com.rookiefly.quickstart.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DubboQuickstartApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboQuickstartApplication.class, args);
	}

}
