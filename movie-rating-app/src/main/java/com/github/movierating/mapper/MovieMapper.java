package com.github.movierating.mapper;

import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface MovieMapper {

    MovieDto map(Movie movie);

    @Mapping(target = "pageNumber", source = "number")
    @Mapping(target = "pageSize", source = "size")
    PageDto<MovieDto> map(Page<Movie> moviePage);

}
