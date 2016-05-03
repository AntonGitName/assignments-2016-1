package ru.spbau.mit;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class ThreadPoolImpl implements ThreadPool {

    private volatile boolean acceptingNewTasks = true;
    private final Object taskQueueMonitor = new Object();
    private final Queue<LightFutureImpl<?>> tasks = new LinkedList<>();
    private volatile int taskCount = 0;

    public ThreadPoolImpl(int n) {
        IntStream.range(0, n).forEach((ignored) -> new Thread(new ThreadTask()).start());
    }

    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        if (acceptingNewTasks) {
            final LightFutureImpl<R> submittedTask = new LightFutureImpl<>(supplier);
            synchronized (taskQueueMonitor) {
                tasks.add(submittedTask);
                ++taskCount;
                taskQueueMonitor.notify();
            }
            return submittedTask;
        }
        return null;
    }

    @Override
    public void shutdown() {
        acceptingNewTasks = false;
        synchronized (taskQueueMonitor) {
            taskQueueMonitor.notifyAll();
        }
    }

    private LightFutureImpl<?> waitForTask() throws InterruptedException {
        synchronized (taskQueueMonitor) {
            while (acceptingNewTasks && taskCount == 0) {
                taskQueueMonitor.wait();
            }
            if (taskCount == 0) {
                return null;
            }
            --taskCount;
            return tasks.poll();
        }
    }

    private class ThreadTask implements Runnable {

        @Override
        public void run() {
            while (acceptingNewTasks || taskCount != 0) {
                try {
                    final LightFutureImpl<?> task = waitForTask();
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    // ignoring interrupted task
                }
            }
        }
    }

    public static void main(String[] args) {
        new ThreadPoolImpl(5).shutdown();
    }
}
