package com.github.movierating.api.rest.v1.controller;

import com.github.movierating.api.rest.dto.v1.MovieSortableFields;
import com.github.movierating.api.rest.dto.v1.ProjectionExtraField;
import com.github.movierating.api.rest.dto.v1.SortOrder;
import com.github.movierating.api.rest.v1.mapper.MovieApiDtoMapper;
import lombok.AllArgsConstructor;
import com.github.movierating.api.rest.controller.v1.MoviesApi;
import com.github.movierating.api.rest.dto.v1.PageMovieApiDto;
import com.github.movierating.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
public class MoviesController implements MoviesApi {

    private final MovieService movieService;
    private final MovieApiDtoMapper movieApiDtoMapper;

    @Override
    public ResponseEntity<PageMovieApiDto> findMovies(Integer pageNumber, Integer pageSize, MovieSortableFields sortBy, SortOrder order, List<ProjectionExtraField> extraFields) {
        log.debug("Received request to findMovies. Page number {}, page size {}, sort by {}, sort order {}, extra projection {}", pageNumber, pageSize, sortBy, order, extraFields);
        var response = movieApiDtoMapper.map(movieService.findAll(movieApiDtoMapper.map(pageNumber, pageSize, sortBy, order, extraFields)));
        log.debug("Returning a page with movies {}", response);
        return ResponseEntity.ok(response);
    }
}
