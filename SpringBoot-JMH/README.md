# SpringBoot-JMH 性能基准测试项目

## 1. 项目运行目的

本项目旨在通过JMH（Java Microbenchmark Harness）框架对Java常见操作进行基准性能测试，主要包括以下三个方面：

### 1.1 基础操作性能测试（BasicBenchmarkTest）
- 字符串操作性能对比（String vs StringBuilder）
- 集合操作性能测试（List、Set、Map的基本操作）
- 数据结构遍历性能对比（数组 vs ArrayList）

### 1.2 IO操作性能测试（IOBenchmarkTest）
- 文件读写性能对比（传统IO vs NIO）
- 对象序列化与反序列化性能测试

### 1.3 高级并发操作测试（AdvancedBenchmarkTest）
- 集合排序性能对比（Collections.sort vs parallelStream.sort）
- 流式操作性能测试（Stream操作的各种场景）
- 线程池执行性能测试

## 2. 项目代码运行逻辑

### 2.1 测试数据初始化
1. 使用`@State`注解定义测试状态，确保测试的隔离性
2. 通过`@Param`注解指定不同的测试数据规模（1000、10000、100000）
3. 在`@Setup`方法中初始化测试数据结构（List、Array、Map、Set等）

### 2.2 测试执行流程
1. **预热阶段**
   - 默认进行3次预热迭代（warmupIterations）
   - 预热阶段帮助JVM进行代码优化，使测试结果更准确

2. **测试阶段**
   - 执行5次测试迭代（measurementIterations）
   - 每个测试方法使用`@Benchmark`注解标记
   - 通过`@BenchmarkMode`指定测试模式（平均时间）
   - 使用`@OutputTimeUnit`指定输出时间单位（微秒）

3. **测试隔离**
   - 使用`@Fork(1)`创建独立的JVM进程执行测试
   - 避免其他测试用例对当前测试的影响

## 3. 项目运行结果评估方法

### 3.1 性能指标解读

1. **平均执行时间（AverageTime）**
   - 数值越小表示性能越好
   - 单位为微秒（MICROSECONDS）
   - 需要考虑数据规模对执行时间的影响

2. **吞吐量评估**
   - 对于集合操作，关注不同数据结构的性能差异
   - 比较不同实现方式的时间复杂度（如List.contains O(n) vs Set.contains O(1)）

### 3.2 测试结果分析方法

1. **基础操作性能对比**
   - StringBuilder vs String拼接：预期StringBuilder性能显著优于直接String拼接
   - 集合操作：HashSet.contains应优于ArrayList.contains
   - 数组遍历：原生数组遍历应略优于ArrayList遍历

2. **IO操作性能评估**
   - NIO操作通常应优于传统IO
   - 对象序列化性能受对象大小影响

3. **并发操作性能分析**
   - 小数据量时，普通排序可能优于并行排序
   - 大数据量时，并行流操作通常更具优势

### 3.3 性能优化建议

1. **字符串操作**
   - 在循环中进行字符串拼接时，使用StringBuilder
   - 预先估计StringBuilder容量可进一步提升性能

2. **集合操作**
   - 根据实际场景选择合适的集合类型
   - 需要频繁查找时优先使用HashSet或HashMap

3. **IO操作**
   - 对于大文件操作，优先考虑NIO
   - 使用缓冲流提升IO性能

4. **并发处理**
   - 根据数据规模和CPU核心数选择是否使用并行流
   - 合理配置线程池参数

## 使用说明

1. 运行测试：
   ```bash
   mvn clean test
   ```

2. 查看测试结果：
   - 测试完成后会输出详细的性能测试报告
   - 关注Average Time指标进行性能对比
   - 结合不同数据规模的测试结果进行综合分析

## 注意事项

1. 测试环境应保持稳定，避免其他程序干扰
2. 建议在服务器级硬件上进行正式的性能测试
3. 测试结果会受到JVM参数配置的影响
4. 建议多次运行测试以获得更可靠的结果