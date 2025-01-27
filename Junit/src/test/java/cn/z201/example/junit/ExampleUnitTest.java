package cn.z201.example.junit;


import cn.z201.example.junit.tool.Calculate;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleUnitTest {
	
	@Test
	public void testSimpleExample() {
		System.out.println("this is testSimpleExample");
	}

	/**
	 * JUnit 5 引入了新的注解，替代了 JUnit 4 的部分注解：
	 * @BeforeEach 代替 @Before
	 * @AfterEach 代替 @After
	 * @BeforeAll 代替 @BeforeClass
	 * @AfterAll 代替 @AfterClass
	 * @throws Exception
	 */

	/*
	 * @BeforeAll 修饰的方法会在所有方法被被调用钱执行,该方法必须是静态的。
	 * 在测试类加载就会运行，而且在内存中只会存在一个实例。它比较适合加载配置文件。
	 */
	@BeforeAll
	public static void setUpBeforeClass() throws Exception{
		System.out.println("this is @BeforeClass");
	}
	
	/*
	 * @AfterAll 所修饰的方法通常用来对资源的清理，修饰方法必须是静态的。
	 */
	@AfterAll
	public static void tearDownAfterClass()  throws Exception{
		System.out.println("this is @AfterClass");
	}
	
	/*
	 * @BeforeEach 修饰的方法会在每个被@Test修饰的方法调用前执行。
	 */
	@BeforeEach
	public void setUp(){
		System.out.println("this is @setUp");
	}
	
	/*
	 * @AfterEach 修饰的方法会在每个被@Test修饰的方法调用后执行。
	 */
	@AfterEach
	public void tearDown(){
		System.out.println("this is @tearDown");
	}


	@Test
	public void testSimple() {
		String str_1 = "this is testSimple";
		assertEquals("this is testSimple", str_1); //检查两个变量或者等式是否平衡
		int int_1 = 2;
        assertNotEquals(int_1, new Calculate().add(2, 1)); //检查条件是假的
		int int_2 = 3;
        assertEquals(int_2, new Calculate().add(2, 1)); //检查条件是真的

		assertNotNull(int_2); //检查对象不是空的

		assertNull(null); //检查对象是空的

		assertEquals(2, 2); //判断2 是不是 2

		String str3 = null;
		String str4 = "abc";
		String str5 = "abc";
		String[] expectedArray = {"one", "two", "three"};
		String[] resultArray =  {"one", "two", "three"};

		//检查两个对象引用是否指向同一个对象
		assertSame(str4,str5);

		//检查两个对象引用是否不指向同一个对象
		assertNotSame(str3,str4);

		//检查两个数组是否相等。
		assertArrayEquals(expectedArray, resultArray);
	}
	
}
