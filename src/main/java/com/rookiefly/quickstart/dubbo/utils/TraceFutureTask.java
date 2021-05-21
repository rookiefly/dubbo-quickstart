package com.rookiefly.quickstart.dubbo.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TraceFutureTask<V> extends FutureTask<V> {

    private String traceId;

    /**
     * @param runnable
     * @param result
     */
    public TraceFutureTask(Runnable runnable, V result) {
        super(runnable, result);
        this.traceId = TraceContext.getContext().getTraceId();
    }

    /**
     * @param callable
     */
    public TraceFutureTask(Callable<V> callable) {
        super(callable);
        this.traceId = TraceContext.getContext().getTraceId();
    }

    @Override
    public void run() {
        TraceContext.getContext().setTraceId(traceId);
        super.run();
    }
}
