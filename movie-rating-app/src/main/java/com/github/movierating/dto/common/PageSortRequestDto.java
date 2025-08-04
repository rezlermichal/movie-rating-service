package com.github.movierating.dto.common;

public record PageSortRequestDto(int pageNumber,
                                 int pageSize,
                                 String sortBy,
                                 SortDirection direction) {
}
