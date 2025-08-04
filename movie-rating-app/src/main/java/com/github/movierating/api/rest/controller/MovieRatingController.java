package com.github.movierating.api.rest.controller;

import com.github.movierating.api.rest.controller.v1.MovieRatingApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class MovieRatingController implements MovieRatingApi {

    @Override
    public ResponseEntity<Void> createMovieRating(Long movieId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteMovieRating(Long movieId, Long ratingId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateMovieRating(Long movieId, Long ratingId) {
        return null;
    }
}
