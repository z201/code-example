package cn.z201.mdc.log;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author z201.coding@gmail.com
 **/
public class MdcThreadTaskUtils {

    private static MdcThreadPoolTaskExecutor taskExecutor = null;

    /**
     * demo
     *         ThreadTaskUtils.run(() -> run());
     *         FutureTask<String> futureTask = new FutureTask<String>(() -> call());
     *         ThreadTaskUtils.run(futureTask);
     */
    static {
        /**
         * 核心线程数 5
         * 最大线程数 50
         * 队列最大长度 1000
         * 线程池维护线程所允许的空闲时间(单位秒) 120
         * 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃. 120
         */
    }
    public static void run(Runnable runnable) {
        if (null == taskExecutor) {
            taskExecutor = MdcThreadPoolTaskExecutor.newWithInheritedMdc(5, 50, 1000, TimeUnit.SECONDS, 120, new ThreadPoolExecutor.AbortPolicy());
            taskExecutor.initialize();
        }
        taskExecutor.execute(runnable);
    }
}
