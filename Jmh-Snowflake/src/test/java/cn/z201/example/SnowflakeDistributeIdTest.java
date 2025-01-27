package cn.z201.example;

import cn.z201.example.snowflake.SnowflakeDistributeId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class SnowflakeDistributeIdTest {

    private final static Integer MEASUREMENT_ITERATIONS = 1;
    private final static Integer WARMUP_ITERATIONS = 1;

    private SnowflakeDistributeId snowflakeDistributeId;

    @Setup(Level.Trial)
    public synchronized void initialize() {
        if (null == snowflakeDistributeId) {
            snowflakeDistributeId = new SnowflakeDistributeId(0, 0);
        }
    }

    @Test
    @Disabled
    public void executeJmhRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                // 设置类名正则表达式基准为搜索当前类
                .include("\\." + SnowflakeDistributeIdTest.class.getSimpleName() + "\\.")
                // 都是一些基本的参数，可以根据具体情况调整。一般比较重的东西可以进行大量的测试，放到服务器上运行。
                .warmupIterations(WARMUP_ITERATIONS) // 多少次预热
                .measurementIterations(MEASUREMENT_ITERATIONS) // 要做多少次测量m
                .timeUnit(TimeUnit.MILLISECONDS) // 毫秒
                // 不使用多线程
                .forks(1) // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
                .threads(1) // 每个进程中的测试线程，这个非常好理解，根据具体情况选择，一般为cpu乘以2。
                // Throughput 整体吞吐量，例如”1秒内可以执行多少次调用”。
                // AverageTime: 调用的平均时间，例如”每次调用平均耗时xxx毫秒”。
                // SampleTime: 随机取样，最后输出取样结果的分布，例如”99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
                // SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。
                // All(“all”, “All benchmark modes”);
                .mode(Mode.All)
                .shouldDoGC(true)
                .shouldFailOnError(true)             //
                .resultFormat(ResultFormatType.JSON) // 输出格式化
//                .result("/dev/null") // set this to a valid filename if you want reports
                .result("benchmark.json")
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();
        new Runner(opt).run();
    }

    /**
     * Benchmark 方法级注解，表示该方法是需要进行 benchmark 的对象，用法和 JUnit 的 @Test 类似。
     */
    @Benchmark
    public void testSnowflakeDistributeId() {
        long id = snowflakeDistributeId.nextId();
    }

}