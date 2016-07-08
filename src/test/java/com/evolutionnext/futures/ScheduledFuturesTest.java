package com.evolutionnext.futures;

import org.junit.Test;

import java.util.concurrent.*;

public class ScheduledFuturesTest {

    /**
     * Demo 5: Scheduled Delay
     */
    @Test
    public void testScheduledFuture()
            throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);

        ScheduledFuture<Integer> schedule = scheduledExecutorService.schedule(
                new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("In Schedule: " + Thread.currentThread().getName());
                return 40 + 50;
            }
        }, 10, TimeUnit.SECONDS);

        System.out.println(schedule.get());
        Thread.sleep(1000);
    }

    /**
     * Demo 6: Scheduled Futures
     */
    @Test
    public void testScheduledFutureWithFixedDelay() throws
            ExecutionException, InterruptedException {

        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(6);

        final ScheduledFuture<?> scheduledFuture =
                scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("In Schedule: " +
                                Thread.currentThread().getName());
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1, 1, TimeUnit.SECONDS);

        Thread.sleep(6000);
        scheduledFuture.cancel(false);
        Thread.sleep(2000);
    }

    @Test
    public void testScheduledFutureWithFixedRate() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(3);

        final ScheduledFuture<?> scheduledFuture =
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    System.out.println("In Schedule: " +
                            Thread.currentThread().getName());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 1, 2, TimeUnit.SECONDS);

        Thread.sleep(7010);
        scheduledFuture.cancel(false);
        Thread.sleep(1000);
    }
}
