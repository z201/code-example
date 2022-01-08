package cn.z201.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 测试套件
 *
 * @author z201
 * 测试套件类,必须保证是一个空类，并且是有public 修饰的类
 * 测试套件可以执行其他测试套件
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({AssertTests.class,
        ExampleTest.class,
        CalculateTest.class,
        AssertTests.class})
public class SuiteTest {

}
