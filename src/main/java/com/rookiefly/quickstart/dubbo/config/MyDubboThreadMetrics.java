package com.rookiefly.quickstart.dubbo.config;

import com.rookiefly.quickstart.dubbo.filter.WatchingThreadPool;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.BaseUnits;
import org.apache.dubbo.common.URL;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.dubbo.common.constants.CommonConstants.DEFAULT_THREAD_NAME;
import static org.apache.dubbo.common.constants.CommonConstants.THREAD_NAME_KEY;

@Configuration
public class MyDubboThreadMetrics {

    public static final String THREAD_NAME_TAG = "thread.name";

    public static final String HOST_TAG = "host";
    
    @Resource
    private MeterRegistry meterRegistry;

    private ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        schedule.scheduleAtFixedRate(() -> {
            for (Map.Entry<URL, ThreadPoolExecutor> entry : WatchingThreadPool.THEAD_POOL_MAP.entrySet()) {
                ThreadPoolExecutor executor = entry.getValue();
                URL url = entry.getKey();
                String name = url.getParameter(THREAD_NAME_KEY, DEFAULT_THREAD_NAME);
                String host = url.getHost();
                if (executor != null) {
                    Gauge.builder("dubbo.thread.pool.core.size", executor, ThreadPoolExecutor::getCorePoolSize)
                            .description("核心线程数")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.largest.size", executor, ThreadPoolExecutor::getLargestPoolSize)
                            .description("历史最高线程数")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.max.size", executor, ThreadPoolExecutor::getMaximumPoolSize)
                            .description("最大线程数")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.active.size", executor, ThreadPoolExecutor::getActiveCount)
                            .description("活跃线程数")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.thread.count", executor, ThreadPoolExecutor::getPoolSize)
                            .description("当前线程数")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.queue.size", executor, e -> e.getQueue().size())
                            .description("队列大小")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.taskCount", executor, ThreadPoolExecutor::getTaskCount)
                            .description("任务总量")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.completedTaskCount", executor, ThreadPoolExecutor::getCompletedTaskCount)
                            .description("已完成的任务量")
                            .baseUnit(BaseUnits.THREADS)
                            .tags(THREAD_NAME_TAG, name, HOST_TAG, host)
                            .register(meterRegistry);
                }
            }
        }, 1000, 3000, TimeUnit.MILLISECONDS);
    }
}