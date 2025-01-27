package cn.z201.example.jmh;

import lombok.Getter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author z201.coding@gmail.com
 **/
@State(Scope.Benchmark)
public class Main {

    private final static Integer MEASUREMENT_ITERATIONS = 1;
    private final static Integer WARMUP_ITERATIONS = 1;

    @Param(value = {"100", "1000", "10000"})
    private int size;

    public List<Integer> numberList = null;

    @Setup(Level.Trial)
    public synchronized void initialize() {
        numberList = new Random()
                .ints()
                .limit(size)
                .boxed()
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // 设置类名正则表达式基准为搜索当前类
                .include("\\." + Main.class.getSimpleName() + "\\.")
                // 都是一些基本的参数，可以根据具体情况调整。一般比较重的东西可以进行大量的测试，放到服务器上运行。
                .warmupIterations(WARMUP_ITERATIONS) // 多少次预热
                .measurementIterations(MEASUREMENT_ITERATIONS) // 要做多少次测量m
                .timeUnit(TimeUnit.MILLISECONDS) // 毫秒
                // 不使用多线程
                .forks(0) // 进行 fork 的次数。如果 fork 数是2的话，则 JMH 会 fork 出两个进程来进行测试。
                .threads(8) // 每个进程中的测试线程，这个非常好理解，根据具体情况选择，一般为cpu乘以2。
                .mode(Mode.AverageTime)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON) // 输出格式化
//                .result("/dev/null") // set this to a valid filename if you want reports
                .result("Main.json")
                .shouldFailOnError(true)
                .jvmArgs("-server")
                .build();
        new Runner(opt).run();
    }

    @Getter
    public static class VolatileLong {
        private volatile long value = 0;

        public synchronized void add(long amount) {
            this.value += amount;
        }

    }

    @Benchmark
    public void atomicLong(Blackhole blackhole) {
        AtomicLong atomicLong = new AtomicLong();
        numberList.parallelStream().forEach(atomicLong::addAndGet);
        blackhole.consume(atomicLong.get());
    }

    @Benchmark
    public void volatileLong(Blackhole blackHole) {
        VolatileLong volatileLong = new VolatileLong();
        numberList.parallelStream().forEach(volatileLong::add);
        blackHole.consume(volatileLong.getValue());
    }

    @Benchmark
    public void longStreamSum(Blackhole blackHole) {
        long sum = numberList.parallelStream().mapToLong(s -> s).sum();
        blackHole.consume(sum);
    }

}
