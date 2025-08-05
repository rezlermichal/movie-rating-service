package com.github.movierating.mapper;

import com.github.movierating.dto.RatingDetailDto;
import com.github.movierating.entity.Rating;
import org.mapstruct.Mapper;

@Mapper
public interface RatingMapper {

    RatingDetailDto map(Rating rating);

}
