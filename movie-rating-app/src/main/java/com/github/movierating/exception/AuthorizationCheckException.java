package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;

public class AuthorizationCheckException extends MovieRatingException {

    public AuthorizationCheckException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
