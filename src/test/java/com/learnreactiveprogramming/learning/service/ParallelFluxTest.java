package com.learnreactiveprogramming.learning.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@Slf4j
class ParallelFluxTest {

    @Test
    void parallelTest() {

        // given
        var numberOfCores = Runtime.getRuntime().availableProcessors();
        log.info("Number of cores: {}", numberOfCores);

        var parallelFlux = Flux.just("A", "B", "C", "D", "E", "F")
//                .publishOn(Schedulers.parallel())  // it takes 7 to 8 seconds
                .parallel()// it takes around 4 seconds
                .runOn(Schedulers.parallel())
                .map(ParallelFluxTest::toUppercase)
                .log();

        // when
        StepVerifier.create(parallelFlux)
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    void parallelFlatMapTest() {

        // given
        var numberOfCores = Runtime.getRuntime().availableProcessors();
        log.info("Number of cores: {}", numberOfCores);

        var parallelFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .flatMap(letter -> Mono.just(letter)
                        .map(ParallelFluxTest::toUppercase)
                        .subscribeOn(Schedulers.parallel()))
                .log();

        // when
        StepVerifier.create(parallelFlux)
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    void parallelFlatMapSequentialTest() {

        // given
        var numberOfCores = Runtime.getRuntime().availableProcessors();
        log.info("Number of cores: {}", numberOfCores);

        var parallelFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .flatMapSequential(letter -> Mono.just(letter)
                        .map(ParallelFluxTest::toUppercase)
                        .subscribeOn(Schedulers.parallel()))
                .log();

        // when
        StepVerifier.create(parallelFlux)
//                .expectNextCount(6)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    static String toUppercase(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return s.toUpperCase();
    }

}
