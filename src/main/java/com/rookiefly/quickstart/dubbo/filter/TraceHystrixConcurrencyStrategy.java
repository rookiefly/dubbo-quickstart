package com.rookiefly.quickstart.dubbo.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.rookiefly.quickstart.dubbo.utils.TraceContext;

import java.util.concurrent.Callable;

public class TraceHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        String traceId = TraceContext.getContext().getTraceId();
        return () -> {
            TraceContext.getContext().setTraceId(traceId);
            try {
                return callable.call();
            } finally {
                TraceContext.getContext().setTraceId(null);
            }
        };
    }
}