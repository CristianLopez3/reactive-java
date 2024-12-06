package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

public class MovieReactiveService {

    private static final Logger log = LoggerFactory.getLogger(MovieReactiveService.class);
    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;
    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies() {
        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    throw new MovieException(ex.getMessage());
                })
                .retry(3)
                .log();
    }

    public Flux<Movie> getAllMoviesRetryWhen() {
        var retryWhen = getRetryBackoffSpec();

        var moviesInfoFlux = movieInfoService.retrieveMoviesFlux();
        return moviesInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    if (ex instanceof RuntimeException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(retryWhen)
                .log();
    }

    public Flux<Movie> getAllMoviesRepeat(long nTimes) {

        return  movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono
                            .map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);
                    if (ex instanceof RuntimeException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                .retryWhen(getRetryBackoffSpec())
                .repeat(nTimes)
                .log();
    }

    private static RetryBackoffSpec getRetryBackoffSpec() {
        var retryWhen = Retry.backoff(3, Duration.ofMillis(500))
                .filter(ex -> ex instanceof MovieException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) ->
                        Exceptions.propagate(retrySignal.failure())
                ));
        return retryWhen;
    }

    public Mono<Movie> getMovieById(long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId)
                .collectList();

        return movieInfoMono.zipWith(reviewsFlux, Movie::new);

    }

    public Mono<Movie> getMovieByIdWithRevenue(long movieId) {
        var movieInfoMono = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux = reviewService.retrieveReviewsFlux(movieId)
                .collectList();

        var revenueMono = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono.zipWith(reviewsFlux, Movie::new)
                .zipWith(revenueMono, (movie, revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                });

    }

}
