package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;

public class RequestValidationException extends MovieRatingException {

    public RequestValidationException(final ErrorCode errorCode) {
        super(errorCode);
    }

}
