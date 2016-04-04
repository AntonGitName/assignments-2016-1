package ru.spbau.mit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author antonpp
 * @since 23/03/16
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:avoidinlineconditionals"})
public class CollectionsTest {

    private static final Predicate<Integer> IS_PRIME = new Predicate<Integer>() {
        @Override
        public Boolean apply(Integer arg) {
            for (int i = 2; i * i <= arg; ++i) {
                if (arg % i == 0) {
                    return false;
                }
            }
            return arg != 1;
        }
    };
    private static final Predicate<Boolean> ID = new Predicate<Boolean>() {
        @Override
        public Boolean apply(Boolean arg) {
            return arg;
        }
    };
    private static final Predicate<String> IS_EMPTY = new Predicate<String>() {
        @Override
        public Boolean apply(String arg) {
            return arg == null || arg.isEmpty();
        }
    };
    private List<Integer> listInts;
    private List<String> listStrs;
    private List<Boolean> listBools;

    @Before
    public void setUp() {
        listInts = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        listStrs = Arrays.asList(null, "1", "2", "", null, "3", "");
        listBools = Arrays.asList(true, false, false, true, true);
    }

    @After
    public void tearDown() {
        listInts = null;
        listStrs = null;
        listBools = null;
    }

    @Test
    public void testFilter() {
        assertEquals(Arrays.asList(2, 3, 5, 7), Collections.filter(IS_PRIME, listInts));
        assertEquals(Arrays.asList("1", "2", "3"), Collections.filter(IS_EMPTY.not(), listStrs));
        assertEquals(Arrays.asList(true, true, true), Collections.filter(ID, listBools));
    }


    @Test
    public void testMap() {
        assertEquals(Arrays.asList(1, 0, 0, 1, 1), Collections.map(new Function1<Boolean, Integer>() {
            @Override
            public Integer apply(Boolean arg) {
                return arg ? 1 : 0;
            }
        }, listBools));

        assertEquals(Arrays.asList(1, 2, 3), Collections.map(new Function1<String, Integer>() {
            @Override
            public Integer apply(String arg) {
                return Integer.parseInt(arg);
            }
        }, Collections.filter(IS_EMPTY.not(), listStrs)));

        final List<Integer> squares = new ArrayList<>(listInts.size());
        for (Integer x : listInts) {
            squares.add(x * x);
        }
        assertEquals(squares, Collections.map(new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer arg) {
                return arg * arg;
            }
        }, listInts));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(Arrays.asList(1, 2, 3), Collections.takeWhile(new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer arg) {
                return arg <= 3;
            }
        }, listInts));

        assertEquals(Arrays.asList(true), Collections.takeWhile(ID, listBools));
        assertEquals(null, Collections.takeWhile(IS_EMPTY, listStrs).iterator().next());
    }

    @Test
    public void testFoldr() {
        int sum = 0;
        for (Integer x : listInts) {
            sum += x;
        }
        assertTrue(sum == Collections.foldr(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }, 0, listInts));

        assertEquals("321", Collections.foldr(new Function2<String, StringBuffer, StringBuffer>() {
            @Override
            public StringBuffer apply(String s, StringBuffer stringBuffer) {
                return stringBuffer.append(IS_EMPTY.not().apply(s) ? s : "");
            }
        }, new StringBuffer(), listStrs).toString());

        final boolean any = Collections.foldl(new Function2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean, Boolean aBoolean2) {
                return aBoolean || aBoolean2;
            }
        }, false, listBools);

        assertTrue(any);
    }

    @Test
    public void testFoldl() {
        int sum = 0;
        for (Integer x : listInts) {
            sum += x;
        }
        assertTrue(sum == Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }, 0, listInts));

        int diff = ((((((((((0 - 1) - 2) - 3) - 4) - 5) - 6) - 7) - 8) - 9) - 10);
        assertTrue(diff == Collections.foldl(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer - integer2;
            }
        }, 0, listInts));
    }

    @Test
    public void testGenericRestrictions() {
        final List<Integer> a = Arrays.asList(1, 2, 3, 4, 5, 6);
        final Function1<Object, String> mapper = new Function1<Object, String>() {
            @Override
            public String apply(Object arg) {
                return Integer.toString(arg.hashCode());
            }
        };
        Iterable<Object> b = Collections.<Object, Object>map(mapper, a);
        assertNotNull(b);
    }
}
