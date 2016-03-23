package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author antonpp
 * @since 23/03/16
 */
public class Function2Test {

    private final Function2<Long, Integer, Integer> f = new Function2<Long, Integer, Integer>() {
        @Override
        public Long apply(Integer integer, Integer integer2) {
            return (long) integer - (long) integer2;
        }
    };
    private final Function1<String, Object> g = new Function1<String, Object>() {
        @Override
        public String apply(Object arg) {
            return arg.toString();
        }
    };

    @Test
    public void testCompose() {
        Assert.assertEquals(Integer.toString(123 - 54), f.compose(g).apply(123, 54));
    }

    @Test
    public void testBind1() {
        Assert.assertEquals(2 - 100L, (long) f.bind1(2).apply(100));
    }

    @Test
    public void testBind2() {
        Assert.assertEquals(100L - 5, (long) f.bind2(5).apply(100));
    }

    @Test
    public void testCurry() {
        Assert.assertEquals(100L - 5, (long) f.curry().apply(100).apply(5));
    }
}
