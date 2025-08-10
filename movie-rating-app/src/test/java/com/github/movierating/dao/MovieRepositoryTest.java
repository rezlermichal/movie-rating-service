package com.github.movierating.dao;

import com.github.movierating.entity.Movie;
import com.github.movierating.entity.Rating;
import com.github.movierating.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MovieRepositoryTest {

    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("01_app_tables.sql");

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void findAllWithAvgRating() {
        var harryPotter = createMovie("harry potter");
        var topGun = createMovie("top gun");
        var godFather = createMovie("The Godfather");

        prepareUsersAndRating(harryPotter, topGun, godFather);

        var result = movieRepository.findAllWithAvgRating(PageRequest.of(0, 10, Sort.unsorted()));
        assertEquals(0, result.getNumber());
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals(1, result.getTotalPages());

        assertTrue(result.getContent().stream().anyMatch(movie -> movie.name().equals(harryPotter.getName()) && movie.avgRating().intValue() == 6));
        assertTrue(result.getContent().stream().anyMatch(movie -> movie.name().equals(topGun.getName()) && movie.avgRating().intValue() == 5));
        assertTrue(result.getContent().stream().anyMatch(movie -> movie.name().equals(godFather.getName()) && movie.avgRating().intValue() == 9));
    }

    @Test
    void findAllWithAvgRating_sorted() {
        var harryPotter = createMovie("harry potter");
        var topGun = createMovie("top gun");
        var godFather = createMovie("The Godfather");

        prepareUsersAndRating(harryPotter, topGun, godFather);

        var result = movieRepository.findAllWithAvgRating(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "avg_rating")));
        assertEquals(0, result.getNumber());
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());
        assertEquals(1, result.getTotalPages());

        assertEquals(godFather.getName(), result.getContent().getFirst().name());
        assertEquals(9, result.getContent().getFirst().avgRating().intValue());
        assertEquals(harryPotter.getName(), result.getContent().get(1).name());
        assertEquals(6, result.getContent().get(1).avgRating().intValue());
        assertEquals(topGun.getName(), result.getContent().get(2).name());
        assertEquals(5, result.getContent().get(2).avgRating().intValue());
    }

    @Test
    void findAllWithAvgRating_pagingSorting() {
        var harryPotter = createMovie("harry potter");
        var topGun = createMovie("top gun");
        var godFather = createMovie("The Godfather");

        prepareUsersAndRating(harryPotter, topGun, godFather);

        var result = movieRepository.findAllWithAvgRating(PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "avg_rating")));
        assertEquals(1, result.getNumber());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(3, result.getTotalPages());

        assertEquals(harryPotter.getName(), result.getContent().getFirst().name());
        assertEquals(6, result.getContent().getFirst().avgRating().intValue());
    }

    private void prepareUsersAndRating(Movie harryPotter, Movie topGun, Movie godFather) {
        var user1 = createUser("user1@gmail.com");
        var user2 = createUser("user2@gmail.com");
        var user3 = createUser("user3@gmail.com");

        createRating(harryPotter, user1, 5);
        createRating(harryPotter, user2, 6);
        createRating(harryPotter, user3, 7);
        createRating(topGun, user1, 4);
        createRating(topGun, user2, 5);
        createRating(topGun, user3, 6);
        createRating(godFather, user1, 8);
        createRating(godFather, user2, 9);
        createRating(godFather, user3, 10);
    }

    private Rating createRating(Movie movie, User user, int ratingNum) {
        var rating = new Rating();
        rating.setMovie(movie);
        rating.setUser(user);
        rating.setRating(ratingNum);
        rating.setRatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    private Movie createMovie(String name) {
        var movie = new Movie();
        movie.setName(name);
        return movieRepository.save(movie);
    }

    private User createUser(String email) {
        var user = new User();
        user.setEmail(email);
        user.setPassword("pwd");
        return userRepository.save(user);
    }

}
