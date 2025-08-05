package com.github.movierating.api.rest.v1.controller;

import com.github.movierating.api.rest.controller.v1.MovieRatingApi;
import com.github.movierating.api.rest.dto.v1.RatingApiDto;
import com.github.movierating.api.rest.dto.v1.RatingDetailApiDto;
import com.github.movierating.api.rest.v1.mapper.MovieRatingApiDtoMapper;
import com.github.movierating.dto.RatingDto;
import com.github.movierating.service.MovieRatingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class MovieRatingController implements MovieRatingApi {

    private final MovieRatingService movieRatingService;
    private final MovieRatingApiDtoMapper movieRatingApiDtoMapper;

    @Override
    public ResponseEntity<RatingDetailApiDto> createMovieRating(final Long movieId, final RatingApiDto ratingApiDto) {
        log.info("Received request to create rating {} for movie id {}", ratingApiDto, movieId);
        var response = movieRatingApiDtoMapper.map(movieRatingService.createRating(RatingDto.builder()
                .movieId(movieId)
                .rating(ratingApiDto.getRating())
                .build()));

        log.info("Rating successfully created {}", response);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteMovieRating(final Long movieId, final Long ratingId) {
        log.info("Received request to delete rating id {} for movie id {}", ratingId, movieId);
        movieRatingService.deleteRating(RatingDto.builder()
                .id(ratingId)
                .movieId(movieId)
                .build());

        log.info("Rating successfully deleted");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<RatingDetailApiDto> updateMovieRating(final Long movieId, final Long ratingId, final RatingApiDto ratingApiDto) {
        log.info("Received request to update rating id {} for movie id {}", ratingId, movieId);
        var response = movieRatingApiDtoMapper.map(movieRatingService.updateRating(RatingDto.builder()
                .id(ratingId)
                .movieId(movieId)
                .rating(ratingApiDto.getRating())
                .build()));

        log.info("Rating successfully updated {}", response);
        return ResponseEntity.ok(response);
    }

}
