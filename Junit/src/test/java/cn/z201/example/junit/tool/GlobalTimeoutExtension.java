package cn.z201.example.junit.tool;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

// 自定义全局超时扩展类
public class GlobalTimeoutExtension implements BeforeAllCallback, AfterAllCallback {

    private ScheduledExecutorService executor;

    private final AtomicBoolean timedOut = new AtomicBoolean(false);

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // 获取全局超时时间，这里设置为 5 秒
        long timeout = 5;
        executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            timedOut.set(true);
            // 超时后可以在这里添加一些额外的处理逻辑，例如打印日志
            System.err.println("Test suite timed out after " + timeout + " seconds.");
        }, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        executor.shutdownNow();
        if (timedOut.get()) {
            throw new RuntimeException("Test suite timed out.");
        }
    }

}
