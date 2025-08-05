package com.github.movierating.api.rest.v1.mapper;

import com.github.movierating.api.rest.dto.v1.FindMoviesPagingSortingParameter;
import com.github.movierating.api.rest.dto.v1.PageMovieApiDto;
import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.dto.common.PageSortRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MovieApiDtoMapper {

    PageMovieApiDto map(PageDto<MovieDto> moviePage);

    @Mapping(target = "direction", source = "order")
    PageSortRequestDto map(FindMoviesPagingSortingParameter parameter);

}
