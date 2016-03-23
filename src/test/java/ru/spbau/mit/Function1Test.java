package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author antonpp
 * @since 23/03/16
 */
public class Function1Test {

    @Test
    public void testCompose() {
        final Function1<Integer, Object> hashCode = new Function1<Integer, Object>() {
            @Override
            public Integer apply(Object arg) {
                return arg.hashCode();
            }
        };

        final Function1<String, Object> toString = new Function1<String, Object>() {
            @Override
            public String apply(Object arg) {
                return arg.toString();
            }
        };

        final List<Integer> x = Arrays.asList(1, 2, 3, 4, 5);
        final List<String> y = Arrays.asList("1", "2", "3", "4", "5");
        Assert.assertEquals(y, Collections.map(hashCode.compose(toString), x));
    }

}
