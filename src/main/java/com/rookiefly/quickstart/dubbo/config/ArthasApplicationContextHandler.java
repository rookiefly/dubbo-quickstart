package com.rookiefly.quickstart.dubbo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Arthas ApplicationContext Handler
 * ognl '#context=@com.rookiefly.quickstart.dubbo.config.ArthasApplicationContextHandler@applicationContext,#context.getEnvironment().getProperty("arthas.agent-id")'
 */
@Component
@Slf4j
public class ArthasApplicationContextHandler implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        log.info("arthas.agent-id -> {}", applicationContext.getEnvironment().getProperty("arthas.agent-id"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ArthasApplicationContextHandler.applicationContext = applicationContext;
    }
}