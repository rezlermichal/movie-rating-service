package com.github.movierating.api.rest.v1.controller;

import com.github.movierating.api.rest.dto.v1.FindMoviesPagingSortingParameter;
import com.github.movierating.api.rest.v1.mapper.MovieApiDtoMapper;
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
    private final MovieApiDtoMapper movieApiDtoMapper;

    @Override
    public ResponseEntity<PageMovieApiDto> findMovies(final FindMoviesPagingSortingParameter pagingSorting) {
        log.debug("Received request to findMovies {}", pagingSorting);
        var response = movieApiDtoMapper.map(movieService.findAll(movieApiDtoMapper.map(pagingSorting)));
        log.debug("Returning a page with movies {}", response);
        return ResponseEntity.ok(response);
    }
}
