package com.rookiefly.quickstart.dubbo.utils;

/**
 * Runnable的包装类
 */
public class TraceRunnable implements Runnable {

    private Runnable targetRunnable;

    private String traceId;

    private Throwable throwable;

    public TraceRunnable(Runnable targetRunnable) {
        this.targetRunnable = targetRunnable;
        this.traceId = TraceContext.getContext().getTraceId();
    }

    @Override
    public void run() {
        if (targetRunnable == null) {
            return;
        }
        TraceContext.getContext().setTraceId(traceId);
        try {
            targetRunnable.run();
        } catch (Throwable t) {
            setThrowable(t);
        }
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}