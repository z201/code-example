package cn.z201.example.junit;

import cn.z201.example.junit.tool.Calculate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 参数化测试
 * @author z201
 */
public class ParameterUnitTest {

    @ParameterizedTest
    @CsvSource({
        "3, 1, 2",
        "5, 2, 3",
        "10, 5, 5"
    })
    void testAdd(int expected, int input1, int input2) {
        assertEquals(expected, new Calculate().add(input1, input2),
            () -> input1 + " + " + input2 + " should equal " + expected);
    }
}
