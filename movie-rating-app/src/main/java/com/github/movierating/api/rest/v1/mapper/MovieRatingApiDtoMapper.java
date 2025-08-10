package com.github.movierating.api.rest.v1.mapper;

import com.github.movierating.api.rest.dto.v1.RatingDetailApiDto;
import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.mapper.DateMapper;
import org.mapstruct.Mapper;

@Mapper(uses = DateMapper.class)
public interface MovieRatingApiDtoMapper {

    RatingDetailApiDto map(RatingDetailDto ratingDetailDto);

}
