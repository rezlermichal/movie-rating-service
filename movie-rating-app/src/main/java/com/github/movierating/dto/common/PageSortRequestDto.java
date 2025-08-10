package com.github.movierating.dto.common;

import com.github.movierating.enums.MovieExtraFieldsProjection;
import com.github.movierating.enums.MovieSortingFields;

import java.util.List;

public record PageSortRequestDto(int pageNumber,
                                 int pageSize,
                                 MovieSortingFields sortBy,
                                 SortDirection direction,
                                 List<MovieExtraFieldsProjection> projectionExtraFields) {
}
