package com.rookiefly.quickstart.dubbo.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.store.DataStore;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@Configuration
public class DubboThreadMetrics {

    @Resource
    private MeterRegistry meterRegistry;

    private ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        schedule.scheduleAtFixedRate(() -> {
            DataStore dataStore = ExtensionLoader.getExtensionLoader(DataStore.class).getDefaultExtension();
            Map<String, Object> executors = dataStore.get(CommonConstants.EXECUTOR_SERVICE_COMPONENT_KEY);
            for (Map.Entry<String, Object> entry : executors.entrySet()) {
                ExecutorService executor = (ExecutorService) entry.getValue();
                if (executor instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
                    Gauge.builder("dubbo.thread.pool.core.size", tp, ThreadPoolExecutor::getCorePoolSize)
                            .description("核心线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.largest.size", tp, ThreadPoolExecutor::getLargestPoolSize)
                            .description("历史最高线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.max.size", tp, ThreadPoolExecutor::getMaximumPoolSize)
                            .description("最大线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.active.size", tp, ThreadPoolExecutor::getActiveCount)
                            .description("活跃线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.thread.count", tp, ThreadPoolExecutor::getPoolSize)
                            .description("当前线程数")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.queue.size", tp, e -> e.getQueue().size())
                            .description("队列大小")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.taskCount", tp, ThreadPoolExecutor::getTaskCount)
                            .description("任务总量")
                            .baseUnit("threads")
                            .register(meterRegistry);
                    Gauge.builder("dubbo.thread.pool.completedTaskCount", tp, ThreadPoolExecutor::getCompletedTaskCount)
                            .description("已完成的任务量")
                            .baseUnit("threads")
                            .register(meterRegistry);
                }
            }
        }, 1000, 3000, TimeUnit.MILLISECONDS);
    }
}