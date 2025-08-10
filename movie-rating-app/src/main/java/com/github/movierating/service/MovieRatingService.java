package com.github.movierating.service;

import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.dto.RatingDto;

public interface MovieRatingService {

    RatingDetailDto createRating(RatingDto ratingDto);

    RatingDetailDto updateRating(RatingDto ratingDto);

    void deleteRating(RatingDto ratingDto);

}
