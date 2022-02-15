package cn.z201.audit.config.mdc;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.TimeUnit;

/**
 * 这是{@link ThreadPoolTaskExecutor}简单的改良，可以在每个任务之前设置子线程的MDC数据。
 * <p/>
 * 在记录日志的时候，通常会使用MDC来存储每个线程的特有参数，以便更好的查询日志。
 * 但是Logback在最新的版本中因为性能问题，不会自动的将MDC的内存传给子线程。所以Logback建议在执行异步线程前
 * 先通过MDC.getCopyOfContextMap()方法将MDC内存获取出来，再传给线程。
 * 并在子线程的执行的最开始调用MDC.setContextMap(context)方法将父线程的MDC内容传给子线程。
 * <p>
 * https://logback.qos.ch/manual/mdc.html
 *
 * @author z201.coding@gamil.com
 */
public class MdcThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /**
     * Pool where task threads take MDC from the submitting thread.
     */
    public static MdcThreadPoolTaskExecutor newWithInheritedMdc(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                                TimeUnit unit, int queueCapacity, RejectedExecutionHandler rejectedExecutionHandler) {
        return new MdcThreadPoolTaskExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queueCapacity, rejectedExecutionHandler);
    }

    private MdcThreadPoolTaskExecutor(int corePoolSize, int maximumPoolSize,
                                      long keepAliveTime, TimeUnit unit, int queueCapacity, RejectedExecutionHandler rejectedExecutionHandler) {
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maximumPoolSize);
        setKeepAliveSeconds((int) unit.toSeconds(keepAliveTime));
        setQueueCapacity(queueCapacity);
        setRejectedExecutionHandler(rejectedExecutionHandler);
    }

    private Map<String, String> initMdcContext() {
        // 获取父线程MDC中的内容，必须在run方法之前，否则等异步线程执行的时候有可能MDC里面的值已经被清空了，这个时候就会返回null
        Map<String, String> context = MDC.getCopyOfContextMap();
        if (Objects.isNull(context)) {
            context = new HashMap<>();
        }
        if (CollectionUtils.isEmpty(context)) {
            // 防止线程没有链路信息,这里可以扩展
            context.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, MdcTool.getInstance().getAndCreate());
        }
        return context;
    }

    /**
     * 所有线程都会委托给这个execute方法，在这个方法中我们把父线程的MDC内容赋值给子线程
     * https://logback.qos.ch/manual/mdc.html#managedThreads
     *
     * @param runnable
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        super.execute(() -> run(runnable, initMdcContext()));
    }

    /**
     * 异步提交
     *
     * @param task
     * @return
     */
    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        return super.submit(() -> run(task, initMdcContext()));
    }

    /**
     * 异步提交
     *
     * @param task
     * @return
     */
    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        return super.submit(submit(task, initMdcContext()));
    }

    /**
     * 子线程委托的执行方法s
     *
     * @param runnable {@link Runnable}
     * @param context  父线程MDC内容
     */
    private void run(Runnable runnable, Map<String, String> context) {
        // 将父线程的MDC内容传给子线程
        if (context != null) {
            try {
                MDC.setContextMap(context);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        try {
            // 执行异步操作
            runnable.run();
        } finally {
            // 清空MDC内容
            MDC.clear();
        }
    }

    private static <T> Callable<T> submit(final Callable<T> task, final Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                return task.call();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}