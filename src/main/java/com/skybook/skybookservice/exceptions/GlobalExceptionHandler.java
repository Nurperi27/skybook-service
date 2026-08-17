package com.skybook.skybookservice.exceptions;

import com.fasterxml.jackson.databind.DatabindException;
import com.skybook.skybookservice.exceptions.custom.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> builderError(HttpStatus status, String message, String exceptionName) {
        ExceptionResponse error = ExceptionResponse.builder().status(status).exceptionName(exceptionName).message(message).timestamp(LocalDateTime.now()).build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmailAlreadyException.class)
    public ResponseEntity<ExceptionResponse> handleEmailAlreadyException(EmailAlreadyException e, HttpServletRequest request) {
        return builderError(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(SeatAlreadyTakenException.class)
    public ResponseEntity<ExceptionResponse> handleSeatAlreadyTakenException(SeatAlreadyTakenException e, HttpServletRequest request) {
        return builderError(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(NoAvailableSeatsException.class)
    public ResponseEntity<ExceptionResponse> handleNoAvailableSeatsException(NoAvailableSeatsException e, HttpServletRequest request) {
        return builderError(HttpStatus.CONFLICT, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ExceptionResponse> handlePaymentException(PaymentException e, HttpServletRequest request) {
        return builderError(HttpStatus.PAYMENT_REQUIRED, e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return builderError(HttpStatus.CONFLICT, "нарушение целостности данных: запись с такими данными уже существует", request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ExceptionResponse response = ExceptionResponse.builder().status(HttpStatus.BAD_REQUEST).exceptionName(e.getClass().getSimpleName()).message("Ошибка валидации входных данных").timestamp(LocalDateTime.now()).errors(errors).build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e, HttpServletRequest request) {
        return builderError(HttpStatus.INTERNAL_SERVER_ERROR, "внутренняя ошибка сервера", e.getClass().getSimpleName());
    }
}
