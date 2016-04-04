package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;
import static ru.spbau.mit.Predicate.ALWAYS_FALSE;
import static ru.spbau.mit.Predicate.ALWAYS_TRUE;

/**
 * @author antonpp
 * @since 23/03/16
 */
public class PredicateTest {

    @Test
    public void testOr() {
        assertTrue(ALWAYS_TRUE.or(ALWAYS_TRUE).apply(new Object()));
        assertTrue(ALWAYS_TRUE.or(ALWAYS_FALSE).apply(new Object()));
        assertTrue(ALWAYS_FALSE.or(ALWAYS_TRUE).apply(new Object()));
        assertFalse(ALWAYS_FALSE.or(ALWAYS_FALSE).apply(new Object()));

        assertTrue(ALWAYS_TRUE.or(new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                fail("Lazy OR failed");
                return true;
            }
        }).apply(new Object()));
    }

    @Test
    public void testAnd() {
        assertTrue(ALWAYS_TRUE.and(ALWAYS_TRUE).apply(new Object()));
        assertFalse(ALWAYS_TRUE.and(ALWAYS_FALSE).apply(new Object()));
        assertFalse(ALWAYS_FALSE.and(ALWAYS_TRUE).apply(new Object()));
        assertFalse(ALWAYS_FALSE.and(ALWAYS_FALSE).apply(new Object()));

        assertFalse(ALWAYS_FALSE.and(new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                throw new RuntimeException();
            }
        }).apply(new Object()));
    }

    @Test
    public void testNot() {
        assertFalse(ALWAYS_TRUE.not().apply(new Object()));
        assertTrue(ALWAYS_FALSE.not().apply(new Object()));
    }
}
