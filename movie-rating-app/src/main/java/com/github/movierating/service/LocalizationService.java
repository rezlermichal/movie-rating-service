package com.github.movierating.service;

import com.github.movierating.enums.ErrorCode;

public interface LocalizationService {

    String localizeError(ErrorCode errorCode);

}
