package com.github.movierating.service;

import com.github.movierating.dao.MovieRepository;
import com.github.movierating.dto.MovieDto;
import com.github.movierating.dto.common.PageDto;
import com.github.movierating.dto.common.PageSortRequestDto;
import com.github.movierating.mapper.MovieMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional(readOnly = true)
    public PageDto<MovieDto> findAll(final PageSortRequestDto pageSortRequestDto) {
        var top = movieRepository.findAllWithAvgRating(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), Sort.unsorted()));
        var top2 = movieRepository.findAllWithAvgRating(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), Sort.by(Sort.Direction.DESC, "avgRating")));
        var top3 = movieRepository.findAllWithAvgRating(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), Sort.by(Sort.Direction.ASC, "avgRating")));
        //@TODO fix pageRequest and sorting - ideally using mapstruct....
        return movieMapper.map(movieRepository.findAll(PageRequest.of(pageSortRequestDto.pageNumber(), pageSortRequestDto.pageSize(), Sort.unsorted())));
    }
}
