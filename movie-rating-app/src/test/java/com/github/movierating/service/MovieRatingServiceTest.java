package com.github.movierating.service;

import com.github.movierating.dao.MovieRepository;
import com.github.movierating.dao.RatingRepository;
import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.dto.RatingDto;
import com.github.movierating.entity.Movie;
import com.github.movierating.entity.Rating;
import com.github.movierating.entity.User;
import com.github.movierating.enums.ErrorCode;
import com.github.movierating.exception.EntityAlreadyExistsException;
import com.github.movierating.exception.EntityNotFoundException;
import com.github.movierating.exception.UnAuthenticatedException;
import com.github.movierating.mapper.RatingMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRatingServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Spy
    private RatingMapperImpl ratingMapper;

    @InjectMocks
    private MovieRatingServiceImpl movieRatingService;

    @Captor
    private ArgumentCaptor<Rating> ratingArgumentCaptor;

    @Test
    void createRating_movieNotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        var request = RatingDto.builder()
                .movieId(123L)
                .rating(5)
                .build();

        var exception = assertThrows(EntityNotFoundException.class, () -> movieRatingService.createRating(request));
        assertEquals(ErrorCode.MOVIE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void createRating_userNotAuthenticated() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(prepareMovie()));
        var request = RatingDto.builder()
                .movieId(123L)
                .rating(5)
                .build();

        when(userService.getCurrentUser()).thenReturn(Optional.empty());

        var exception = assertThrows(UnAuthenticatedException.class, () -> movieRatingService.createRating(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void createRating_ratingAlreadyExists() {
        var movie = prepareMovie();
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        var request = RatingDto.builder()
                .movieId(123L)
                .rating(5)
                .build();

        var user = prepareUser();
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));

        when(ratingRepository.existsRatingByMovieAndUser(movie, user)).thenReturn(true);

        var exception = assertThrows(EntityAlreadyExistsException.class, () -> movieRatingService.createRating(request));
        assertEquals(ErrorCode.RATING_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void createRating_ok() {
        var movie = prepareMovie();
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        var request = RatingDto.builder()
                .movieId(123L)
                .rating(5)
                .build();

        var user = prepareUser();
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));

        when(ratingRepository.existsRatingByMovieAndUser(movie, user)).thenReturn(false);

        var newRatingId = 123456789L;
        when(ratingRepository.save(any(Rating.class))).thenAnswer(i -> {
            var rating = (Rating) i.getArguments()[0];
            rating.setId(newRatingId);
            return rating;
        });

        var result = movieRatingService.createRating(request);

        verify(ratingRepository).save(ratingArgumentCaptor.capture());

        var savedRating = ratingArgumentCaptor.getValue();
        assertEquals(newRatingId, savedRating.getId());
        assertEquals(movie, savedRating.getMovie());
        assertEquals(user, savedRating.getUser());
        assertEquals(request.rating(), savedRating.getRating());
        assertNotNull(savedRating.getRatedAt());

        verifyReturnedRating(movie, savedRating, result);
    }

    @Test
    void updateRating_movieNotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        var exception = assertThrows(EntityNotFoundException.class, () -> movieRatingService.updateRating(request));
        assertEquals(ErrorCode.MOVIE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateRating_ratingNotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(prepareMovie()));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        when(ratingRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> movieRatingService.updateRating(request));
        assertEquals(ErrorCode.RATING_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateRating_userNotAuthenticated() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(prepareMovie()));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        var existentRating = prepareRating();
        when(ratingRepository.findById(request.id())).thenReturn(Optional.of(existentRating));

        when(userService.getCurrentUser()).thenReturn(Optional.empty());

        var exception = assertThrows(UnAuthenticatedException.class, () -> movieRatingService.updateRating(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void updateRating_ok() {
        var existentRating = prepareRating();
        var movie = existentRating.getMovie();
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        when(ratingRepository.findById(request.id())).thenReturn(Optional.of(existentRating));

        var user = existentRating.getUser();
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));

        var result = movieRatingService.updateRating(request);
        assertEquals(request.rating(), existentRating.getRating());
        verifyReturnedRating(movie, existentRating, result);
    }

    @Test
    void deleteRating_movieNotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        var exception = assertThrows(EntityNotFoundException.class, () -> movieRatingService.deleteRating(request));
        assertEquals(ErrorCode.MOVIE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteRating_ratingNotFound() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(prepareMovie()));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        when(ratingRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class, () -> movieRatingService.deleteRating(request));
        assertEquals(ErrorCode.RATING_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteRating_userNotAuthenticated() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(prepareMovie()));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        var existentRating = prepareRating();
        when(ratingRepository.findById(request.id())).thenReturn(Optional.of(existentRating));

        when(userService.getCurrentUser()).thenReturn(Optional.empty());

        var exception = assertThrows(UnAuthenticatedException.class, () -> movieRatingService.deleteRating(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void deleteRating_ok() {
        var existentRating = prepareRating();
        var movie = existentRating.getMovie();
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        var request = RatingDto.builder()
                .id(123456789L)
                .movieId(123L)
                .rating(5)
                .build();

        when(ratingRepository.findById(request.id())).thenReturn(Optional.of(existentRating));

        when(userService.getCurrentUser()).thenReturn(Optional.of(existentRating.getUser()));

        movieRatingService.deleteRating(request);
        verify(ratingRepository).delete(existentRating);
    }

    private void verifyReturnedRating(Movie movie, Rating savedRating, RatingDetailDto result) {
        assertEquals(savedRating.getId(), result.id());
        assertEquals(movie.getId(), result.movie().id());
        assertEquals(movie.getName(), result.movie().name());
        assertEquals(savedRating.getRating(), result.rating());
        assertEquals(savedRating.getRatedAt(), result.ratedAt());
        assertEquals(savedRating.getUser().getId(), result.user().id());
        assertEquals(savedRating.getUser().getEmail(), result.user().email());
    }

    private Movie prepareMovie() {
        var movie = new Movie();
        movie.setId(123L);
        movie.setName("Harry Potter");
        return movie;
    }

    private User prepareUser() {
        var user = new User();
        user.setId(1L);
        user.setEmail("michal@gmail.com");
        return user;
    }

    private Rating prepareRating() {
        var rating = new Rating();
        rating.setMovie(prepareMovie());
        rating.setUser(prepareUser());
        rating.setRatedAt(LocalDateTime.now());
        rating.setRating(1);
        rating.setId(123456789L);
        rating.setVersion(0L);
        return rating;
    }

}
