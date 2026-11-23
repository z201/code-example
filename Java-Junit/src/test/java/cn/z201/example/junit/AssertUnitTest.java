package cn.z201.example.junit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.core.CombinableMatcher;
import java.util.Arrays;

public class AssertUnitTest {

    @Test
    public void testAssertArrayEquals() {
        byte[] expected = "trial".getBytes();
        byte[] actual = "trial".getBytes();
        assertArrayEquals(expected, actual, "failure - byte arrays not same");
    }

    @Test
    public void testAssertEquals() {
        assertEquals("text", "text", "failure - strings are not equal");
    }

    @Test
    public void testAssertFalse() {
        assertFalse(false, "failure - should be false");
    }

    @Test
    public void testAssertNotNull() {
        assertNotNull(new Object(), "should not be null");
    }

    @Test
    public void testAssertNotSame() {
        assertNotSame(new Object(), new Object(), "should not be same Object");
    }

    @Test
    public void testAssertNull() {
        assertNull(null, "should be null");
    }

    @Test
    public void testAssertSame() {
        Integer aNumber = Integer.valueOf(768);
        assertEquals(aNumber, 768, "should be same");
    }

    // JUnit Matchers assertThat
    @Test
    public void testAssertThatBothContainsString() {
        assertThat("albumen", both(containsString("a")).and(containsString("b")));
    }

    @Test
    public void testAssertThatHasItems() {
        assertThat(Arrays.asList("one", "two", "three"), hasItems("one", "three"));
    }

    @Test
    public void testAssertThatEveryItemContainsString() {
        assertThat(Arrays.asList("fun", "ban", "net"), everyItem(containsString("n")));
    }

    // Core Hamcrest Matchers with assertThat
    @Test
    public void testAssertThatHamcrestCoreMatchers() {
        assertThat("good", allOf(equalTo("good"), startsWith("good")));
        assertThat("good", not(allOf(equalTo("bad"), equalTo("good"))));
        assertThat("good", anyOf(equalTo("bad"), equalTo("good")));
        assertThat(7, not(CombinableMatcher.<Integer>either(equalTo(3)).or(equalTo(4))));
        assertThat(new Object(), not(sameInstance(new Object())));
    }

    @Test
    public void testAssertTrue() {
        assertTrue(true, "failure - should be true");
    }

}
