package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {

    private final MovieInfoService movieInfoService = new MovieInfoService();
    private final ReviewService reviewService = new ReviewService();
    private RevenueService revenueService = new RevenueService();

    MovieReactiveService movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, revenueService);

    @Test
    void testGetAllMovies(){
        Flux<Movie> movies = movieReactiveService.getAllMovies().log();
        StepVerifier.create(movies)
                .assertNext(movie -> {
                    assertNotNull(movie);
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertNotNull(movie);
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertNotNull(movie);
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void testGetMoviesById(){
        Mono<Movie> movie = movieReactiveService.getMovieById(100L).log();
        StepVerifier.create(movie)
                .assertNext(m -> {
                    assertNotNull(m);
                    assertEquals("Batman Begins", m.getMovie().getName());
                    assertEquals(2, m.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdWithRevenue() {
        Mono<Movie> movie = movieReactiveService.getMovieByIdWithRevenue(100L).log();
        StepVerifier.create(movie)
                .assertNext(m -> {
                    assertNotNull(m.getRevenue());
                    assertEquals("Batman Begins", m.getMovie().getName());
                    assertEquals(2, m.getReviewList().size());
                })
                .verifyComplete();

    }
}