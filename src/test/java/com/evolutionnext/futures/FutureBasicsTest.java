package com.evolutionnext.futures;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class FutureBasicsTest {

    @Test
    public void testBasicFuture() throws ExecutionException, InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        Callable<Integer> callable = () -> {
            System.out.println("Inside ze future" + Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getPriority());
            Thread.sleep(3000);
            return 5 + 3;
        };

        System.out.println("In test:" + Thread.currentThread().getName());
        System.out.println("Main priority" + Thread.currentThread().getPriority());
        Future<Integer> future = fixedThreadPool.submit(callable);

        //This will block
        Integer result = future.get(); //block
        System.out.println("result = " + result);
    }

    @Test
    public void testBasicFutureAsync() throws ExecutionException, InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        Callable<Integer> callable = () -> {
            Thread.sleep(3000);
            return 5 + 3;
        };

        Future<Integer> future = cachedThreadPool.submit(callable);

        //This will not block
        while (!future.isDone()) {
            System.out.println("I am doing something else");
        }

        Integer result = future.get();
        System.out.println("result = " + result);
    }


    public Future<Stream<String>> downloadingContentFromURL(final String url) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        FutureTask<Stream<String>> futureTask = new FutureTask<>(() -> {
            URL netUrl = new URL(url);
            URLConnection urlConnection = netUrl.openConnection();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            urlConnection.getInputStream()));
            return reader
                    .lines()
                    .flatMap(x -> Arrays.stream(x.split(" ")));
        });
        cachedThreadPool.execute(futureTask);
        return futureTask;
    }

    @Test
    public void testGettingUrl() throws ExecutionException, InterruptedException {
        Future<Stream<String>> future = downloadingContentFromURL("http://www.cnn.com");
        while (!future.isDone()) {
            Thread.sleep(1000);
            System.out.println("Doing Something Else");
        }
        Stream<String> allStrings = future.get();
        allStrings
                .filter(x -> x.contains("Trump"))
                .forEach(System.out::println);

        Thread.sleep(5000);
    }



    @Test
    public void testFutureTaskAsRunnableInThread() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return 33 + 100;}
        );

        Thread thread = new Thread(task);
        thread.start();

        Integer result = task.get();
        System.out.println("result = " + result);
        Thread.sleep(5000);
    }


    @Test
    public void testFutureTaskAsRunnableDirect() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return 33 + 100;}
        );

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(task);

        Integer result = task.get(); //Block!
        System.out.println("result = " + result);
        Thread.sleep(5000);
    }


    @Test
    public void testFutureTaskUsingExecute() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println("In Task:" + Thread.currentThread().getName());
            return 40 + 100;
        });
        ExecutorService service = Executors.newFixedThreadPool(3);
        System.out.println("Starting task!");
        service.execute(task);
        System.out.println("result = " + task.get());
        System.out.println("isDone = " + task.isDone());
        Thread.sleep(5000);
    }

    @Test
    public void testCompletionService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService<Integer> service = new ExecutorCompletionService<>(executorService);
        service.submit(() -> {
            Thread.sleep(4000);
            return 4000;
        });
        service.submit(() -> {
            Thread.sleep(1000);
            return 1000;
        });
        service.submit(() -> {
            Thread.sleep(8000);
            return 8000;
        });
        service.submit(() -> {
            Thread.sleep(100);
            return 100;
        });

        System.out.println("result = " + service.take().get());
        System.out.println("result = " + service.take().get());
        System.out.println("result = " + service.take().get());
        System.out.println("result = " + service.take().get());
    }
}
