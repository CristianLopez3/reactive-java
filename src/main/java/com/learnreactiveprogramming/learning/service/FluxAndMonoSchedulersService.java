package com.learnreactiveprogramming.learning.service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

import static com.learnreactiveprogramming.util.CommonUtil.delay;

@Slf4j
public class FluxAndMonoSchedulersService {

    static List<String> namesList = List.of("alex", "ben", "chloe");
    static List<String> namesList1 = List.of("adam", "jill", "jack");

    public Flux<String> explorePublishOn() {
        var namesFlux = getNamesList(namesList);

        var namesFlux1 = Flux.fromIterable(namesList1)
                .publishOn(Schedulers.boundedElastic())
                .map(this::upperCase)
                .map(s -> {
                    log.info("Name: {} ", s);
                    return s;
                })
                .log();

        return namesFlux.concatWith(namesFlux1);
    }

    public Flux<String> exploreSubscribeOn() {
        var namesFlux = getNamesList(namesList)
                .log();

        var namesFlux1 = getNamesList(namesList1)
                .map(s -> {
                    log.info("Name: {}", s);
                    return s;
                })
                .log();

        return namesFlux.concatWith(namesFlux1);
    }

    private Flux<String> getNamesList(List<String> namesList) {
        return Flux.fromIterable(namesList)
                .map(this::upperCase);
    }

    private String upperCase(String name) {
        delay(1000);
        return name.toUpperCase();
    }

}
