package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author antonpp
 * @since 23/03/16
 */
public class Function1Test {

    @Test
    public void testCompose() {
        final Function1<Object, Integer> hashCode = new Function1<Object, Integer>() {
            @Override
            public Integer apply(Object arg) {
                return arg.hashCode();
            }
        };

        final Function1<Object, String> toString = new Function1<Object, String>() {
            @Override
            public String apply(Object arg) {
                return arg.toString();
            }
        };

        final List<Integer> x = Arrays.asList(1, 2, 3, 4, 5);
        final List<String> y = Arrays.asList("1", "2", "3", "4", "5");
        Assert.assertEquals(y, Collections.map(hashCode.compose(toString), x));
    }

    @Test
    public void testGenericRestrictions() {
        final Function1<Integer, Boolean> isEven = new Function1<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg % 2 == 0;
            }
        };
        final Function1<Boolean, Circle> circleGen = new Function1<Boolean, Circle>() {
            @Override
            public Circle apply(Boolean arg) {
                return new Circle();
            }
        };
        List<Shape> a = new ArrayList<>();
        a.add(isEven.compose(circleGen).apply(0));
        assertNotNull(a);
    }

    private abstract static class Shape {
    }

    private static class Circle extends Shape {
        @Override
        public String toString() {
            return "o";
        }
    }
}
