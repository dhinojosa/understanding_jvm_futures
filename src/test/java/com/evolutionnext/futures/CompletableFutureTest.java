package com.evolutionnext.futures;

import org.junit.Before;
import org.junit.Test;

import java.util.IllegalFormatException;
import java.util.InputMismatchException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureTest {

    private CompletableFuture<Integer> integerFuture1;
    private CompletableFuture<Integer> integerFuture2;
    private CompletableFuture<String> stringFuture1;
    private ExecutorService executorService;

    @Before
    public void startUp() {
        executorService = Executors.newCachedThreadPool();

        integerFuture1 = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        System.out.println("intFuture1 is Sleeping in thread: "
                                + Thread.currentThread().getName());
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 43 + 12;
                });

        integerFuture2 = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        System.out.println("intFuture2 is sleeping in thread: "
                                + Thread.currentThread().getName());
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 500 + 55;
                });

        stringFuture1 = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        System.out.println("stringFuture2 is sleeping in thread: "
                                + Thread.currentThread().getName());
                        Thread.sleep(4300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Boston, MA";
                });
    }

    @Test
    public void completableFutureWithThenApply() throws InterruptedException {
        CompletableFuture<Integer> thenApply =
                integerFuture1.thenApply(x -> {
                    System.out.println("In Block:" +
                            Thread.currentThread().getName());
                    return x + 19;
                });
        thenApply.thenAccept(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void completableFutureWithThenApplyAsync() throws InterruptedException {
        CompletableFuture<Integer> thenApplyAsync =
                integerFuture1.thenApplyAsync(x -> {
                    System.out.println("In Block:" +
                            Thread.currentThread().getName());
                    return x + 19;
                }, executorService);
        thenApplyAsync.thenAccept(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void applyToEither() throws InterruptedException {
        integerFuture1
                .applyToEither(integerFuture2, x -> x * 10)
                .thenAccept(System.out::println);
        Thread.sleep(6000);
    }

    @Test
    public void completableFutureWithThenAccept() throws InterruptedException {
        integerFuture1.thenAccept(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void completableFutureWithThenAcceptAsync() throws InterruptedException {
        integerFuture1.thenAcceptAsync(x -> {
            System.out.println("Got value: " + x + " in "
                    + Thread.currentThread().getName());
        }, executorService);
        Thread.sleep(5000);
    }

    @Test
    public void completableFutureWithThenRun() throws InterruptedException {
        integerFuture1.thenRun(() -> {
            String successMessage =
                    "I am doing something else once" +
                            " that future has been triggered!";
            System.out.println
                    (successMessage);
        });
        Thread.sleep(3000);
    }

    @Test
    public void completableFutureAcceptEither() throws InterruptedException {
        integerFuture1.acceptEither(integerFuture2, System.out::println);
        Thread.sleep(4000);
    }

    @Test
    public void completableFutureExceptionally() throws InterruptedException {
        stringFuture1.thenApply(Integer::parseInt)
                .exceptionally(t -> -1).thenAccept(System.out::println);
        Thread.sleep(6000);
    }

    @Test
    public void completableFutureHandle() throws InterruptedException {
        stringFuture1.thenApply(Integer::parseInt).handle((i, t) -> {
            if (i != null) return i;
            else return -1;
        }).thenAccept(System.out::println);

        Thread.sleep(6000);
    }

    public CompletableFuture<Integer> getTemperatureInFahrenheit(final String cityState) {
        return CompletableFuture.supplyAsync(() -> {
            //We go into a webservice to find the weather...
            System.out.println("In getTemperatureInFahrenheit: " +
                    Thread.currentThread().getName());
            System.out.println("Finding the temperature for " + cityState);
            return 44;
        });
    }

    //Need slides for completableCompose vs. combine
    @Test
    public void completableCompose() throws InterruptedException {
        CompletableFuture<Integer> combine =
                stringFuture1.thenCompose(this::getTemperatureInFahrenheit);
        combine.thenAccept(System.out::println);
        Thread.sleep(6000);
    }

    @Test
    public void completableCombine() throws InterruptedException {
        CompletableFuture<Integer> combine =
                integerFuture1
                        .thenCombine(integerFuture2, (x, y) -> x + y);
        combine.thenAccept(System.out::println);
        Thread.sleep(6000);
    }

    @Test
    public void completeAcceptBoth() throws InterruptedException {
        integerFuture1.thenAcceptBoth(integerFuture2, (x, y) -> {
            System.out.println("Inside of accept both:"
                    + Thread.currentThread().getName());
            System.out.println("x = " + x);
            System.out.println("y = " + y);
        });
        Thread.sleep(6000);
    }

    @Test
    public void completeAcceptBothAsync() throws InterruptedException {
        integerFuture1.thenAcceptBothAsync(integerFuture2, (x, y) -> {
            System.out.println("Inside of accept both:"
                    + Thread.currentThread().getName());
            System.out.println("x = " + x);
            System.out.println("y = " + y);
        }, executorService);
        Thread.sleep(6000);
    }

    @Test
    public void testAllOf() throws InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture.allOf(integerFuture1, integerFuture2).join();
        long end = System.currentTimeMillis();
        System.out.println(
                "Guaranteed that all futures have completed in: "
                + (end - start));
        Thread.sleep(6000);
    }

    @Test
    public void testAnyOf() throws InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture.anyOf(integerFuture1, integerFuture2).join();
        long end = System.currentTimeMillis();
        System.out.println(
                "Guaranteed that any of the futures have completed in: "
                        + (end - start));
        Thread.sleep(6000);
    }

    @Test
    public void testCompletableFuturePromise() {
        CompletableFuture<Integer> completableFuture =
                new CompletableFuture<>();

        completableFuture.thenAccept(System.out::println);

        System.out.println("Processing something else");

        completableFuture.complete(50);
    }

    @Test
    public void testCompletableFuturePromiseWithException() {
        CompletableFuture<Integer> completableFuture =
                new CompletableFuture<>();

        completableFuture.handleAsync((item, throwable) -> {
           if (throwable != null) {
               throwable.printStackTrace();
               return -1;
           } else {
               return item;
           }
        });

        System.out.println("Processing something else");

        completableFuture.completeExceptionally(
                new InputMismatchException("Just for fun"));
    }
}
