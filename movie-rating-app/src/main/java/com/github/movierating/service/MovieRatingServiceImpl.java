package com.github.movierating.service;

import com.github.movierating.dao.MovieRepository;
import com.github.movierating.dao.RatingRepository;
import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.dto.RatingDto;
import com.github.movierating.entity.Movie;
import com.github.movierating.entity.Rating;
import com.github.movierating.entity.User;
import com.github.movierating.enums.ErrorCode;
import com.github.movierating.exception.*;
import com.github.movierating.mapper.RatingMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MovieRatingServiceImpl implements MovieRatingService {

    private final UserService userService;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public RatingDetailDto createRating(final RatingDto ratingDto) {
        var movie = movieRepository.findById(ratingDto.movieId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MOVIE_NOT_FOUND));

        var user = userService.getCurrentUser()
                .orElseThrow(() -> new UnAuthenticatedException(ErrorCode.UNAUTHENTICATED));

        if (ratingRepository.existsRatingByMovieAndUser(movie, user)) {
            throw new EntityAlreadyExistsException(ErrorCode.RATING_ALREADY_EXISTS);
        }

        var rating = ratingRepository.save(new Rating(ratingDto.rating(), user, movie, LocalDateTime.now()));

        return ratingMapper.map(rating);
    }

    @Override
    @Transactional
    public RatingDetailDto updateRating(final RatingDto ratingDto) {
        var movie = movieRepository.findById(ratingDto.movieId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MOVIE_NOT_FOUND));

        var rating = ratingRepository.findById(ratingDto.id())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RATING_NOT_FOUND));

        var user = userService.getCurrentUser()
                .orElseThrow(() -> new UnAuthenticatedException(ErrorCode.UNAUTHENTICATED));

        validateRatingData(rating, movie, user);

        //@TODO endpoint for top movies
        //@TODO check required fields in openapi
        //@TODO check validation - min and max in openapi
        //@TODO check validation - add userEmail to RatingApiDto ???
        //@TODO check validation - junit tests
        //@TODO check validation - integration tests
        //@TODO check validation - database indices
        //@TODO check validation - database constraints
        //@TODO check validation - controller advice - handle all possible exceptions
        //@TODO check csrf - learn something about it and comment why I disabled it
        //@TODO check describe all returned http codes in openapi swagger
        rating.setRatedAt(LocalDateTime.now());
        rating.setRating(ratingDto.rating());

        return ratingMapper.map(rating);
    }

    @Override
    @Transactional
    public void deleteRating(final RatingDto ratingDto) {
        var movie = movieRepository.findById(ratingDto.movieId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MOVIE_NOT_FOUND));

        var rating = ratingRepository.findById(ratingDto.id())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RATING_NOT_FOUND));

        var user = userService.getCurrentUser()
                .orElseThrow(() -> new UnAuthenticatedException(ErrorCode.UNAUTHENTICATED));

        validateRatingData(rating, movie, user);

        ratingRepository.delete(rating);
    }

    private void validateRatingData(final Rating rating, final Movie movie, final User user) {
        if (!movie.equals(rating.getMovie())) {
            throw new RequestValidationException(ErrorCode.RATING_MOVIE_MISMATCH);
        }

        if (!user.equals(rating.getUser())) {
            throw new AuthorizationCheckException(ErrorCode.RATING_USER_MISMATCH);
        }
    }

}
