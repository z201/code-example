package cn.z201.junit4;

import cn.z201.junit4.tool.Calculate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * 参数化测试
 * @author z201
 */
@RunWith(Parameterized.class)
public class ParameterTest {
	
	int expected = 0;
	int intput_1 = 0;
	int intput_2 = 0;
	
	@Parameters
	public static Collection<Object[]> collection(){
		return Arrays.asList(new Object[][]{
			{3,1,2},{4,2,2}	
		});
	}

	public ParameterTest(int expected , int intput_1 ,int intput_2){
		this.expected = expected;
		this.intput_1  = intput_1;
		this.intput_2 = intput_2;
	}
	
	/*
	 * 执行的时候会被调用两次
	 */
	@Test
	public void testAdd(){
		assertEquals(expected, new Calculate().add(intput_1, intput_2));
	}
	
}
