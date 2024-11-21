package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.learning.service.FluxAndMonoGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService;

    @BeforeEach
    void setUp(){
        fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    }

    @Test
    void namesFlux() {
        // given

        //when
        var namedFlux = fluxAndMonoGeneratorService.namesFlux();

        // then
        StepVerifier.create(namedFlux)
//                .expectNext("Alex").expectNext("Ben").expectNext("Chloe")
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesMono() {
        var monoNames = fluxAndMonoGeneratorService.namesMono();

        StepVerifier.create(monoNames)
                .expectNext("Alex")
                .verifyComplete();
    }

    @Test
    void namesFluxMap(){

        var fluxNames = fluxAndMonoGeneratorService.namesFluxMap();

        StepVerifier.create(fluxNames)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability(){

        var fluxNames = fluxAndMonoGeneratorService.namesFluxImmutability();

        StepVerifier.create(fluxNames)
                .expectNext("Alex", "Ben", "Chloe")
                .verifyComplete();
    }


    @Test
    void namesFlux_FlatMap(){

        var fluxNames = fluxAndMonoGeneratorService.namesFlux_flatMap();

        StepVerifier.create(fluxNames)
                .expectNext("A", "L", "E", "X", "B", "E", "N", "C", "H", "L", "O", "E") 
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        int stringLength  = 3;

        var namesFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesMono_mono_flatMap() {
        int stringLength = 3;
        var namesMono = fluxAndMonoGeneratorService.namesMono_mono_flatMap(stringLength);

        StepVerifier.create(namesMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMono_mono_flatMapMany() {
        int stringLength = 3;
        Flux<String> namesMono = fluxAndMonoGeneratorService.namesMono_mono_flatMapMany(stringLength);

        StepVerifier.create(namesMono)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }


    @Test
    void exploreZip() {
        var resultFlux = fluxAndMonoGeneratorService.exploreZip();
        StepVerifier.create(resultFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }
}
