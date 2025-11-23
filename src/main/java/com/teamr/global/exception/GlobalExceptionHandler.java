package com.teamr.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.debug("[404 NOT FOUND] Method: {}, URI: {}, RemoteAddr: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ErrorCode.NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        if (e instanceof BusinessException businessException) {
            ErrorCode errorCode = businessException.getErrorCode();
            log.error("[Business Exception] code: {}, message: {}", errorCode.getCode(), errorCode.getMessage());
            return ResponseEntity.status(errorCode.getStatus())
                    .body(ErrorResponse.of(errorCode));
        }

        log.error(
                """
                [500 SERVER ERROR]
                Method: {}
                URI: {}
                Query: {}
                RemoteAddr: {}
                Exception: {}
                """,
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                request.getRemoteAddr(),
                e.toString(),
                e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}

