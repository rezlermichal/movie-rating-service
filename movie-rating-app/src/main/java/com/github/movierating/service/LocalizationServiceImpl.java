package com.github.movierating.service;

import com.github.movierating.enums.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private final MessageSource messageSource;

    @Override
    public String localizeError(final ErrorCode errorCode) {
        return messageSource.getMessage(errorCode.name(), null, LocaleContextHolder.getLocale());
    }
}
