package cn.z201.junit4;

import cn.z201.junit4.tool.Calculate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculateTest {

	@Test(timeout = 10)
	public void testAdd(){
		assertEquals(6, new Calculate().add(3, 3));
	}
	
	@Test
	public void testSubtract(){
		assertEquals(0, new Calculate().subtract(3, 3));
	}
	
	@Test
	public void testMultipy(){
		assertEquals(9, new Calculate().multipy(3, 3));
	}
	
	@Test
	public void testDivide(){
		assertEquals(1, new Calculate().divide(3, 3));
	}
	
}
