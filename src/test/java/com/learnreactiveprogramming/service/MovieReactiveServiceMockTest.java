package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @InjectMocks
    private MovieReactiveService movieReactiveService;

    @Mock
    private MovieInfoService movieInfoService;

    @Mock
    private ReviewService reviewService;

    @Test
    void getAllMovies() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        // when
        var moviesFlux = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void getAllMoviesError() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException("Exception Occurred"));

        // when
        var moviesFlux = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();

    }


    @Test
    void getAllMoviesErrorWithRetry() {
        // given
        var errorMessage = "Exception Occurred";
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(errorMessage));

        // when
        var moviesFlux = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(moviesFlux)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));

    }

    @Test
    void getAllMoviesErrorWithRetryWhen() {
        // given
        var errorMessage = "Exception Occurred";
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(errorMessage));

        // when
        var moviesFlux = movieReactiveService.getAllMoviesRetryWhen();

        // then
        StepVerifier.create(moviesFlux)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));

    }

    @Test
    void getAllMoviesErrorWithRepeat(){
        // given
        var nTimes = 2L;
        var errorMessage = "Exception Occurred";
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenThrow(new RuntimeException(errorMessage));

        // when
        var moviesFlux = movieReactiveService.getAllMoviesRepeat(nTimes);

        // then
        StepVerifier.create(moviesFlux)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(isA(Long.class));

    }

}