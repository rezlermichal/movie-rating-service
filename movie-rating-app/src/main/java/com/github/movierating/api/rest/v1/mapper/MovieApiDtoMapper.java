package com.github.movierating.api.rest.v1.mapper;

import com.github.movierating.api.rest.dto.v1.MovieSortableFields;
import com.github.movierating.api.rest.dto.v1.PageMovieApiDto;
import com.github.movierating.api.rest.dto.v1.ProjectionExtraField;
import com.github.movierating.api.rest.dto.v1.SortOrder;
import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.dto.common.PageSortRequestDto;
import com.github.movierating.enums.MovieExtraFieldsProjection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MovieApiDtoMapper {

    PageMovieApiDto map(PageDto<MovieDto> moviePage);

    PageSortRequestDto map(Integer pageNumber, Integer pageSize, MovieSortableFields sortBy, SortOrder direction, List<ProjectionExtraField> projectionExtraFields);

    MovieExtraFieldsProjection map(ProjectionExtraField extraFieldsEnum);

    List<MovieExtraFieldsProjection> map(List<ProjectionExtraField> extraFieldsEnums);

}
