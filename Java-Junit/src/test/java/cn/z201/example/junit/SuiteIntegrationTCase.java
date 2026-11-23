package cn.z201.example.junit;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * 测试套件
 *
 * @author z201 测试套件类,必须保证是一个空类，并且是有public 修饰的类 测试套件可以执行其他测试套件
 */
@Suite
@SelectClasses({ AssertUnitTest.class, ExampleUnitTest.class, CalculateUnitTest.class, })
public class SuiteIntegrationTCase {

}
