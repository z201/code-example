package cn.z201.example.spring.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.*;

/**
 * JMH基准测试示例
 * @author z201.coding@gmail.com
 */
@State(Scope.Thread)
public class BenchmarkTest {

    @Param({"100", "1000", "10000"})
    private int size;

    private List<String> arrayList;
    private List<String> linkedList;
    private StringBuilder stringBuilder;
    private StringBuffer stringBuffer;
    private ConcurrentHashMap<String, String> concurrentHashMap;
    private HashMap<String, String> hashMap;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        stringBuilder = new StringBuilder();
        stringBuffer = new StringBuffer();
        concurrentHashMap = new ConcurrentHashMap<>();
        hashMap = new HashMap<>();

        for (int i = 0; i < size; i++) {
            String value = String.valueOf(i);
            arrayList.add(value);
            linkedList.add(value);
            concurrentHashMap.put(value, value);
            hashMap.put(value, value);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testArrayListIteration() {
        for (String s : arrayList) {
            // 模拟遍历操作
            s.length();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testLinkedListIteration() {
        for (String s : linkedList) {
            // 模拟遍历操作
            s.length();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testStringBuilderAppend() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testStringBufferAppend() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testConcurrentHashMapPut() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < size; i++) {
            String value = String.valueOf(i);
            map.put(value, value);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testHashMapPut() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String value = String.valueOf(i);
            map.put(value, value);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}