package com.rookiefly.quickstart.dubbo.filter;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.AppResponse;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboHystrixCommand.class);

    private static final int DEFAULT_THREADPOOL_CORE_SIZE = 30;

    private Invoker invoker;

    private Invocation invocation;

    public DubboHystrixCommand(Invoker invoker, Invocation invocation) {

        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(invoker.getInterface().getName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(String.format("%s_%d", invocation.getMethodName(),
                        invocation.getArguments() == null ? 0 : invocation.getArguments().length)))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(300) //熔断器在整个统计时间内是否开启的阀值，默认20。也就是在metricsRollingStatisticalWindow（默认10s）内至少请求20次，熔断器才发挥起作用
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)// 熔断器中断请求3秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)// 错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(false))// 使用dubbo的超时，禁用这里的超时
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter().withCoreSize(getThreadPoolCoreSize(invoker.getUrl()))
                                .withMaxQueueSize(1000).withQueueSizeRejectionThreshold(700)));// 线程池为30
        this.invoker = invoker;
        this.invocation = invocation;
    }

    /**
     * 获取线程池大小
     *
     * @param url
     * @return
     */
    private static int getThreadPoolCoreSize(URL url) {
        if (url != null) {
            int size = url.getParameter("threadPoolCoreSize", DEFAULT_THREADPOOL_CORE_SIZE);
            LOGGER.debug("threadPoolCoreSize is: {}", size);
            return size;
        }
        return DEFAULT_THREADPOOL_CORE_SIZE;
    }

    @Override
    protected Result run() {
        return invoker.invoke(invocation);
    }

    @Override
    protected Result getFallback() {
        LOGGER.error("come into fall back method,please check it!", getFailedExecutionException());
        LOGGER.error("the execution exception is here!", getExecutionException());
        return new AppResponse(new RuntimeException("server error"));
    }
}