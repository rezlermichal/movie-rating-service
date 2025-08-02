package com.github.movierating.api.rest;

import com.github.movierating.api.rest.dto.v1.FindMoviesPagingSortingParameter;
import lombok.AllArgsConstructor;
import com.github.movierating.api.rest.controller.v1.MoviesApi;
import com.github.movierating.api.rest.dto.v1.PageMovieApiDto;
import com.github.movierating.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@Slf4j
public class MoviesController implements MoviesApi {

    private final MovieService movieService;

    @Override
    public ResponseEntity<PageMovieApiDto> findMovies(FindMoviesPagingSortingParameter pagingSorting) {
        return null;
    }
}
