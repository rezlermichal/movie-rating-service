package com.github.movierating.dto;

import java.math.BigDecimal;

public record MovieDto(Long id,
                       String name,
                       BigDecimal avgRating) {
}
