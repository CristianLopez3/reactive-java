package com.learnreactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BackpressureTest {

    @Test
    void testBackPressure() {

        var numberRange = Flux.range(1, 100);
        numberRange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);

                    }

                    @Override
                    protected void hookOnNext(Integer value) {
//                        super.hookOnNext(value);
                        log.info("Hook on next: {}", value);
                        if(value == 2){
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
//                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
//                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
//                        super.hookOnCancel();
                        log.info("Hook on cancel");
                    }
                });


    }


    @Test
    void textBackPressure1() throws InterruptedException {
        var numberRange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);

        numberRange
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);

                    }

                    @Override
                    protected void hookOnNext(Integer value) {
//                        super.hookOnNext(value);
                        log.info("Hook on next: {}", value);
                        if(value % 2 == 0 || value < 50){
                            request(2);
                        } else {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
//                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
//                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
//                        super.hookOnCancel();
                        log.info("Hook on cancel");
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

    }

    @Test
    void textBackPressureWithBuffer() throws InterruptedException {
        var numberRange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);

        numberRange
                .onBackpressureBuffer(10, i -> log.info("Dropping: {}", i))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);

                    }

                    @Override
                    protected void hookOnNext(Integer value) {
//                        super.hookOnNext(value);
                        log.info("Hook on next: {}", value);
                        if(value % 2 == 0 || value < 50){
                            request(2);
                        } else {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
//                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
//                        super.hookOnError(throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
//                        super.hookOnCancel();
                        log.info("Hook on cancel");
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

    }

    @Test
    void textBackPressureOnError() throws InterruptedException {
        var numberRange = Flux.range(1, 100).log();
        CountDownLatch latch = new CountDownLatch(1);

        numberRange
                .onBackpressureError()
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(2);

                    }

                    @Override
                    protected void hookOnNext(Integer value) {
//                        super.hookOnNext(value);
                        log.info("Hook on next: {}", value);
                        if(value % 2 == 0 || value < 50){
                            request(2);
                        } else {
                            cancel();
                        }
                    }

                    @Override
                    protected void hookOnComplete() {
//                        super.hookOnComplete();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        log.error("Error is: {}", throwable);
                    }

                    @Override
                    protected void hookOnCancel() {
//                        super.hookOnCancel();
                        log.info("Hook on cancel");
                        latch.countDown();
                    }
                });

        assertTrue(latch.await(5, TimeUnit.SECONDS));

    }
}
