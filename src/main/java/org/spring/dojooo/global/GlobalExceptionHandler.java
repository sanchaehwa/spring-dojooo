package org.spring.dojooo.global;

import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.jwt.exception.InvalidTokenException;
import org.spring.dojooo.global.exception.ApiException;
import org.spring.dojooo.global.exception.DuplicateException;
import org.spring.dojooo.global.exception.NotFoundException;
import org.spring.dojooo.global.exception.S3Exception;
import org.spring.dojooo.main.users.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.IllegalArgumentException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("handelException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        log.error("handleMethodArgumentNotValidException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({NotFoundUserException.class, NotFoundTagException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception) {
        log.error("handleNotFoundException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_RESOURCE);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        log.error("handleIllegalArgumentException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DuplicateUserException.class, DuplicateTagException.class})
    public ResponseEntity<ErrorResponse> handleDuplicateException(DuplicateException exception) {
        log.error("handleDuplicateUserException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.CONFLICT_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidTokenException exception) {
        log.error("handleInvalidTokenException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_TOKEN);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception exception) {
        log.error("handleS3Exception", exception);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(WrongUserEditException.class)
    public ResponseEntity<ErrorResponse> handleWrongUserEditException(WrongUserEditException exception) {
        log.error("handleWrongUserEditException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.WRONG_USER_EDIT);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(NotUserEqualsCurrentUserException.class)
    public ResponseEntity<ErrorResponse> handleNotUserEqualsCurrentUserException(NotUserEqualsCurrentUserException exception) {
        log.error("handleNotUserEqualsCurrentUserException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_USER_EQUALS_CURRENTUSER);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(MaxTegLengthException.class)
    public ResponseEntity<ErrorResponse> handleMaxTegLengthException(MaxTegLengthException exception) {
        log.error("handleMaxTegLengthExceptin", exception);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MAX_TAG_LENGTH_EXCEPTION);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
