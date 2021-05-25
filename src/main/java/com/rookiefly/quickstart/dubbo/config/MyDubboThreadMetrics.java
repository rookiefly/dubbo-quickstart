package com.rookiefly.quickstart.dubbo.config;

import com.rookiefly.quickstart.dubbo.filter.WatchingThreadPool;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.dubbo.common.URL;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MyDubboThreadMetrics {

    @Resource
    private MeterRegistry meterRegistry;

    private ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        schedule.scheduleAtFixedRate(() -> {
            for (Map.Entry<URL, ThreadPoolExecutor> entry : WatchingThreadPool.THEAD_POOL_MAP.entrySet()) {
                ThreadPoolExecutor executor = entry.getValue();
                if (executor != null) {
                    Gauge.builder("dubbo.thread.pool.core.size", executor, ThreadPoolExecutor::getCorePoolSize)
                            .description("核心线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.largest.size", executor, ThreadPoolExecutor::getLargestPoolSize)
                            .description("历史最高线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.max.size", executor, ThreadPoolExecutor::getMaximumPoolSize)
                            .description("最大线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.active.size", executor, ThreadPoolExecutor::getActiveCount)
                            .description("活跃线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.thread.count", executor, ThreadPoolExecutor::getPoolSize)
                            .description("当前线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.queue.size", executor, e -> e.getQueue().size())
                            .description("队列大小")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.taskCount", executor, ThreadPoolExecutor::getTaskCount)
                            .description("任务总量")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.completedTaskCount", executor, ThreadPoolExecutor::getCompletedTaskCount)
                            .description("已完成的任务量")
                            .baseUnit("threads")
                            .register(meterRegistry);
                }
            }
        }, 1000, 3000, TimeUnit.MILLISECONDS);
    }
}