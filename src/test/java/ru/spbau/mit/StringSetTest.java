package ru.spbau.mit;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringSetTest {

    public static StringSet instance() {
        try {
            return (StringSet) Class.forName("ru.spbau.mit.StringSetImpl").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Error while class loading");
    }

    @Test
    public void testSimple() {
        StringSet stringSet = instance();

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.contains("abc"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("abc"));
    }

    @Ignore
    @Test
    public void testDuplicates() {
        final StringSet stringSet = instance();
        assertTrue(stringSet.add("abc"));
        assertFalse(stringSet.add("abc"));
        assertTrue(stringSet.add("abd"));
        assertFalse(stringSet.add("abd"));

        assertEquals(4, stringSet.howManyStartsWithPrefix("ab"));

        assertTrue(stringSet.remove("abc"));
        assertEquals(3, stringSet.howManyStartsWithPrefix("ab"));

        assertFalse(stringSet.remove("ab"));
        assertEquals(3, stringSet.howManyStartsWithPrefix("ab"));
    }

    @Test
    public void testPrefix() {
        final StringSet stringSet = instance();
        assertEquals(0, stringSet.howManyStartsWithPrefix("ab"));
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("abcdefghjiklmnoprst"));
        assertEquals(1, stringSet.howManyStartsWithPrefix("abcde"));
        assertEquals(2, stringSet.howManyStartsWithPrefix("a"));
    }

    @Test
    public void testUpperCase() {
        StringSet stringSet = instance();
        assertTrue(stringSet.add("ABC"));
        assertTrue(stringSet.contains("ABC"));
        assertEquals(1, stringSet.size());
        assertEquals(1, stringSet.howManyStartsWithPrefix("ABC"));
        assertTrue(stringSet.add("ABCD"));
        assertEquals(0, stringSet.howManyStartsWithPrefix("BC"));
        assertEquals(2, stringSet.howManyStartsWithPrefix("A"));
    }

    @Test
    public void testCharToIndex() {
        int index = 0;
        for (char c = 'a'; c <= 'z'; ++c) {
            assertTrue(index++ == StringSetImpl.charToIndex(c));
        }
        for (char c = 'A'; c <= 'Z'; ++c) {
            assertTrue(index++ == StringSetImpl.charToIndex(c));
        }
        assertTrue(index == StringSetImpl.ALPHABET_SIZE);
    }

    @Test
    public void testEmpty() {
        final StringSet stringSet = instance();
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("abcdefghjiklmnoprst"));
        assertTrue(stringSet.add("sdgsadfasdf"));
        assertTrue(stringSet.add("KJNDfsdfjnskdfADF"));
        assertTrue(stringSet.add("asdvjnsKAJBDSSDF"));
        assertTrue(stringSet.add("ABCDEFGHIJKLMNOPRST"));
        assertTrue(stringSet.add("AKSHJDBVSD"));
        assertTrue(stringSet.add("AAAAAAAAAAAAA"));
        assertTrue(stringSet.add("ZZZZZZZZZ"));
        assertTrue(stringSet.add("zzzzzzzzz"));
        assertTrue(stringSet.add("aaaaaaaa"));
        assertTrue(stringSet.add("KSJDfskdfjsdf"));
        assertTrue(stringSet.add("popo"));

        assertTrue(stringSet.remove("abc"));
        assertTrue(stringSet.remove("abcdefghjiklmnoprst"));
        assertTrue(stringSet.remove("sdgsadfasdf"));
        assertTrue(stringSet.remove("KJNDfsdfjnskdfADF"));
        assertTrue(stringSet.remove("asdvjnsKAJBDSSDF"));
        assertTrue(stringSet.remove("ABCDEFGHIJKLMNOPRST"));
        assertTrue(stringSet.remove("AKSHJDBVSD"));
        assertTrue(stringSet.remove("AAAAAAAAAAAAA"));
        assertTrue(stringSet.remove("ZZZZZZZZZ"));
        assertTrue(stringSet.remove("zzzzzzzzz"));
        assertTrue(stringSet.remove("aaaaaaaa"));
        assertTrue(stringSet.remove("KSJDfskdfjsdf"));
        assertTrue(stringSet.remove("popo"));

        assertEquals(0, stringSet.size());

        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("abcdefghjiklmnoprst"));
        assertTrue(stringSet.add("sdgsadfasdf"));
        assertTrue(stringSet.add("KJNDfsdfjnskdfADF"));
        assertTrue(stringSet.add("asdvjnsKAJBDSSDF"));
        assertTrue(stringSet.add("ABCDEFGHIJKLMNOPRST"));
        assertTrue(stringSet.add("AKSHJDBVSD"));
        assertTrue(stringSet.add("AAAAAAAAAAAAA"));
        assertTrue(stringSet.add("ZZZZZZZZZ"));
        assertTrue(stringSet.add("zzzzzzzzz"));
        assertTrue(stringSet.add("aaaaaaaa"));
        assertTrue(stringSet.add("KSJDfskdfjsdf"));
        assertTrue(stringSet.add("popo"));

        assertEquals(13, stringSet.size());
    }

    @Test
    public void testSize() {
        final StringSet stringSet = instance();
        assertEquals(0, stringSet.howManyStartsWithPrefix("ab"));
        assertTrue(stringSet.add("abc"));
        assertTrue(stringSet.add("abcdefghjiklmnoprst"));
        assertTrue(stringSet.add("sdgsadfasdf"));
        assertTrue(stringSet.add("KJNDfsdfjnskdfADF"));
        assertTrue(stringSet.add("asdvjnsKAJBDSSDF"));
        assertTrue(stringSet.add("ABCDEFGHIJKLMNOPRST"));
        assertTrue(stringSet.add("AKSHJDBVSD"));
        assertTrue(stringSet.add("AAAAAAAAAAAAA"));
        assertTrue(stringSet.add("ZZZZZZZZZ"));
        assertTrue(stringSet.add("zzzzzzzzz"));
        assertTrue(stringSet.add("aaaaaaaa"));
        assertTrue(stringSet.add("KSJDfskdfjsdf"));
        assertTrue(stringSet.add("popo"));
        assertEquals(stringSet.size(), stringSet.howManyStartsWithPrefix(""));
        assertEquals(stringSet.size(), 13);
    }
}
