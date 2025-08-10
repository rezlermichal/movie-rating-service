package com.github.movierating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RatingDto(Long id,
                        @NotNull Long movieId,
                        @Min(1) @Max(10) Integer rating) {
}
