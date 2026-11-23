package cn.z201.example.junit;

import cn.z201.example.junit.tool.Calculate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateUnitTest {

    @Test
    public void testAdd() {
        assertEquals(6, new Calculate().add(3, 3));
    }

    @Test
    public void testSubtract() {
        assertEquals(0, new Calculate().subtract(3, 3));
    }

    @Test
    public void testMultipy() {
        assertEquals(9, new Calculate().multipy(3, 3));
    }

    @Test
    public void testDivide() {
        assertEquals(1, new Calculate().divide(3, 3));
    }

}
