package com.learnreactiveprogramming.learning.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    private static final FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void exceptionFlux() {
        var fluxError = fluxAndMonoGeneratorService.exceptionFlux().log();
        StepVerifier.create(fluxError)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void exceptionOnErrorReturn() {
        var value = fluxAndMonoGeneratorService.exploreFluxOnError();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D") // replace the exception for the default value "D")
                .verifyComplete();
    }

    @Test
    void onErrorResume() {
        var e = new RuntimeException("Not a valid State");
        var value = fluxAndMonoGeneratorService.exploreOnErrorResume(e).log();
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void onErrorContinue() {
        var value = fluxAndMonoGeneratorService.exploreOnErrorContinue();
        StepVerifier.create(value)
                .expectNext("A", "C", "D")
                .verifyComplete();
    }

    @Test
    void onErrorMap() {
        var value = fluxAndMonoGeneratorService.exploreOnErrorMap();
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void doOnError(){
        var value = fluxAndMonoGeneratorService.exploreDoOnError();
        StepVerifier.create(value)
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void onErrorMapWithMono(){
        var value = fluxAndMonoGeneratorService.onErrorMapWithMono();
        StepVerifier.create(value)
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void onErrorContinueSuccess(){
        var input = "reactor";
        var value = fluxAndMonoGeneratorService.onErrorContinueWithMono(input);
        StepVerifier.create(value)
                .expectNext(input)
                .verifyComplete();
    }

    @Test
    void onErrorContinueError(){
        var input = "abc";
        var value = fluxAndMonoGeneratorService.onErrorContinueWithMono(input);
        StepVerifier.create(value)
                .expectNextCount(0)
                .verifyComplete();
    }

}