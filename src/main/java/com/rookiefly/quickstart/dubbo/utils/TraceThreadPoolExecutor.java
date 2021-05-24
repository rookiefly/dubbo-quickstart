package com.rookiefly.quickstart.dubbo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

    /**
     * 线程池运行日志类
     */
    private static final Logger runLogger = LoggerFactory.getLogger("treadPoolRunLogger");

    /**
     * 线程统计日志类
     */
    private static final Logger staLogger = LoggerFactory.getLogger("treadPoolStaLogger");

    /**
     * 线程变量，用于保存线程开始执行的时间
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 线程名称
     */
    private ThreadLocal<String> threadName = new ThreadLocal<>();

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 线程已执行的任务数
     */
    private long executedTaskCount;

    /**
     * 线程池统计任务
     */
    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Returns a <tt>RunnableFuture</tt> for the given runnable and default
     * value.
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new TraceFutureTask<>(runnable, value);
    }

    /**
     * Returns a <tt>RunnableFuture</tt> for the given callable task.
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new TraceFutureTask<>(callable);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new ThreadFactoryBuilder("trace-pool-thread"), new AbortPolicy());
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param handler
     */
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                new ThreadFactoryBuilder("trace-pool-thread"), handler);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactoryBuilder
     */
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder, new AbortPolicy());
    }

    /**
     * Executes the given task sometime in the future. The task may execute in a
     * new thread or in an existing pooled thread.
     * <p>
     * If the task cannot be submitted for execution, either because this
     * executor has been shutdown or because its capacity has been reached, the
     * task is handled by the current {@code RejectedExecutionHandler}.
     *
     * @param command the task to execute
     * @throws RejectedExecutionException at discretion of {@code RejectedExecutionHandler}, if the
     *                                    task cannot be accepted for execution
     * @throws NullPointerException       if {@code command} is null
     */
    @Override
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();

        TraceRunnable run = new TraceRunnable(command);

        super.execute(run);
    }

    /**
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactoryBuilder
     * @param handler
     */
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> workQueue, ThreadFactoryBuilder threadFactoryBuilder,
                                   RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactoryBuilder.build(), handler);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder.build());
        scheduledExecutorService.scheduleWithFixedDelay(new ThreadPoolStatistics(), 5, 60, TimeUnit.SECONDS);
        executedTaskCount = 0;
        threadPoolName = threadFactoryBuilder.getName();
    }

    /**
     * beforeExecute
     *
     * @param t the thread that will run task {@code r}
     * @param r the task that will be executed
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTime.set(System.currentTimeMillis());
        String treadName = t.getName();
        threadName.set(treadName);
        super.beforeExecute(t, r);
    }

    /**
     * afterExecute
     *
     * @param r the runnable that has completed
     * @param t the exception that caused termination, or null if execution
     *          completed normally
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        int result = 0;
        if (hasException(t, r)) {
            result = 1;
        }
        long useTime = System.currentTimeMillis() - startTime.get();

        if (useTime > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(threadName.get());
            sb.append(",");
            sb.append(result);
            sb.append(",");
            sb.append(useTime);

            runLogger.info(sb.toString());
        }
        startTime.remove();
        threadName.remove();
        TraceContext.clear();
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        scheduledExecutorService.shutdownNow();
        return super.shutdownNow();
    }

    /**
     * 判断该Runnable是否有异常
     *
     * @param t
     * @param r
     * @return
     */
    private boolean hasException(Throwable t, Runnable r) {

        if (t != null) {
            return true;
        }

        if (r instanceof TraceRunnable) {
            return ((TraceRunnable) r).getThrowable() != null;

        } else if (r instanceof FutureTask) {

            try {
                ((Future<?>) r).get();

            } catch (InterruptedException ce) {
                Thread.currentThread().interrupt();

            } catch (Exception ee) {
                return true;
            }
        }

        return false;
    }

    class ThreadPoolStatistics implements Runnable {

        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            int maxPoolSize = getMaximumPoolSize();
            int corePoolSize = getCorePoolSize();
            int activeCount = getActiveCount();
            int idleCount = getPoolSize() - activeCount;
            long lastPeroidProcTotalTaskNum = executedTaskCount;
            long curProcTotalTaskNum = getCompletedTaskCount();
            long curProcTaskNum = curProcTotalTaskNum - lastPeroidProcTotalTaskNum;
            long toBeExecuteTaskNum = getTaskCount() - curProcTotalTaskNum;
            executedTaskCount = curProcTotalTaskNum;
            StringBuilder sb = new StringBuilder();
            sb.append(threadPoolName);
            sb.append(",");
            sb.append(maxPoolSize);
            sb.append(",");
            sb.append(corePoolSize);
            sb.append(",");
            sb.append(activeCount);
            sb.append(",");
            sb.append(idleCount);
            sb.append(",");
            sb.append(curProcTaskNum);
            sb.append(",");
            sb.append(toBeExecuteTaskNum);
            sb.append(",");
            sb.append(getQueue().size());
            staLogger.info(sb.toString());
        }
    }
}
