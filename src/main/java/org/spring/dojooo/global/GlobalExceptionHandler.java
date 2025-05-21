package org.spring.dojooo.global;

import lombok.extern.slf4j.Slf4j;
import org.spring.dojooo.auth.jwt.exception.InvalidTokenException;
import org.spring.dojooo.global.exception.NotFoundException;
import org.spring.dojooo.main.users.exception.DuplicateUserException;
import org.spring.dojooo.main.users.exception.NotFoundUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        log.error("handleMethodArgumentNotValidException", exception);
        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INVALID_INPUT, exception.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundUserException.class)
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

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserException(DuplicateUserException exception) {
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

}
