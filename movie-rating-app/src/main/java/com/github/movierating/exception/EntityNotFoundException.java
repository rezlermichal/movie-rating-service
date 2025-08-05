package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;

public class EntityNotFoundException extends MovieRatingException {

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
