package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieRatingException extends RuntimeException {

    private final ErrorCode errorCode;

}
