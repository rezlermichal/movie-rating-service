package com.github.movierating.service;

import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.dto.common.PageSortRequestDto;

public interface MovieService {

    PageDto<MovieDto> findAll(PageSortRequestDto pageSortRequestDto);

}
