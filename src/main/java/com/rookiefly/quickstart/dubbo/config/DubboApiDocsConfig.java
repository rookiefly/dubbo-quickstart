package com.rookiefly.quickstart.dubbo.config;

import org.apache.dubbo.apidocs.EnableDubboApiDocs;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableDubboApiDocs
@Profile("dev")
public class DubboApiDocsConfig {

}
