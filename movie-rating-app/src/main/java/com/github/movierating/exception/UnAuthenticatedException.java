package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;

public class UnAuthenticatedException extends MovieRatingException {

    public UnAuthenticatedException(ErrorCode errorCode) {
        super(errorCode);
    }

}
