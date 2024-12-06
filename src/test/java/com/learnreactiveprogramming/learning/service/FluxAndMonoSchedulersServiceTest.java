package com.learnreactiveprogramming.learning.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoSchedulersServiceTest {

    FluxAndMonoSchedulersService fluxAndMonoSchedulersService = new FluxAndMonoSchedulersService();

    @Test
    void explorePublishOn(){
        // given

        //when

        var namesFlux = fluxAndMonoSchedulersService.explorePublishOn();

        //then
        StepVerifier.create(namesFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void exploreSubscribeOn(){
        // given

        //when

        var namesFlux = fluxAndMonoSchedulersService.explorePublishOn();

        //then
        StepVerifier.create(namesFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

}