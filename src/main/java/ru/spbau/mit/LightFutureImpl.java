package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author antonpp
 * @since 03/05/16
 */
public final class LightFutureImpl<R> implements LightFuture<R>, Runnable {

    private final Object completionMonitor = new Object();
    private final Runnable task;
    private boolean isStarted = false;
    private volatile boolean isReady = false;
    private volatile Throwable exception;
    private volatile  R result;

    public LightFutureImpl(Supplier<R> function, boolean startInstantly) {
        task = () -> {
            try {
                result = function.get();
            } catch (Exception e) {
                exception = e;
            }
            onTaskCompleted();
        };
        if (startInstantly) {
            run();
        }
    }

    LightFutureImpl(Supplier<R> function) {
        this(function, false);
    }

    private <U> LightFutureImpl(LightFutureImpl<U> lightFuture, Function<? super U, ? extends R> function,
                                boolean startInstantly) {
        task = () -> {
            try {
                result = function.apply(lightFuture.get());
            } catch (LightExecutionException e) {
                exception = e.getThrowable();
            } catch (Exception e) {
                exception = e;
            }
            onTaskCompleted();
        };
        if (startInstantly) {
            lightFuture.run();
            run();
        }
    }

    private void onTaskCompleted() {
        synchronized (completionMonitor) {
            isReady = true;
            completionMonitor.notify();
        }
    }


    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public R get() throws LightExecutionException, InterruptedException {
        synchronized (completionMonitor) {
            while (!isReady) {
                completionMonitor.wait();
            }
        }
        if (exception == null) {
            return result;
        }
        throw new LightExecutionException(exception);
    }

    @Override
    public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> f) {
        return new LightFutureImpl<>(this, f, true);
    }

    @Override
    public void run() {
        if (!isStarted) {
            isStarted = true;
            task.run();
        }
    }
}
