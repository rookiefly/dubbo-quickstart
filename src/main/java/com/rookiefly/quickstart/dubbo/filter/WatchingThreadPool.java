package com.rookiefly.quickstart.dubbo.filter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadpool.support.fixed.FixedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_THREAD_NAME;
import static org.apache.dubbo.common.constants.CommonConstants.THREAD_NAME_KEY;

public class WatchingThreadPool extends FixedThreadPool implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WatchingThreadPool.class);

    private static final double ALARM_PERCENT = 0.90; // 线程池预警值

    public static final Map<URL, ThreadPoolExecutor> THEAD_POOL_MAP = new ConcurrentHashMap<>();

    public WatchingThreadPool() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this, 1, 3, TimeUnit.SECONDS);
    }

    @Override
    public Executor getExecutor(URL url) {
        Executor executor = super.getExecutor(url);
        if (executor instanceof ThreadPoolExecutor) {
            THEAD_POOL_MAP.put(url, (ThreadPoolExecutor) executor);
        }

        return executor;
    }

    @Override
    public void run() {
        for (Map.Entry<URL, ThreadPoolExecutor> entry : THEAD_POOL_MAP.entrySet()) {
            URL url = entry.getKey();
            ThreadPoolExecutor threadPoolExecutor = entry.getValue();
            String name = url.getParameter(THREAD_NAME_KEY, DEFAULT_THREAD_NAME);
            // 获取正在活动的线程数
            int activeCount = threadPoolExecutor.getActiveCount();
            // 获取总的线程数
            int corePoolSize = threadPoolExecutor.getCorePoolSize();
            double percent = activeCount / (corePoolSize * 1.0);
            LOGGER.info("[host={}] [thread.name={}]线程池运行状态：{}/{},: {}%", url.getHost(), name, activeCount, corePoolSize, percent * 100);
            if (percent > ALARM_PERCENT) {
                LOGGER.error("超出预警值 : host:{}, 当前使用量 {}%, URL:{}", url.getHost(), percent * 100, url);
            }
        }
    }
}