package com.learnreactiveprogramming.learning;

import reactor.core.publisher.Flux;

public class BackpressureExample {
    public static void main(String[] args) {
        Flux<Integer> numberFlux = Flux.range(1, 100)
                .doOnNext(i -> {
                    // Simulate some processing time
                    try {
                        Thread.sleep(100); // Simulate slow processing
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        numberFlux
                .onBackpressureBuffer(10) // Buffer up to 10 items
                .subscribe(
                        item -> System.out.println("Received: " + item),
                        error -> System.err.println("Error: " + error),
                        () -> System.out.println("Completed")
                );

        // Keep the application running to see the output
        try {
            Thread.sleep(10000); // Main thread sleeps for 10 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}