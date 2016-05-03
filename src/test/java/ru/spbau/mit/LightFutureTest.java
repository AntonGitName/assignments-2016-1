package ru.spbau.mit;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LightFutureTest {

    @Test
    public void testThenApply() throws Exception {
        final Supplier<Integer> generate = () -> 316;
        final Function<Object, String> first = Object::toString;
        final Function<Integer, String> second = x -> Integer.toString(x + 1);
        final LightFuture<Integer> a = new LightFutureImpl<>(generate);
        final LightFuture<String> b = a.thenApply(first);
        final LightFuture<String> c = a.thenApply(second);
        assertEquals("316", b.get());
        assertEquals("317", c.get());
    }
}