package com.evolutionnext.futures;


import org.junit.Test;

import java.util.concurrent.*;

public class CompletionServiceTest {

    @Test
    public void testCompletionService()
            throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CompletionService<Integer> service =
                new ExecutorCompletionService<>(executorService);

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

        //Blocking on take. Unfortunate
        //This block synchronous?
        Future<Integer> take1 = service.take();
        System.out.println("Take 1 complete");
        Future<Integer> take2 = service.take();
        System.out.println("Take 2 complete");
        Future<Integer> take3 = service.take();
        System.out.println("Take 3 complete");
        Future<Integer> take4 = service.take();
        System.out.println("Take 4 complete");

        System.out.println("result = " + take1.get());
        System.out.println("result = " + take2.get());
        System.out.println("result = " + take3.get());
        System.out.println("result = " + take4.get());
    }
}
