package cn.z201.example.spring.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JMH IO操作基准测试示例
 * @author z201.coding@gmail.com
 */
@State(Scope.Thread)
public class IOBenchmarkTest {

    @Param({"1000", "10000", "100000"})
    private int size;

    private List<String> dataList;
    private File tempFile;
    private Path tempPath;
    private TestObject testObject;

    @Setup
    public void setup() throws IOException {
        dataList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            dataList.add("test-data-" + i);
        }
        tempFile = File.createTempFile("benchmark", ".txt");
        tempPath = tempFile.toPath();
        testObject = new TestObject("test", size);
    }

    @TearDown
    public void tearDown() {
        tempFile.delete();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testFileWrite() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            for (String data : dataList) {
                writer.write(data);
                writer.newLine();
            }
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<String> testFileRead() throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testNioWrite() throws IOException {
        Files.write(tempPath, dataList, StandardCharsets.UTF_8);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public List<String> testNioRead() throws IOException {
        return Files.readAllLines(tempPath, StandardCharsets.UTF_8);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public byte[] testObjectSerialization() throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(testObject);
            return baos.toByteArray();
        }
    }

    @State(Scope.Thread)
    public static class DeserializationState {
        public byte[] data;

        @Setup
        public void setup(IOBenchmarkTest test) throws IOException {
            data = test.testObjectSerialization();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public TestObject testObjectDeserialization(DeserializationState state) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(state.data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (TestObject) ois.readObject();
        }
    }

    public static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IOBenchmarkTest.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .build();
        new Runner(opt).run();
    }
}