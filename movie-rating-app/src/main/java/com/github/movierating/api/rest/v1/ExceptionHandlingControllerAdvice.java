package com.github.movierating.api.rest.v1;

import com.github.movierating.api.rest.dto.v1.ErrorApiDto;
import com.github.movierating.api.rest.dto.v1.ErrorResponseApiDto;
import com.github.movierating.enums.ErrorCode;
import com.github.movierating.exception.*;
import com.github.movierating.service.LocalizationService;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandlingControllerAdvice {

    private final LocalizationService localizationService;

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseApiDto handleBindException(BindException exception) {
        log.debug("BindException occurred", exception);
        return createErrorResponseApiDto("INVALID_REQUEST_CONTENT", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseApiDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.debug("MethodArgumentTypeMismatchException occurred", exception);
        return createErrorResponseApiDto("INVALID_REQUEST_CONTENT", exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseApiDto handleEntityNotFoundException(EntityNotFoundException exception) {
        log.debug("EntityNotFoundException occurred", exception);
        return createErrorResponseApiDto(exception.getErrorCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseApiDto handleConstraintViolationException(ConstraintViolationException exception) {
        log.debug("ConstraintViolationException occurred", exception);
        return createErrorResponseApiDto("INVALID_REQUEST_CONTENT", exception.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseApiDto handleEntityAlreadyExistsException(EntityAlreadyExistsException exception) {
        log.debug("EntityAlreadyExistsException occurred", exception);
        return createErrorResponseApiDto(exception.getErrorCode());
    }

    @ExceptionHandler(AuthorizationCheckException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseApiDto handleAuthorizationCheckException(AuthorizationCheckException exception) {
        log.debug("AuthorizationCheckException occurred", exception);
        return createErrorResponseApiDto(exception.getErrorCode());
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseApiDto handleUnauthenticatedException(UnAuthenticatedException exception) {
        log.debug("UnauthenticatedException occurred", exception);
        return createErrorResponseApiDto(exception.getErrorCode());
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseApiDto handleRequestValidationException(RequestValidationException exception) {
        log.debug("RequestValidationException occurred", exception);
        return createErrorResponseApiDto(exception.getErrorCode());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseApiDto handleRuntimeException(RuntimeException exception) {
        log.error("Unhandled exception occurred", exception);
        return createErrorResponseApiDto(ErrorCode.OTHER_ERROR);
    }

    private ErrorResponseApiDto createErrorResponseApiDto(final ErrorCode code) {
        return createErrorResponseApiDto(code.name(), localizationService.localizeError(code));
    }

    private ErrorResponseApiDto createErrorResponseApiDto(final String code, final String message) {
        var errorResponse = new ErrorResponseApiDto();
        var errorItem = new ErrorApiDto();
        errorItem.setCode(code);
        errorItem.setMessage(message);
        errorResponse.addErrorsItem(errorItem);
        return errorResponse;
    }
}
