package cn.z201.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class SnowflakeDistributeIdTest {

    private final static Integer MEASUREMENT_ITERATIONS = 1;
    private final static Integer WARMUP_ITERATIONS = 10;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 设置类名正则表达式基准为搜索当前类
                .include("\\." + SnowflakeDistributeIdTest.class.getSimpleName() + "\\.")
                .warmupIterations(WARMUP_ITERATIONS) // 多少次预热
                .measurementIterations(MEASUREMENT_ITERATIONS) // 要做多少次测量m
                .forks(1)
                // 不使用多线程
                .threads(1)
                .mode(Mode.AverageTime)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
//                .result("/dev/null") // set this to a valid filename if you want reports
                .result("benchmark.log")
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void testSnowflakeDistributeId() {
        SnowflakeDistributeId idWorker = new SnowflakeDistributeId(0, 0);
        for (int i = 0; i < 1000000; i++) {
            long id = idWorker.nextId();
        }
    }
}