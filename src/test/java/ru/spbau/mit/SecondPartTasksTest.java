package ru.spbau.mit;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SecondPartTasksTest {

    private static final double EPS = 1e-3;

    @Test
    public void testFindQuotes() {
        final List<String> paths = Arrays.asList(
                "src/test/resources/a.txt",
                "src/test/resources/b.txt",
                "src/test/resources/c.txt"
        );
        final List<String> expected = Arrays.asList(
                "import java.util.*;",
                "import java.util.function.Function;",
                "import java.util.stream.Collectors;",
                "import java.util.stream.IntStream;",
                "import java.util.stream.Stream;",
                "import com.google.common.collect.ImmutableMap;",
                "import org.junit.Test;",
                "import java.util.Arrays;",
                "import java.util.Collections;",
                "import java.util.Optional;",
                "import java.util.stream.Collectors;",
                "import java.util.stream.IntStream;",
                "import java.util.stream.Stream;",
                "import static junitx.framework.Assert.assertEquals;",
                "import static ru.spbau.mit.FirstPartTasks.*;"
        );
        Assert.assertEquals(expected, SecondPartTasks.findQuotes(paths, "import"));
    }

    @Test
    public void testPiDividedBy4() {
        Assert.assertEquals(Math.PI / 4, SecondPartTasks.piDividedBy4(), EPS);
    }

    @Test
    public void testFindPrinter() {
        final Map<String, List<String>> test = ImmutableMap.of(
                "1", Arrays.asList("1", "22", "333"),
                "2", Arrays.asList("22", "4444"),
                "3", Arrays.asList("55555")
        );
        Assert.assertEquals("1", SecondPartTasks.findPrinter(test));
    }

    @Test
    public void testCalculateGlobalOrder() {
        final List<Map<String, Integer>> test = Arrays.asList(ImmutableMap.of(
                        "1", 1,
                        "2", 2,
                        "3", 3
                ), ImmutableMap.of(
                        "1", 4,
                        "2", 5,
                        "3", 6
                )
        );
        final ImmutableMap<String, Integer> expected = ImmutableMap.of(
                "1", 5,
                "2", 7,
                "3", 9
        );
        Assert.assertEquals(expected, SecondPartTasks.calculateGlobalOrder(test));
    }
}
