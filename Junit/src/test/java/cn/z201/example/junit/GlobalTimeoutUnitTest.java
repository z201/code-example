package cn.z201.example.junit;

import cn.z201.example.junit.tool.GlobalTimeoutExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

// 使用自定义扩展类
@ExtendWith(GlobalTimeoutExtension.class)
public class GlobalTimeoutUnitTest {
    
    public static String log;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Test
    @Timeout(3) // 设置3秒超时
    public void testSleepForTooLong() throws Exception {
        log += "ran1";
        TimeUnit.SECONDS.sleep(2); // 将睡眠时间改为2秒
    }

    @Test
    @Timeout(3) // 设置3秒超时
    public void testBlockForever() throws Exception {
        log += "ran2";
        // 设置等待超时时间为2秒
        latch.await(2, TimeUnit.SECONDS);
    }
}
