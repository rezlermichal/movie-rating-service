package com.github.movierating.exception;

import com.github.movierating.enums.ErrorCode;

public class EntityAlreadyExistsException extends MovieRatingException {

    public EntityAlreadyExistsException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
