package com.learnreactiveprogramming.learning;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SubscribeOnPublishOn {
    public static void main(String[] args) {
        Flux<Integer> flux = Flux.range(1, 5)
                .map(i -> {
                    System.out.println("Map1: " + i + " - " + Thread.currentThread().getName());
                    return i;
                })
                .subscribeOn(Schedulers.boundedElastic()) // Afecta toda la cadena de operaciones hasta el origen
                .map(i -> {
                    System.out.println("Map2: " + i + " - " + Thread.currentThread().getName());
                    return i;
                })
                .publishOn(Schedulers.parallel()) // Cambia el hilo para las operaciones que siguen
                .map(i -> {
                    System.out.println("Map3: " + i + " - " + Thread.currentThread().getName());
                    return i;
                })
                .log();

        flux.subscribe(i -> System.out.println("Received: " + i + " - " + Thread.currentThread().getName()));
    }
}
