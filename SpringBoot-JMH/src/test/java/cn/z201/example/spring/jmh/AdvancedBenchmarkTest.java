package cn.z201.example.spring.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * JMH高级基准测试示例
 * @author z201.coding@gmail.com
 */
@State(Scope.Thread)
public class AdvancedBenchmarkTest {

    @Param({"1000", "10000", "100000"})
    private int size;

    private List<Integer> numbers;
    private ExecutorService executorService;
    private CountDownLatch latch;

    @Setup
    public void setup() {
        numbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            numbers.add(random.nextInt());
        }
        executorService = Executors.newFixedThreadPool(4);
    }

    @TearDown
    public void tearDown() {
        executorService.shutdown();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Integer> testCollectionSort() {
        List<Integer> sorted = new ArrayList<>(numbers);
        Collections.sort(sorted);
        return sorted;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Integer> testParallelStreamSort() {
        return numbers.parallelStream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Map<Boolean, List<Integer>> testStreamGrouping() {
        return numbers.stream()
                .collect(Collectors.groupingBy(n -> n % 2 == 0));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testThreadPoolExecution() throws InterruptedException {
        latch = new CountDownLatch(size);
        for (Integer number : numbers) {
            executorService.submit(() -> {
                // 模拟耗时操作
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                latch.countDown();
            });
        }
        latch.await();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<Integer> testStreamFilter() {
        return numbers.stream()
                .filter(n -> n > 0)
                .collect(Collectors.toList());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Double testStreamReduce() {
        return numbers.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AdvancedBenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}