package com.github.movierating.dto.common;

import java.util.List;

public record PageDto<T>(List<T> content,
                         int pageNumber,
                         int pageSize,
                         int totalPages,
                         long totalElements){
}
