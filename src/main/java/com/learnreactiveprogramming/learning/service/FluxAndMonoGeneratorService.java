package com.learnreactiveprogramming.learning.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

    private static final  Logger logger = LoggerFactory.getLogger(FluxAndMonoGeneratorService.class);

    public Flux<String> namesFlux(){
        return  Flux.fromIterable(List.of("Alex", "Ben", "Chloe"));
    }

    public Flux<String> namesFluxMap(){
        return  Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> exploreZip(){
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");
        return  Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t -> t.getT1() + t.getT2() + t.getT3() + t.getT4())
                .log();
    }

    public Flux<String> exceptionFlux(){
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("D"));
    }


    public Flux<String> namesFluxImmutability(){
        var namesFlux =   Flux.fromIterable(List.of("Alex", "Ben", "Chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
    }

    public Flux<String> namesFlux_flatMap(){
        return  Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .flatMap(this::splitString)
                .log();
    }


    public Flux<String> namesFlux_concatMap(int nameLength){
        return  Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .map(String::toUpperCase)
                .filter( n -> n.length() > nameLength)
                .concatMap(this::splitString)
                .log();
    }

    public Mono<List<String>> namesMono_mono_flatMap(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(n -> n.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_mono_flatMapMany(int stringLength){
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(n -> n.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }


    private Mono<List<String>> splitStringMono(String s) {
        var chatArray = s.split("");
        var listChat = List.of(chatArray);
        return Mono.just(listChat);
    }

    public Flux<String> splitString(String name){
        var chatArray = name.split("");
        return Flux.fromArray(chatArray);
    }

    public Mono<String> namesMono(){
        return  Mono.just("Alex");
    }

    public Flux<String> exploreFluxOnError(){
        return Flux.just("A", "B", "C")
                .concatWith(Flux.error(new IllegalArgumentException("Exception Occurred")))
                .onErrorReturn("D");
    }

    public static void main(String[] args) {
        var fluxAndMono = new FluxAndMonoGeneratorService();
        logger.debug(" == starting flux operation == ");
        fluxAndMono.namesFlux()
                .log()
                .subscribe(item -> System.out.println("Item is: " + item));
        logger.debug(" == ending flux operation == ");

        logger.debug(" == starting mono operation == ");
        fluxAndMono.namesMono()
                .log()
                .subscribe(item -> System.out.println("Item is: " + item));
        logger.debug(" == ending mono operation == ");

    }


}
