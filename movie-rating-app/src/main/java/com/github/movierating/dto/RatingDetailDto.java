package com.github.movierating.dto;

import java.time.LocalDateTime;

public record RatingDetailDto(Long id,
                              MovieDto movie,

                              UserDto user,
                              int rating,
                              LocalDateTime ratedAt) {
}
