package com.aliev.mes.interaction.receiving_order.exception;

import com.aliev.mes.common.exceptions.ApiError;
import com.aliev.mes.common.exceptions.ConflictException;
import com.aliev.mes.common.exceptions.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NoDataFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoDataFoundException(NoDataFoundException exception) {
        String cause = "Ошибка при поиске данных";
        return ApiError.builder()
                .message(exception.getMessage())
                .reason(cause)
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException exception) {
        String cause = "Нарушение целостности данных";
        return ApiError.builder()
                .message(exception.getMessage())
                .reason(cause)
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
