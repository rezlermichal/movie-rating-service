package com.github.movierating.dto;

import lombok.Builder;

@Builder
public record RatingDto(Long id,
                        Long movieId,
                        Integer rating) {
}
