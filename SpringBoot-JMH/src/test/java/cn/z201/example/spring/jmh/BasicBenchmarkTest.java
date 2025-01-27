package cn.z201.example.spring.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * JMH基础性能基准测试类
 * 
 * 该类演示了Java常见操作的性能测试，包括：
 * 1. 字符串操作性能对比（String vs StringBuilder）
 * 2. 集合操作性能测试（List、Set、Map的基本操作）
 * 3. 数据结构遍历性能对比（数组 vs List）
 * 
 * 测试规模参数：
 * - size: 测试数据规模，分别测试1000、10000、100000三种规模
 * 
 * @author z201.coding@gmail.com
 */
@State(Scope.Thread)
public class BasicBenchmarkTest {

    /**
     * 测试数据规模，通过@Param注解指定多个测试规模
     */
    @Param({"1000", "10000", "100000"})
    private int size;

    // 测试用的数据结构
    private List<String> stringList;
    private String[] stringArray;
    private String testString;
    private Map<String, String> hashMap;
    private Set<String> hashSet;

    /**
     * 测试初始化方法
     * 在基准测试开始前，初始化所需的数据结构
     */
    @Setup
    public void setup() {
        stringList = new ArrayList<>();
        stringArray = new String[size];
        hashMap = new HashMap<>();
        hashSet = new HashSet<>();
        
        // 填充测试数据
        for (int i = 0; i < size; i++) {
            String value = String.valueOf(i);
            stringList.add(value);
            stringArray[i] = value;
            hashMap.put(value, value);
            hashSet.add(value);
        }
        
        testString = "test";
    }

    /**
     * 测试字符串拼接性能 - 使用String直接拼接
     * 这种方式每次拼接都会创建新的String对象，性能较差
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public String testStringConcat() {
        String result = "";
        for (int i = 0; i < 100; i++) {
            result += testString;
        }
        return result;
    }

    /**
     * 测试字符串拼接性能 - 使用StringBuilder
     * StringBuilder是可变的，不会创建临时对象，性能更好
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public String testStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(testString);
        }
        return sb.toString();
    }

    /**
     * 测试List的contains操作性能
     * ArrayList的contains方法需要遍历整个列表，时间复杂度O(n)
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public boolean testListContains() {
        return stringList.contains(String.valueOf(size - 1));
    }

    /**
     * 测试Set的contains操作性能
     * HashSet的contains方法使用哈希表实现，时间复杂度O(1)
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public boolean testSetContains() {
        return hashSet.contains(String.valueOf(size - 1));
    }

    /**
     * 测试Map的get操作性能
     * HashMap的get操作使用哈希表实现，时间复杂度O(1)
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public String testMapGet() {
        return hashMap.get(String.valueOf(size - 1));
    }

    /**
     * 测试数组遍历性能
     * 数组的内存布局连续，具有更好的缓存局部性
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public int testArrayIteration() {
        int sum = 0;
        for (String s : stringArray) {
            sum += s.length();
        }
        return sum;
    }

    /**
     * 测试ArrayList遍历性能
     * ArrayList虽然底层也是数组，但有额外的对象开销
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public int testListIteration() {
        int sum = 0;
        for (String s : stringList) {
            sum += s.length();
        }
        return sum;
    }

    /**
     * 主方法，用于运行基准测试
     * 配置测试参数：
     * - forks: 进行fork的次数，用于避免JVM优化对测试的影响
     * - warmupIterations: 预热迭代次数
     * - measurementIterations: 实际测量迭代次数
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BasicBenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}