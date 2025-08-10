package com.github.movierating;

import com.github.movierating.dao.MovieRepository;
import com.github.movierating.dao.RatingRepository;
import com.github.movierating.dao.UserRepository;
import com.github.movierating.entity.Movie;
import com.github.movierating.entity.Rating;
import com.github.movierating.entity.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieRatingIntegrationTest {

    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withInitScript("01_app_tables.sql");

    private final static User testUser = createTestUser();
    private final static Movie testMovie = createTestMovie();

    @LocalServerPort
    private Integer port;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingRepository ratingRepository;

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
        RestAssured.baseURI = "http://localhost:" + port;
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        movieRepository.deleteAll();

        testUser.setId(null);
        testUser.setVersion(null);
        testMovie.setId(null);
        testMovie.setVersion(null);

        userRepository.save(testUser);
        movieRepository.save(testMovie);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void createMovieRating() {
        given()
                .auth()
                .basic(testUser.getEmail(), "password")
                .contentType(ContentType.JSON)
                .when()
                .body("""
                {
                    "rating": "5"
                }
                """)
                .post("/rest/v1/movies/" + testMovie.getId() + "/ratings")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("rating", equalTo(5),
                      "ratedAt", notNullValue(),
                      "id", notNullValue(),
                      "movie.id", equalTo(testMovie.getId().intValue()),
                      "movie.name", equalTo(testMovie.getName()),
                      "user.id", equalTo(testUser.getId().intValue()),
                      "user.email", equalTo(testUser.getEmail()));

        assertEquals(1, ratingRepository.findAll().size());

        var savedRating = ratingRepository.findAll().getFirst();
        assertEquals(testMovie.getId(), savedRating.getMovie().getId());
        assertEquals(testUser.getId(), savedRating.getUser().getId());
        assertEquals(5, savedRating.getRating());
        assertNotNull(savedRating.getRatedAt());
    }

    @Test
    void updateMovieRating() {
        var existentRating = new Rating();
        existentRating.setRating(5);
        existentRating.setRatedAt(LocalDateTime.now());
        existentRating.setMovie(testMovie);
        existentRating.setUser(testUser);
        ratingRepository.saveAndFlush(existentRating);

        given()
                .auth()
                .basic(testUser.getEmail(), "password")
                .contentType(ContentType.JSON)
                .when()
                .body("""
                {
                    "rating": "10"
                }
                """)
                .put("/rest/v1/movies/" + testMovie.getId() + "/ratings/" + existentRating.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("rating", equalTo(10),
                        "ratedAt", notNullValue(),
                        "id", notNullValue(),
                        "movie.id", equalTo(testMovie.getId().intValue()),
                        "movie.name", equalTo(testMovie.getName()),
                        "user.id", equalTo(testUser.getId().intValue()),
                        "user.email", equalTo(testUser.getEmail()));

        assertEquals(1, ratingRepository.findAll().size());

        var savedRating = ratingRepository.findAll().getFirst();
        assertEquals(testMovie.getId(), savedRating.getMovie().getId());
        assertEquals(testUser.getId(), savedRating.getUser().getId());
        assertEquals(10, savedRating.getRating());
        assertNotNull(savedRating.getRatedAt());
    }

    @Test
    void deleteMovieRating() {
        var existentRating = new Rating();
        existentRating.setRating(5);
        existentRating.setRatedAt(LocalDateTime.now());
        existentRating.setMovie(testMovie);
        existentRating.setUser(testUser);
        ratingRepository.saveAndFlush(existentRating);

        given()
                .auth()
                .basic(testUser.getEmail(), "password")
                .contentType(ContentType.JSON)
                .when()
                .delete("/rest/v1/movies/" + testMovie.getId() + "/ratings/" + existentRating.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertEquals(0, ratingRepository.findAll().size());
    }

    private static User createTestUser() {
        var testUser = new User();
        testUser.setEmail("michal@gmail.com");
        testUser.setPassword("{noop}password");
        return testUser;
    }

    private static Movie createTestMovie() {
        var testMovie = new Movie();
        testMovie.setName("Harry Potter");
        return testMovie;
    }

}
