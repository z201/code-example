package cn.z201.example.spring.jmh;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@Slf4j
@State(Scope.Benchmark)
public class AppApplicationTest {

    private final static Integer MEASUREMENT_ITERATIONS = 1;

    private final static Integer WARMUP_ITERATIONS = 1;

    static ConfigurableApplicationContext context;

    public String[] activeProfiles;

    @Setup(Level.Trial)
    public synchronized void initialize() {
        try {
            String args = "";
            if (context == null) {
                context = SpringApplication.run(AppApplication.class, args);
                activeProfiles = context.getEnvironment().getActiveProfiles();
            }
            log.info("{}", activeProfiles);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Disabled
    public void executeJmhRunner() throws RunnerException {
        Options opt = new OptionsBuilder()
                // 设置类名正则表达式基准为搜索当前类
                .include("\\." + AppApplicationTest.class.getSimpleName() + "\\.")
                // 都是一些基本的参数，可以根据具体情况调整。一般比较重的东西可以进行大量的测试，放到服务器上运行。
                .warmupIterations(WARMUP_ITERATIONS) // 多少次预热
                .measurementIterations(MEASUREMENT_ITERATIONS) // 要做多少次测量m
                .timeUnit(TimeUnit.MILLISECONDS) // 毫秒
                // 不使用多线程
                .forks(0) // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
                .threads(1) // 每个进程中的测试线程，这个非常好理解，根据具体情况选择，一般为cpu乘以2。
                .mode(Mode.AverageTime).shouldDoGC(true).shouldFailOnError(true).resultFormat(ResultFormatType.JSON) // 输出格式化
                // .result("/dev/null") // set this to a valid filename if you want
                // reports
                .result("benchmark.json").shouldFailOnError(true).jvmArgs("-server").build();
        new Runner(opt).run();
    }

    @Benchmark
    public void environment() {

    }

}