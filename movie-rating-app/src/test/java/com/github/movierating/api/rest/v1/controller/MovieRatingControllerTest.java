package com.github.movierating.api.rest.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.movierating.api.rest.dto.v1.ErrorResponseApiDto;
import com.github.movierating.api.rest.dto.v1.RatingDetailApiDto;
import com.github.movierating.api.rest.v1.mapper.MovieRatingApiDtoMapperImpl;
import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.dto.RatingDto;
import com.github.movierating.dto.UserDto;
import com.github.movierating.enums.ErrorCode;
import com.github.movierating.exception.EntityAlreadyExistsException;
import com.github.movierating.exception.EntityNotFoundException;
import com.github.movierating.exception.UnAuthenticatedException;
import com.github.movierating.mapper.DateMapperImpl;
import com.github.movierating.service.LocalizationServiceImpl;
import com.github.movierating.service.MovieRatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {MovieRatingController.class, MovieRatingApiDtoMapperImpl.class, DateMapperImpl.class, LocalizationServiceImpl.class},
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class MovieRatingControllerTest {

    @MockitoBean
    private MovieRatingService movieRatingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createMovieRating_ok_checkMapping() throws Exception {
        var movieId = 1L;
        var ratingDetail = prepareRatingDetailDto();
        when(movieRatingService.createRating(any())).thenReturn(ratingDetail);
        var mockMvcResult = mockMvc.perform(post("/rest/v1/movies/{movieId}/ratings", movieId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                 {
                     "rating": "5"
                 }
                 """))
                .andExpect(status().isCreated())
                .andReturn();

        var response = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(mockMvcResult.getResponse().getContentAsString(), RatingDetailApiDto.class);
        verifyResult(ratingDetail, response);

        verify(movieRatingService).createRating(RatingDto.builder()
                .movieId(movieId)
                .rating(5)
                .build());
    }

    @Test
    void createMovieRating_movieNotFound() throws Exception {
        var movieId = 1L;
        when(movieRatingService.createRating(any())).thenThrow(new EntityNotFoundException(ErrorCode.MOVIE_NOT_FOUND));
        var mockMvcResult = mockMvc.perform(post("/rest/v1/movies/{movieId}/ratings", movieId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                 {
                     "rating": "5"
                 }
                 """))
                .andExpect(status().isNotFound())
                .andReturn();

        var response = new ObjectMapper().readValue(mockMvcResult.getResponse().getContentAsString(), ErrorResponseApiDto.class);
        assertNotNull(response.getErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals(ErrorCode.MOVIE_NOT_FOUND.name(), response.getErrors().getFirst().getCode());
        assertEquals("Given movie was not found", response.getErrors().getFirst().getMessage());
    }

    @Test
    void createMovieRating_userNotAuthenticated() throws Exception {
        var movieId = 1L;
        when(movieRatingService.createRating(any())).thenThrow(new UnAuthenticatedException(ErrorCode.UNAUTHENTICATED));
        var mockMvcResult = mockMvc.perform(post("/rest/v1/movies/{movieId}/ratings", movieId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                 {
                     "rating": "5"
                 }
                 """))
                .andExpect(status().isUnauthorized())
                .andReturn();

        var response = new ObjectMapper().readValue(mockMvcResult.getResponse().getContentAsString(), ErrorResponseApiDto.class);
        assertNotNull(response.getErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals(ErrorCode.UNAUTHENTICATED.name(), response.getErrors().getFirst().getCode());
        assertEquals("Unauthenticated", response.getErrors().getFirst().getMessage());
    }

    @Test
    void createMovieRating_ratingAlreadyExists() throws Exception {
        var movieId = 1L;
        when(movieRatingService.createRating(any())).thenThrow(new EntityAlreadyExistsException(ErrorCode.RATING_ALREADY_EXISTS));
        var mockMvcResult = mockMvc.perform(post("/rest/v1/movies/{movieId}/ratings", movieId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                 {
                     "rating": "5"
                 }
                 """))
                .andExpect(status().isConflict())
                .andReturn();

        var response = new ObjectMapper().readValue(mockMvcResult.getResponse().getContentAsString(), ErrorResponseApiDto.class);
        assertNotNull(response.getErrors());
        assertEquals(1, response.getErrors().size());
        assertEquals(ErrorCode.RATING_ALREADY_EXISTS.name(), response.getErrors().getFirst().getCode());
        assertEquals("You've already rated this movie.", response.getErrors().getFirst().getMessage());
    }

    @Test
    void updateMovieRating_ok_checkMapping() throws Exception {
        var ratingId = 123L;
        var movieId = 1L;
        var ratingDetail = prepareRatingDetailDto();
        when(movieRatingService.updateRating(any())).thenReturn(ratingDetail);
        var mockMvcResult = mockMvc.perform(put("/rest/v1/movies/{movieId}/ratings/{ratingId}", movieId, ratingId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                 {
                     "rating": "5"
                 }
                 """))
                .andExpect(status().isOk())
                .andReturn();

        var response = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(mockMvcResult.getResponse().getContentAsString(), RatingDetailApiDto.class);
        verifyResult(ratingDetail, response);

        verify(movieRatingService).updateRating(RatingDto.builder()
                .id(ratingId)
                .movieId(movieId)
                .rating(5)
                .build());
    }

    @Test
    void deleteMovieRating_ok_checkMapping() throws Exception {
        var ratingId = 123L;
        var movieId = 1L;

        mockMvc.perform(delete("/rest/v1/movies/{movieId}/ratings/{ratingId}", movieId, ratingId))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(movieRatingService).deleteRating(RatingDto.builder()
                .id(ratingId)
                .movieId(movieId)
                .build());
    }

    private void verifyResult(RatingDetailDto ratingDetailDto, RatingDetailApiDto ratingDetailApiDto) {
        assertEquals(ratingDetailDto.id(), ratingDetailApiDto.getId());
        assertNotNull(ratingDetailApiDto.getMovie());
        assertEquals(ratingDetailDto.movie().id(), ratingDetailApiDto.getMovie().getId());
        assertEquals(ratingDetailDto.movie().name(), ratingDetailApiDto.getMovie().getName());
        assertEquals(ratingDetailDto.rating(), ratingDetailApiDto.getRating());
        assertNotNull(ratingDetailApiDto.getUser());
        assertEquals(ratingDetailDto.user().id(), ratingDetailApiDto.getUser().getId());
        assertEquals(ratingDetailDto.user().email(), ratingDetailApiDto.getUser().getEmail());
    }

    private RatingDetailDto prepareRatingDetailDto() {
        return new RatingDetailDto(123L,
                new MovieDto(1L, "harry potter", null),
                new UserDto(456L, "michal@gmail.com"),
                5, LocalDateTime.now());
    }
}
