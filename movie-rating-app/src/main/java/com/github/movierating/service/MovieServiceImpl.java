package com.github.movierating.service;

import com.github.movierating.dao.MovieRepository;
import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.dto.common.PageSortRequestDto;
import com.github.movierating.enums.MovieExtraFieldsProjection;
import com.github.movierating.enums.MovieSortingFields;
import com.github.movierating.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional(readOnly = true)
    public PageDto<MovieDto> findAll(final PageSortRequestDto pageSortRequestDto) {
        var sorting = prepareSorting(pageSortRequestDto);
        if ((CollectionUtils.isNotEmpty(pageSortRequestDto.projectionExtraFields()) && pageSortRequestDto.projectionExtraFields().contains(MovieExtraFieldsProjection.AVG_RATING)) ||
            MovieSortingFields.AVG_RATING == pageSortRequestDto.sortBy()) {

            return movieMapper.mapPage(movieRepository.findAllWithAvgRating(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), Sort.by(sorting.stream().map(Sort.Order::nullsLast).toList()))));
        }

        return movieMapper.map(movieRepository.findAll(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), sorting)));
    }

    private Sort prepareSorting(final PageSortRequestDto pageSortRequestDto) {
        var sortBy = pageSortRequestDto.sortBy();
        if (sortBy != null) {
            if (pageSortRequestDto.direction() != null) {
                return Sort.by(EnumUtils.getEnum(Sort.Direction.class, pageSortRequestDto.direction().name()), sortBy.getDbField());
            }
            return Sort.by(sortBy.getDbField());
        }

        return Sort.unsorted();
    }
}
