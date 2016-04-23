package com.evolutionnext.futures;

import org.junit.Test;

import java.util.concurrent.*;

public class ScheduledFuturesTest {

    @Test
    public void testScheduledFuture() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);

        ScheduledFuture<Integer> schedule = scheduledExecutorService.schedule(() -> {
            System.out.println("In Schedule: " + Thread.currentThread().getName());
            return 40 + 50;
        }, 3, TimeUnit.SECONDS);

        System.out.println(schedule.get());
        Thread.sleep(12000);
    }

    @Test
    public void testScheduledFutureWithFixedDelay() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(6);

        final ScheduledFuture<?> scheduledFuture =
                scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("In Schedule: " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);

        Thread.sleep(20000);
        scheduledFuture.cancel(false);
        Thread.sleep(2000);
    }

    @Test
    public void testScheduledFutureWithFixedRate() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(2);

        final ScheduledFuture<?> scheduledFuture =
                scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("In Schedule: " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);

        Thread.sleep(6000);
        scheduledFuture.cancel(false);
        Thread.sleep(1000);
    }
}
