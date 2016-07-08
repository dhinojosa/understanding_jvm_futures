package com.evolutionnext.futures;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class GuavaFutureTest {

    /**
     * Demo 8: Guava Listening Executors
     */
    @Test
    public void testGuavaFutures() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ListeningExecutorService listeningExecutorService =
                MoreExecutors.listeningDecorator(executorService);

        ListenableFuture<Integer> listenableFuture =
                listeningExecutorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return 33 + 40;
                    }
                });

        Futures.addCallback(listenableFuture,
                new FutureCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        System.out.println(
                                "Got the result and the answer is? " + result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        System.out.println("Things happened man. Bad things" + t.getMessage());
                    }
                }
        );
        Thread.sleep(4000);
    }

    @Test
    public void testGuavaFutureMapEquivalent() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ListeningExecutorService listeningExecutorService = MoreExecutors
                .listeningDecorator(executorService);

        ListenableFuture<Integer> listenableFuture = listeningExecutorService
                .submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return 33 + 40;
                    }
                });

        ListenableFuture<Integer> mapped = Futures.transform
                (listenableFuture,
                        (Function<Integer, Integer>) integer -> integer + 40);


        Futures.addCallback(mapped,
                new FutureCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer result) {
                        System.out.println(
                                "Got the result and the answer is? "
                                        + result);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        System.out.println(
                                "Things happened man. Bad things"
                                        + t.getMessage());
                    }
                }
        );
        Thread.sleep(4000);
    }

    public ListenableFuture<Stream<String>> downloadingContentFromURL(final String url) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        return listeningExecutorService.submit(() -> {
            URL netUrl = new URL(url);
            URLConnection urlConnection = netUrl.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));
            return reader
                    .lines()
                    .flatMap(x -> Arrays.stream(x.split(" ")));
        });
    }

    @Test
    public void testGuavaFutureFlatMapEquivalent() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ListeningExecutorService listeningExecutorService =
                MoreExecutors.listeningDecorator(executorService);
        ListenableFuture<String> listenableFuture =
                listeningExecutorService.submit(() -> "http://www.nytimes.com");

        ListenableFuture<Stream<String>> mapped =
                Futures.transformAsync(listenableFuture,
                        new AsyncFunction<String, Stream<String>>() {
                            @Override
                            public ListenableFuture<Stream<String>> apply(String url) throws Exception {
                                return downloadingContentFromURL(url);
                            }
                        });

        Futures.addCallback(mapped,
                new FutureCallback<Stream<String>>() {
                    @Override
                    public void onSuccess(Stream<String> result) {
                        result
                                .filter(x -> x.contains("Unemployment"))
                                .forEach(System.out::println);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        System.out.println("Things happened man. Bad things" + t.getMessage());
                    }
                }
        );
        Thread.sleep(4000);
    }
}