package com.easyshop.orderservice.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductOrderNotFoundException.class)
    public ResponseEntity<Object> handle(
            ProductOrderNotFoundException ex, WebRequest request) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handle(
            OrderNotFoundException ex, WebRequest request) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotEditableException.class)
    public ResponseEntity<Object> handle(
            OrderNotEditableException ex, WebRequest request) {

        log.warn(ex.getMessage());
        var pd = ProblemDetail
                .forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(pd, HttpStatus.CONFLICT);
    }
}
