package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ThreadPoolTest {

    private static final int THREADS = 8;
    private static final long TEST_LONG_WAIT_TIME = 1000;
    private static final long TEST_SHORT_WAIT_TIME = 100;
    private static final long LONG_SLEEP_TIME = 1000;
    private static final long SHORT_SLEEP_TIME = 100;
    private static final long SHORTEST_SLEEP_TIME = 10;

    private ThreadPool threadPool;

    @Before
    public void setUp() throws Exception {
        threadPool = new ThreadPoolImpl(THREADS);
    }

    @Test(expected = LightExecutionException.class)
    public void testSubmitRuntimeException() throws Exception {
        final LightFuture<?> task = threadPool.submit(new FailedTask(SHORT_SLEEP_TIME));
        task.get();
        threadPool.shutdown();
    }

    @Test
    public void testSubmitMultipleExceptions() throws Exception {
        final List<LightFuture<?>> mustFinish = IntStream.range(0, THREADS).
                mapToObj((unused) -> threadPool.submit(new FailedTask(SHORT_SLEEP_TIME))).
                collect(Collectors.toList());
        threadPool.shutdown();
        for (LightFuture<?> future : mustFinish) {
            try {
                future.get();
                fail("Expected LightExecutionException");
            } catch (LightExecutionException e) {
                // ok
            }
        }
    }

    @Test
    public void testSubmitMultipleInterruptions() throws Exception {
        final List<LightFuture<?>> mustFinish = IntStream.range(0, 2 * THREADS).
                mapToObj((unused) -> threadPool.submit(new InterruptedTask(SHORT_SLEEP_TIME))).
                collect(Collectors.toList());
        for (LightFuture<?> future : mustFinish) {
            assertNull(future.get());
        }
    }

    @Test
    public void testShutdownAllCompleteBeforeShutdown() throws Exception {
        final List<LightFuture<?>> mustFinish = IntStream.range(0, THREADS).
                mapToObj((unused) -> threadPool.submit(new SleepTask(SHORT_SLEEP_TIME))).
                collect(Collectors.toList());
        Thread.sleep(TEST_LONG_WAIT_TIME);
        assertTrue(mustFinish.stream().allMatch(LightFuture::isReady));
        threadPool.shutdown();
        for (int i = 0; i < THREADS; ++i) {
            assertNull(threadPool.submit(new SleepTask(LONG_SLEEP_TIME)));
        }
        for (LightFuture<?> future : mustFinish) {
            assertNull(future.get());
        }
    }

    @Test
    public void testNoTasksLost() throws Exception {
        final int n = 1000;
        final List<LightFuture<Integer>> mustFinish = IntStream.range(0, n).
                mapToObj((number) -> threadPool.submit(new IntTask(SHORTEST_SLEEP_TIME, number))).
                collect(Collectors.toList());
        threadPool.shutdown();
        for (int i = 0; i < n; ++i) {
            assertEquals(i, mustFinish.get(i).get().intValue());
        }
    }

    @Test
    public void testShutdownAllCompleteAfterShutdown() throws Exception {
        final List<LightFuture<?>> mustFinish = IntStream.range(0, THREADS).
                mapToObj((unused) -> threadPool.submit(new SleepTask(LONG_SLEEP_TIME))).
                collect(Collectors.toList());
        Thread.sleep(TEST_SHORT_WAIT_TIME);
        assertFalse(mustFinish.stream().allMatch(LightFuture::isReady));
        threadPool.shutdown();
        for (int i = 0; i < THREADS; ++i) {
            assertNull(threadPool.submit(new SleepTask(LONG_SLEEP_TIME)));
        }
        for (LightFuture<?> future : mustFinish) {
            assertNull(future.get());
        }
    }

    private static class SleepTask implements Supplier<Void> {

        private final long timeToSleep;

        public SleepTask(long timeToSleep) {
            this.timeToSleep = timeToSleep;
        }

        @Override
        public Void get() {
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }

    private static final class InterruptedTask extends SleepTask {

        public InterruptedTask(long timeToSleep) {
            super(timeToSleep);
        }

        @Override
        public Void get() {
            Thread.currentThread().interrupt();
            super.get();
            return null;
        }
    }

    private static final class FailedTask extends SleepTask {

        public FailedTask(long timeToSleep) {
            super(timeToSleep);
        }

        @Override
        public Void get() {
            super.get();
            throw new FailedTaskException();
        }
    }

    private static final class FailedTaskException extends RuntimeException {

    }

    private static final class IntTask implements Supplier<Integer> {

        private final SleepTask sleepTask;
        private final int number;

        public IntTask(long timeToSleep, int number) {
            sleepTask = new SleepTask(timeToSleep);
            this.number = number;
        }

        @Override
        public Integer get() {
            sleepTask.get();
            return number;
        }
    }

}