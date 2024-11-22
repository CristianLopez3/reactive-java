package com.learnreactiveprogramming.learning.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    private static FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void exceptionFlux() {
        var fluxError = fluxAndMonoGeneratorService.exceptionFlux().log();
        StepVerifier.create(fluxError)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void exceptionOnErrorReturn(){
        var value = fluxAndMonoGeneratorService.exploreFluxOnError();
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D") // replace the exception for the default value "D")
                .verifyComplete();
    }

}