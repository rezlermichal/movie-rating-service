package com.github.movierating.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MovieSortingFields {
    NAME("name"),
    AVG_RATING("avg_rating"),
    ;

    private final String dbField;

}
